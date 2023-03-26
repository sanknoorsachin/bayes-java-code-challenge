package gg.bayes.challenge.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gg.bayes.challenge.exception.DotaException;
import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.CombatLogEntryEntity.Type;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.persistence.repository.CombatLogEntryRepository;
import gg.bayes.challenge.persistence.repository.MatchRepository;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

@Service
public class MatchService {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(MatchService.class);

	public static String BUY_EVENT = "buys item";
	public static String CAST_EVENT = "casts ability";
	public static String HIT_EVENT = "hits";
	public static String KILL_EVENT = "killed";

	private MatchRepository matchRepository;
	private CombatLogEntryRepository combatLogEntryRepository;

	@Autowired
	public MatchService(MatchRepository matchRepository, CombatLogEntryRepository combatLogEntryRepository) {
		this.matchRepository = matchRepository;
		this.combatLogEntryRepository = combatLogEntryRepository;
	}

	/**
	 * Method to process DOTA combact logs and save the events in DB
	 * 
	 * @param logPath - log file path
	 * @throws IOException
	 */

	public void processLogFile(String logPath) throws DotaException, IOException {
		try {
			List<String> allLines = Files.readAllLines(Paths.get(logPath));
			MatchEntity matchEntity = new MatchEntity();
			CombatLogEntryEntity combLogEntry = null;
			Set<CombatLogEntryEntity> combatLogEntries = new HashSet<>();

			for (String record : allLines) {
				if (record.contains(BUY_EVENT)) {
					combLogEntry = new CombatLogEntryEntity();
					String data[] = record.split(" ");
					getTimestamp(combLogEntry, data[0]);
					getActorName(combLogEntry, data[1]);
					combLogEntry.setItem(data[data.length - 1].substring(5));
					combLogEntry.setType(Type.ITEM_PURCHASED);
					combLogEntry.setMatch(matchEntity);
					combatLogEntries.add(combLogEntry);

				} else if (record.contains(CAST_EVENT)) {
					combLogEntry = new CombatLogEntryEntity();
					String data[] = record.split(" ");
					getTimestamp(combLogEntry, data[0]);
					getActorName(combLogEntry, data[1]);
					int indexofAbility = Arrays.asList(data).indexOf("ability");
					combLogEntry.setAbility(data[indexofAbility + 1]);
					combLogEntry.setType(Type.SPELL_CAST);
					combLogEntry.setMatch(matchEntity);
					combatLogEntries.add(combLogEntry);

				} else if (record.contains(HIT_EVENT)) {
					combLogEntry = new CombatLogEntryEntity();
					String data[] = record.split(" ");
					getTimestamp(combLogEntry, data[0]);
					getActorName(combLogEntry, data[1]);
					int indexofDamage = Arrays.asList(data).indexOf("damage");
					combLogEntry.setDamage(Integer.parseInt(data[indexofDamage - 1]));
					combLogEntry.setType(Type.DAMAGE_DONE);
					int indexofHits = Arrays.asList(data).indexOf("hits");
					combLogEntry.setTarget(data[indexofHits + 1].substring(14));
					combLogEntry.setMatch(matchEntity);
					combatLogEntries.add(combLogEntry);

				} else if (record.contains(KILL_EVENT)) {
					combLogEntry = new CombatLogEntryEntity();
					String data[] = record.split(" ");
					getTimestamp(combLogEntry, data[0]);
					getActorName(combLogEntry, data[data.length - 1]);
					combLogEntry.setTarget(data[1].substring(14));
					combLogEntry.setType(Type.HERO_KILLED);
					combLogEntry.setMatch(matchEntity);
					combatLogEntries.add(combLogEntry);
				}

			}

			matchEntity.setCombatLogEntries(combatLogEntries);
			matchRepository.save(matchEntity);

		} catch (DotaException e) {
			logger.debug("Exception occured while processing and ingestion of logs" + e.getMessage());
		}
	}

	/**
	 * Method to fetch items brought by hero for given match id
	 * 
	 * @param id   - match id
	 * @param name - actor name
	 * @return
	 */

	public List<HeroItem> fetchItemsBroughtByHero(Long id, String name) {
		HeroItem heroItem = null;
		List<HeroItem> itemList = new ArrayList<>();
		try {

			List<CombatLogEntryEntity> items = combatLogEntryRepository.findItemsBroughtByActor(id, name);

			for (CombatLogEntryEntity record : items) {
				heroItem = new HeroItem(record.getItem(), record.getTimestamp());
				itemList.add(heroItem);
			}
		} catch (DotaException de) {
			throw new DotaException("Exception occured while fetching items", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return itemList;

	}

	/**
	 * Method to find kill count for hero
	 * 
	 * @param id - match id
	 * @return
	 */

	public List<HeroKills> findKillCountForHeros(Long id) {
		List<HeroKills> killCounts = null;
		try {
			killCounts = combatLogEntryRepository.findKillCountForHeros(id);
		} catch (DotaException de) {
			throw new DotaException("Exception occured while fetching kill count", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return killCounts;

	}

	/**
	 * Method to find spell cast by a actor for a given match id
	 * 
	 * @param id   - match id
	 * @param name - actor
	 * @return
	 */

	public List<HeroSpells> findSpellCastByActor(Long id, String name) {
		List<HeroSpells> heroSpells = null;
		try {
			heroSpells = combatLogEntryRepository.findSpellCastByActor(id, name);
		} catch (DotaException de) {
			throw new DotaException("Exception occured while fetching spell cast by actor",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return heroSpells;
	}

	/**
	 * Method to find damage done for actor
	 * 
	 * @param id   -match id
	 * @param name - actor
	 * @return
	 */
	public List<HeroDamage> findDamageDoneForActor(Long id, String name) {
		List<HeroDamage> heroDamage = null;
		try {
			heroDamage = combatLogEntryRepository.findDamageDoneForActor(id, name);
		} catch (DotaException de) {
			throw new DotaException("Exception occured while fetching damage done for  actor",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return heroDamage;
	}

	/**
	 * Parse timestamp from the log
	 * 
	 * @param combLogEntry - combLogEntry
	 * @param record       - record in the file
	 */

	private void getTimestamp(CombatLogEntryEntity combLogEntry, String record) {
		try {
			combLogEntry.setTimestamp((record.replaceAll("\\[", " ").replaceAll("\\]", " ").trim()));
		} catch (Exception e) {
			logger.debug("File may contain data  not in specified format skipping such records");
		}

	}

	/**
	 * Parse actor name from the log
	 * 
	 * @param combLogEntry - combLogEntry
	 * @param record       - record in the file
	 */

	private void getActorName(CombatLogEntryEntity combLogEntry, String record) {
		try {
			combLogEntry.setActor(record.substring(14));
		} catch (Exception e) {
			logger.debug("File may contain data  not in specified format skipping such records");
		}

	}

}

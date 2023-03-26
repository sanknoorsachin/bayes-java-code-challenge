package gg.bayes.challenge.rest.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/match")
@Validated
public class MatchController {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(MatchController.class);
	private MatchService matchService;

	@Autowired
	public MatchController(MatchService matchService) {
		this.matchService = matchService;
	}

	/**
	 * Ingests a DOTA combat log file, parses and persists relevant events data. All
	 * events are associated with the same match id.
	 *
	 * @param combatLog the content of the combat log file
	 * @return the match id associated with the parsed events
	 * @throws IOException
	 */
	@PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Long> ingestCombatLog(@RequestBody @NotBlank String combatLog) throws IOException {
		logger.info("DOTA combat log file ingestion process started");
		matchService.processLogFile(combatLog);

		logger.info("DOTA combat log file ingestion completed");
		return new ResponseEntity<>(HttpStatus.CREATED);

	}

	/**
	 * Fetches the heroes and their kill counts for the given match.
	 *
	 * @param matchId the match identifier
	 * @return a collection of heroes and their kill counts
	 */
	@GetMapping(path = "{matchId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
		logger.info("Getmatch api exceution started  ");
		
		List<HeroKills> killCount = matchService.findKillCountForHeros(matchId);
		HttpStatus status = null;

		if (killCount.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		} else {
			status = HttpStatus.OK;
		}

		logger.info("Get match api exceution completed  ");
		return new ResponseEntity<>(killCount, status);

	}

	/**
	 * For the given match, fetches the items bought by the named hero.
	 *
	 * @param matchId  the match identifier
	 * @param heroName the hero name
	 * @return a collection of items bought by the hero during the match
	 */
	@GetMapping(path = "{matchId}/{heroName}/items", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HeroItem>> getHeroItems(@PathVariable("matchId") Long matchId,
			@PathVariable("heroName") String heroName) {
		logger.info("GetHeroItems api execution started");
		
		List<HeroItem> heroItems = matchService.fetchItemsBroughtByHero(matchId, heroName);
		HttpStatus status = null;
		
		if (heroItems.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		} else {
			status = HttpStatus.OK;
		}
		logger.info("GetHeroItems api execution completed");
		return new ResponseEntity<>(heroItems, status);

	}

	/**
	 * For the given match, fetches the spells cast by the named hero.
	 *
	 * @param matchId  the match identifier
	 * @param heroName the hero name
	 * @return a collection of spells cast by the hero and how many times they were
	 *         cast
	 */
	@GetMapping(path = "{matchId}/{heroName}/spells", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HeroSpells>> getHeroSpells(@PathVariable("matchId") Long matchId,
			@PathVariable("heroName") String heroName) {
		logger.info("GetHeroSpells api execution started");
		List<HeroSpells> spells = matchService.findSpellCastByActor(matchId, heroName);
         
		HttpStatus status = null;
		
		if (spells.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		} else {
			status = HttpStatus.OK;
		}

		logger.info("GetHeroSpells api execution Completed");
		return new ResponseEntity<>(spells, status);

	}

	/**
	 * For a given match, fetches damage done data for the named hero.
	 *
	 * @param matchId  the match identifier
	 * @param heroName the hero name
	 * @return a collection of "damage done" (target, number of times and total
	 *         damage) elements
	 */
	@GetMapping(path = "{matchId}/{heroName}/damage", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HeroDamage>> getHeroDamages(@PathVariable("matchId") Long matchId,
			@PathVariable("heroName") String heroName) {
		logger.info("GetHeroDamages api execution started");
		List<HeroDamage> damages = matchService.findDamageDoneForActor(matchId, heroName);
		
        HttpStatus status = null;
		
		if (damages.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		} else {
			status = HttpStatus.OK;
		}

		logger.info("GetHeroDamages api execution Completed");
		return new ResponseEntity<>(damages, status);

	}
}

package gg.bayes.challenge.rest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.CombatLogEntryEntity.Type;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItem;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;

@WebMvcTest(MatchController.class)
public class MatchControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private MatchService matchService;

	/**
	 * Test for log ingest combact log
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	void test_ingestCombatLog() throws JsonProcessingException, Exception {
		MatchEntity matchEntity = new MatchEntity();
		Set<CombatLogEntryEntity> combatLogEntries = new HashSet<>();
		CombatLogEntryEntity combactEntyity = new CombatLogEntryEntity();

		combactEntyity.setTimestamp("time_stamp");
		combactEntyity.setType(Type.DAMAGE_DONE);
		combactEntyity.setActor("actor");

		combatLogEntries.add(combactEntyity);
		matchEntity.setCombatLogEntries(combatLogEntries);

		mockMvc.perform(post("/api/match").contentType(MediaType.TEXT_PLAIN_VALUE)
				.content(objectMapper.writeValueAsString(matchEntity))).andExpect(status().isCreated()).andDo(print());

	}

	/**
	 * Test for kill count for hero
	 * 
	 * @throws Exception
	 */

	@Test
	void test_getMatch() throws Exception {

		HeroKills heroKills1 = new HeroKills("test1", 10L);
		HeroKills heroKills2 = new HeroKills("test2", 20L);

		List<HeroKills> listOfKills = new ArrayList<HeroKills>();
		listOfKills.add(heroKills1);
		listOfKills.add(heroKills2);

		when(matchService.findKillCountForHeros(1L)).thenReturn(listOfKills);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/match/{matchId}", 1).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(listOfKills.size()))
				.andDo(print());

	}

	/**
	 * Test to fetch items brought by hero
	 * 
	 * @throws Exception
	 */
	@Test
	void test_getHeroItems() throws Exception {
		HeroItem heroItem1 = new HeroItem("item1", "timestamp1");
		HeroItem heroItem2 = new HeroItem("item2", "timestamp2");

		List<HeroItem> listOFHeroItems = new ArrayList<HeroItem>();
		listOFHeroItems.add(heroItem1);
		listOFHeroItems.add(heroItem2);

		when(matchService.fetchItemsBroughtByHero(1L, "actor")).thenReturn(listOFHeroItems);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/match/{matchId}/{heroName}/items", 1, "actor")
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(listOFHeroItems.size())).andDo(print());

	}

	/**
	 * Test for spell cast by actor
	 * 
	 * @throws Exception
	 */
	@Test
	void test_getHeroSpells() throws Exception {
		HeroSpells heroSpells1 = new HeroSpells("spell", 10L);
		HeroSpells heroSpells2 = new HeroSpells("spell", 10L);

		List<HeroSpells> listOFHeroSpells = new ArrayList<HeroSpells>();
		listOFHeroSpells.add(heroSpells1);
		listOFHeroSpells.add(heroSpells2);

		when(matchService.findSpellCastByActor(1L, "actor")).thenReturn(listOFHeroSpells);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/match/{matchId}/{heroName}/spells", 1, "actor")
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(listOFHeroSpells.size())).andDo(print());

	}

	/**
	 * Test to find damage done by actor
	 * 
	 * @throws Exception
	 */
	@Test
	void test_getHeroDamages() throws Exception {
		HeroDamage HeroDamage1 = new HeroDamage("target1", 1, 10L);
		HeroDamage HeroDamage2 = new HeroDamage("target2", 2, 20L);

		List<HeroDamage> listOFHeroDamage = new ArrayList<HeroDamage>();
		listOFHeroDamage.add(HeroDamage1);
		listOFHeroDamage.add(HeroDamage2);

		when(matchService.findDamageDoneForActor(1L, "actor")).thenReturn(listOFHeroDamage);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/match/{matchId}/{heroName}/damage", 1, "actor")
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(listOFHeroDamage.size())).andDo(print());
	}
}

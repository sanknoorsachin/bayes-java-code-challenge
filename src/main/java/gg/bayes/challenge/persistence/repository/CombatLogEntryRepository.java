package gg.bayes.challenge.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

@Repository
public interface CombatLogEntryRepository extends JpaRepository<CombatLogEntryEntity, Long> {

	/**
	 * Query to Fetch items brought by actor
	 * 
	 * @param id   - match id
	 * @param name - actor
	 * @return
	 */
	@Query(value = "SELECT * FROM dota_combat_log  u WHERE u.match_id = ?1 AND u.actor= ?2 AND u.entry_type='ITEM_PURCHASED'", nativeQuery = true)
	List<CombatLogEntryEntity> findItemsBroughtByActor(@Param("id") Long id, @Param("name") String name);

	/**
	 * Query to find kill cout for the hero
	 * 
	 * @param id - match id
	 * @return
	 */
	@Query(value = "SELECT new gg.bayes.challenge.rest.model.HeroKills(L.actor,count(L.type)) FROM CombatLogEntryEntity L inner join L.match M where M.id= :id AND L.type='HERO_KILLED' GROUP BY L.actor")
	List<HeroKills> findKillCountForHeros(@Param("id") Long id);

	/**
	 * Query to find spell cast by actor for a given match id
	 * 
	 * @param id   - match id
	 * @param name - actor
	 * @return
	 */
	@Query(value = "SELECT new gg.bayes.challenge.rest.model.HeroSpells(L.ability,count(L.type)) FROM CombatLogEntryEntity L inner join L.match M where M.id= :id AND L.actor= :name AND L.type='SPELL_CAST' GROUP BY L.ability")
	List<HeroSpells> findSpellCastByActor(@Param("id") Long id, @Param("name") String name);

	/**
	 * Query to find damage done for actor in given match
	 * 
	 * @param id   - natch id
	 * @param name - actor
	 * @return
	 */
	@Query(value = "SELECT new gg.bayes.challenge.rest.model.HeroDamage(L.target,L.damage,count(L.type)) FROM CombatLogEntryEntity L inner join L.match M  where M.id= :id AND L.actor= :name AND L.type='DAMAGE_DONE' GROUP BY L.target,L.damage")
	List<HeroDamage> findDamageDoneForActor(@Param("id") Long id, @Param("name") String name);

}

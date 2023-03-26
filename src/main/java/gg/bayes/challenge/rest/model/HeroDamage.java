package gg.bayes.challenge.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HeroDamage {
    String target;
    @JsonProperty("damage_instances")
    Integer damageInstances;
    @JsonProperty("total_damage")
    Long totalDamage;
	
    public HeroDamage(String target, Integer damageInstances, Long totalDamage) {
		this.target = target;
		this.damageInstances = damageInstances;
		this.totalDamage = totalDamage;
	}
    
    
}

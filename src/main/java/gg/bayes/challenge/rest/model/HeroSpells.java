package gg.bayes.challenge.rest.model;

import lombok.Data;

@Data
public class HeroSpells {
	String spell;
	Long casts;

	public HeroSpells(String spell, Long casts) {
		this.spell = spell;
		this.casts = casts;
	}

}

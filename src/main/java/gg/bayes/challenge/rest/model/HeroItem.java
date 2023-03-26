package gg.bayes.challenge.rest.model;

import lombok.Value;

@Value
public class HeroItem {
	String item;
	String timestamp;

	public HeroItem(String item, String timestamp) {
		this.item = item;
		this.timestamp = timestamp;
	}

}

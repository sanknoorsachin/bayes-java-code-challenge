package gg.bayes.challenge.persistence.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dota_match")
public class MatchEntity implements Serializable {


	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dota_match_sequence_generator")
	@SequenceGenerator(name = "dota_match_sequence_generator", sequenceName = "dota_match_sequence", allocationSize = 1)
	@Id
	@Column(name = "id")
	private Long id;

	@OneToMany(mappedBy = "match", cascade = CascadeType.PERSIST)
	private Set<CombatLogEntryEntity> combatLogEntries = new HashSet<>();
}
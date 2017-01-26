package com.jnaka.golf.domain.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jnaka.domain.xml.IdAdapter;
import com.jnaka.golf.domain.Season.HandicapPolicy;
import com.jnaka.golf.domain.Season.ScoringPolicy;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Season {

	@XmlID
	@XmlAttribute(name = "seasonID")
	@XmlJavaTypeAdapter(IdAdapter.class)
	private Integer id;
	private ScoringPolicy scoringPolicy = ScoringPolicy.DEFAULT_20;
	private HandicapPolicy handicapPolicy = HandicapPolicy.FIVE_OF_TEN;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ScoringPolicy getScoringPolicy() {
		return scoringPolicy;
	}

	public void setScoringPolicy(ScoringPolicy scoringPolicy) {
		this.scoringPolicy = scoringPolicy;
	}

	public HandicapPolicy getHandicapPolicy() {
		return handicapPolicy;
	}

	public void setHandicapPolicy(HandicapPolicy handicapPolicy) {
		this.handicapPolicy = handicapPolicy;
	}

}

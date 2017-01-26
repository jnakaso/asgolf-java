package com.jnaka.golf.domain.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jnaka.domain.xml.IdAdapter;
import com.jnaka.domain.xml.YesNoAdapter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Player {

	@XmlID
	@XmlAttribute(name = "playerID")
	@XmlJavaTypeAdapter(IdAdapter.class)
	private Number id;

	@XmlAttribute
	private String firstName;

	@XmlAttribute
	private String lastName;

	@XmlAttribute
	@XmlJavaTypeAdapter(YesNoAdapter.class)
	private Boolean active;

	@XmlElement(name = "Season")
	private List<SeasonSummary> seasonSummaries = new ArrayList<SeasonSummary>();

	@XmlAttribute(name = "hdcp")
	private float handicap;

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public float getHandicap() {
		return handicap;
	}

	public void setHandicap(float handicap) {
		this.handicap = handicap;
	}

	public List<SeasonSummary> getSeasonSummaries() {
		return seasonSummaries;
	}

	public void setSeasonSummaries(List<SeasonSummary> seasonSummaries) {
		this.seasonSummaries = seasonSummaries;
	}

}

package com.jnaka.golf.domain.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jnaka.domain.xml.CurrencyAdapter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class SeasonSummary {

	//@XmlIDREF
	@XmlAttribute(name = "seasonID")
	private Integer seasonID;
	
	@XmlTransient
	private Season season;
	
	@XmlAttribute()
	private Float points;
	@XmlAttribute()
	@XmlJavaTypeAdapter(CurrencyAdapter.class)
	private Float earnings;
	@XmlAttribute()
	private int kps;
	@XmlAttribute()
	private int attendance;

	public Integer getSeasonID() {
		return seasonID;
	}

	public void setSeasonID(Integer seasonID) {
		this.seasonID = seasonID;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
		this.seasonID = season.getId();
	}

	public float getPoints() {
		return points;
	}

	public void setPoints(float points) {
		this.points = points;
	}

	public float getEarnings() {
		return earnings;
	}

	public void setEarnings(float earnings) {
		this.earnings = earnings;
	}

	public int getKps() {
		return kps;
	}

	public void setKps(int kps) {
		this.kps = kps;
	}

	public int getAttendance() {
		return attendance;
	}

	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}

}

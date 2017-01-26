package com.jnaka.golf.domain.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jnaka.domain.xml.IntegerArrayAdapter;
import com.jnaka.domain.xml.Number2IntegerAdapter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class CourseTee {

	@XmlAttribute
	private String name;
	private Float rating;
	@XmlJavaTypeAdapter(Number2IntegerAdapter.class)
	private Integer slope;
	@XmlElement(name = "holePars")
	@XmlJavaTypeAdapter(IntegerArrayAdapter.class)
	private Integer[] pars = new Integer[18];
	@XmlElement(name = "holeHdcp")
	@XmlJavaTypeAdapter(IntegerArrayAdapter.class)
	private Integer[] handicaps = new Integer[18];

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	public Integer getSlope() {
		return slope;
	}

	public void setSlope(Integer slope) {
		this.slope = slope;
	}

	public Integer[] getPars() {
		return pars;
	}

	public void setPars(Integer[] pars) {
		this.pars = pars;
	}

	public Integer[] getHandicaps() {
		return handicaps;
	}

	public void setHandicaps(Integer[] handicaps) {
		this.handicaps = handicaps;
	}

}

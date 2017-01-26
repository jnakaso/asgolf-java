package com.jnaka.golf.domain;

import java.util.ArrayList;
import java.util.List;

public class CourseTee {

	private transient Course course;

	private String name;
	private Float rating = 0f;
	private Integer slope = 0;
	private List<Integer> pars = new ArrayList<>(18);
	private List<Integer> handicaps = new ArrayList<>(18);

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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Integer> getPars() {
		return pars;
	}

	public void setPars(List<Integer> pars) {
		this.pars = pars;
	}

	public List<Integer> getHandicaps() {
		return handicaps;
	}

	public void setHandicaps(List<Integer> handicaps) {
		this.handicaps = handicaps;
	}

}

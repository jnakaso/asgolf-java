package com.jnaka.golf.domain;


public class CourseTee {

	private transient Course course;

	private String name;
	private Float rating = 0f;
	private Integer slope = 0;
	private Integer[] pars = new Integer[18];
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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
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

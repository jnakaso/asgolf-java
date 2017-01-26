package com.jnaka.golf.domain.json;

public class JsCourse {

	private Number id;
	private String name;
	private Float rating = 0f;
	private Integer slope = 0;

	public Number getId() {
		return id;
	}
	public void setId(Number id) {
		this.id = id;
	}
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

}

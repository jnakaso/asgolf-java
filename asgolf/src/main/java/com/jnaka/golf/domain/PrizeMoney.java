package com.jnaka.golf.domain;

public class PrizeMoney {

	private Integer place;
	private Float earnings;
	private Float points;
	
	public PrizeMoney(Integer place, Float earnings, Float points) {
		super();
		this.place = place;
		this.earnings = earnings;
		this.points = points;
	}
	public Float getEarnings() {
		return earnings;
	}
	public void setEarnings(Float earnings) {
		this.earnings = earnings;
	}
	public Float getPoints() {
		return points;
	}
	public void setPoints(Float points) {
		this.points = points;
	}
	public Integer getPlace() {
		return place;
	}
	public void setPlace(Integer place) {
		this.place = place;
	}
	
	
}

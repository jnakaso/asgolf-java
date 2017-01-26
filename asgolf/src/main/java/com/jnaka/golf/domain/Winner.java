package com.jnaka.golf.domain;

public class Winner {
	private Round round;
	private Float points = 0f;
	private String finish;
	private Float earnings = 0f;

	public Winner() {
		super();
	}

	public Winner(Round round, String finish, Float points, Float earnings) {
		super();
		this.round = round;
		this.points = points;
		this.finish = finish;
		this.earnings = earnings;
	}

	public Round getRound() {
		return round;
	}

	public Float getPoints() {
		return points;
	}

	public String getFinish() {
		return finish;
	}

	public Float getEarnings() {
		return earnings;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	public void setPoints(Float points) {
		this.points = points;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public void setEarnings(Float earnings) {
		this.earnings = earnings;
	}
}

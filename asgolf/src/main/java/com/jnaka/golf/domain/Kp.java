package com.jnaka.golf.domain;

public class Kp {

	private Player player;
	private Flight flight = Flight.A;
	private String hole;

	public Kp() {
		super();
	}

	public Kp(Player player, Flight flight, String hole) {
		super();
		this.player = player;
		this.flight = flight;
		this.hole = hole;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public String getHole() {
		return hole;
	}

	public void setHole(String hole) {
		this.hole = hole;
	}

}

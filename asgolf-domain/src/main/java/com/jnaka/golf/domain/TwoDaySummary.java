/**
 * 
 */
package com.jnaka.golf.domain;

import org.apache.commons.lang.StringUtils;

public class TwoDaySummary {
	private Player player;
	private Flight flight;
	private Integer handicap = 0;
	private Integer total = 0;
	private Integer totalNet = 0;
	private Integer numRound = 0;

	private String standing = StringUtils.EMPTY;
	private Integer dayOne = 0;
	private Integer dayTwo = 0;

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

	public Integer getHandicap() {
		return handicap;
	}

	public void setHandicap(Integer handicap) {
		this.handicap = handicap;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotalNet() {
		return totalNet;
	}

	public void setTotalNet(Integer totalNet) {
		this.totalNet = totalNet;
	}

	public Integer getNumRound() {
		return numRound;
	}

	public void setNumRound(Integer numRound) {
		this.numRound = numRound;
	}

	public String getStanding() {
		return standing;
	}

	public void setStanding(String standing) {
		this.standing = standing;
	}

	public Integer getDayOne() {
		return dayOne;
	}

	public void setDayOne(Integer dayOne) {
		this.dayOne = dayOne;
	}

	public Integer getDayTwo() {
		return dayTwo;
	}

	public void setDayTwo(Integer dayTwo) {
		this.dayTwo = dayTwo;
	}

}
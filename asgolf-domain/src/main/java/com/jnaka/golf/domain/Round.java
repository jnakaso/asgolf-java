package com.jnaka.golf.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.jnaka.domain.EntityObjectImpl;

public class Round extends EntityObjectImpl implements PlayerHolder {

	private Tournament tournament;
	private Player player;
	private Flight flight = Flight.A;
	private Integer handicap = 0;
	private Integer front = 0;
	private Integer back = 0;
	private Integer total = 0;
	private Float frontNet = 0f;
	private Float backNet = 0f;
	private Float totalNet = 0f;
	private Integer[] scores = new Integer[18];
	private String accepted = "";
	private Integer adjusted = 0;

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public Tournament getTournament() {
		return tournament;
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

	public Integer getHandicap() {
		return handicap;
	}

	public void setHandicap(Integer handicap) {
		this.handicap = handicap;
	}

	public Integer getFront() {
		return front;
	}

	public void setFront(Integer front) {
		this.front = front;
	}

	public Integer getBack() {
		return back;
	}

	public void setBack(Integer back) {
		this.back = back;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Float getFrontNet() {
		return frontNet;
	}

	public void setFrontNet(Float frontNet) {
		this.frontNet = frontNet;
	}

	public Float getBackNet() {
		return backNet;
	}

	public void setBackNet(Float backNet) {
		this.backNet = backNet;
	}

	public Float getTotalNet() {
		return totalNet;
	}

	public void setTotalNet(Float totalNet) {
		this.totalNet = totalNet;
	}

	public Integer[] getScores() {
		return scores;
	}

	public void setScores(Integer[] scores) {
		this.scores = scores;
	}

	public String getAccepted() {
		return this.accepted;
	}

	public void setAccepted(String accepted) {
		this.accepted = accepted;
	}

	public Integer getAdjusted() {
		return adjusted;
	}

	public void setAdjusted(Integer adjusted) {
		this.adjusted = adjusted;
	}

	@Override
	public boolean equals(Object that) {
		if (that == null) {
			return false;
		}
		if (that instanceof Round == false) {
			return false;
		}
		if (this == that) {
			return true;
		}
		return new EqualsBuilder() //
				.append(this.getId(), ((Round) that).getId()) //
				.isEquals();
	}

	@Override
	public int hashCode() {
		if (this.getId() == null) {
			return super.hashCode();
		}
		return new HashCodeBuilder(13, 37) //
				.append(this.getId()) //
				.hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE) //
				.appendSuper(super.toString()) //
				.append("Player", this.getPlayer()) //
				.append("total", this.total) //
				.append("Tournament", this.getTournament()) //
				.toString();
	}

}

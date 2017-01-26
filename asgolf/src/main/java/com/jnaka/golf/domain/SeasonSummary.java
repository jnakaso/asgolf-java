package com.jnaka.golf.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SeasonSummary implements PlayerHolder {

	private Player player;
	private Season season;
	private Float points = 0f;
	private Float earnings = 0f;
	private int kps = 0;
	private int attendance = 0;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	public int getSeasonID() {
		return this.getSeason().getId().intValue();
	}

	public Float getPoints() {
		return points;
	}

	public void setPoints(Float points) {
		this.points = points;
	}

	public Float getEarnings() {
		return earnings;
	}

	public void setEarnings(Float earnings) {
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

	/**
	 * Used only for sorting
	 * 
	 * @return
	 */
	public String getPlayerName() {
		return this.getPlayer().getLastName();
	}

	public Float getHandicap() {
		if (this.getPlayer() == null) {
			return Float.valueOf("0.0");
		}
		return this.getPlayer().getHandicap();
	}

	@Override
	public boolean equals(Object obj) {
		SeasonSummary that = (SeasonSummary) obj;
		return new EqualsBuilder().append(this.getPlayer(), that.getPlayer()) //
				.append(this.getSeason(), that.getSeason()) //
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 43).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE) //
				.appendSuper(super.toString()) //
				.append("player", this.player)//
				.append("season", this.season)//
				.append("earnings", this.earnings)//
				.append("points", this.points)//
				.append("attendance", this.attendance)//
				.toString();
	}
}

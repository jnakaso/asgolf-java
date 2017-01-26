package com.jnaka.asgolf.service;

public class SiteDefaults {
	private Integer powerRank; // year of PowerRank to show
	private Integer currentSeason;
	private Integer currentEvent;
	private Integer nextCourseId;
	private Integer lastTournamentSeason; // year of Last Tournament to show
	private Integer lastTournamentId;
	private Boolean showTwoDay = false;

	public Integer getPowerRank() {
		return this.powerRank;
	}

	public void setPowerRank(Integer powerRank) {
		this.powerRank = powerRank;
	}

	public Integer getCurrentSeason() {
		return this.currentSeason;
	}

	public void setCurrentSeason(Integer currentSeason) {
		this.currentSeason = currentSeason;
	}

	public Integer getCurrentEvent() {
		return this.currentEvent;
	}

	public void setCurrentEvent(Integer currentEvent) {
		this.currentEvent = currentEvent;
	}

	public Integer getNextCourseId() {
		return this.nextCourseId;
	}

	public void setNextCourseId(Integer nextCourseId) {
		this.nextCourseId = nextCourseId;
	}

	public Integer getLastTournamentSeason() {
		return this.lastTournamentSeason;
	}

	public void setLastTournamentSeason(Integer lastTournamentSeason) {
		this.lastTournamentSeason = lastTournamentSeason;
	}

	public Integer getLastTournamentId() {
		return this.lastTournamentId;
	}

	public void setLastTournamentId(Integer lastTournamentId) {
		this.lastTournamentId = lastTournamentId;
	}

	public Boolean getShowTwoDay() {
		return this.showTwoDay;
	}

	public void setShowTwoDay(Boolean showTwoDay) {
		this.showTwoDay = showTwoDay;
	}

}

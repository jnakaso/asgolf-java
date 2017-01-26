package com.jnaka.golf.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GolfClub {

	private List<Player> players = new ArrayList<Player>();
	private List<Season> seasons = new ArrayList<Season>();
	private List<Course> courses = new ArrayList<Course>();
	private List<Tournament> tournaments = new ArrayList<Tournament>();

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public void setSeasons(List<Season> seasons) {
		this.seasons = seasons;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public void setTournaments(List<Tournament> tournaments) {
		this.tournaments = tournaments;
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(this.players);
	}

	public List<Season> getSeasons() {
		return Collections.unmodifiableList(this.seasons);
	}

	public List<Course> getCourses() {
		return Collections.unmodifiableList(this.courses);
	}

	public List<Tournament> getTournaments() {
		return Collections.unmodifiableList(this.tournaments);
	}

	public void addSeason(Season dSeason) {
		this.seasons.add(dSeason);
	}

	public boolean removeSeason(Season dSeason) {
		return this.seasons.remove(dSeason);
	}

	public void addPlayer(Player dPlayer) {
		this.players.add(dPlayer);
	}

	public boolean removePlayer(Player dPlayer) {
		// TODO should not allow removing players, messes with rounds
		return this.players.remove(dPlayer);
	}

	public void addCourse(Course dCourse) {
		this.courses.add(dCourse);
	}

	public boolean removeCourse(Course course) {
		return this.courses.remove(course);
	}

	public void addTournament(Tournament dTour) {
		this.tournaments.add(dTour);
	}

	public boolean removeTournament(Tournament dTour) {
		return this.tournaments.remove(dTour);
	}
}

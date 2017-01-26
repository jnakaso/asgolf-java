package com.jnaka.golf.domain.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "GolfClub")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataStore {

	@XmlType
	@XmlAccessorType(XmlAccessType.FIELD)
	static class SeasonsList {

		@XmlElement(name = "Season")
		private List<Season> seasons = new ArrayList<Season>();

		public List<Season> getSeasons() {
			return this.seasons;
		}

		public void setSeasons(List<Season> seasons) {
			this.seasons = seasons;
		}

	}

	@XmlType
	@XmlAccessorType(XmlAccessType.FIELD)
	static class PlayersList {

		@XmlElement(name = "Player")
		private List<Player> players = new ArrayList<Player>();

		public List<Player> getPlayers() {
			return players;
		}

		public void setPlayers(List<Player> players) {
			this.players = players;
		}
	}

	@XmlType
	@XmlAccessorType(XmlAccessType.FIELD)
	static class CoursesList {

		@XmlElement(name = "Course")
		private List<Course> courses = new ArrayList<Course>();

		public List<Course> getCourses() {
			return this.courses;
		}

		public void setCourse(List<Course> courses) {
			this.courses = courses;
		}
	}

	@XmlElement(name = "Seasons")
	private SeasonsList seasonsList = new SeasonsList();
	@XmlElement(name = "PlayerHistory")
	private PlayersList playersList = new PlayersList();
	@XmlElement(name = "Courses")
	private CoursesList coursesList = new CoursesList();
	@XmlElement(name = "Tournament")
	private List<Tournament> tournaments = new ArrayList<Tournament>();

	public List<Player> getPlayers() {
		return this.getPlayersList().getPlayers();
	}

	public PlayersList getPlayersList() {
		return playersList;
	}

	public List<Season> getSeasons() {
		return this.getSeasonsList().getSeasons();
	}

	public SeasonsList getSeasonsList() {
		return this.seasonsList;
	}

	public List<Course> getCourses() {
		return this.getCoursesList().getCourses();
	}

	public CoursesList getCoursesList() {
		return this.coursesList;
	}

	public List<Tournament> getTournaments() {
		return tournaments;
	}

}

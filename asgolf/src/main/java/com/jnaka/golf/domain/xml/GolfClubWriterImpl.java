package com.jnaka.golf.domain.xml;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.BeanUtils;

import com.jnaka.golf.domain.GolfClub;

public class GolfClubWriterImpl {

	// private final Logger logger = LoggerFactory.getLogger(this.getClass());
	//
	// public Logger getLogger() {
	// return logger;
	// }

	private DataStore store;
	private Map<Number, Season> seasons = new HashMap<Number, Season>();
	private Map<Number, Course> courses = new HashMap<Number, Course>();
	private Map<Number, Player> players = new HashMap<Number, Player>();

	public void write(GolfClub club, Writer writer) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(DataStore.class);
		Marshaller marshaller = jc.createMarshaller();
		DataStore xStore = this.map(club);
		marshaller.marshal(xStore, writer);
	}

	private DataStore map(GolfClub club) {
		this.setStore(new DataStore());
		this.mapSeasons(club, this.getStore());
		this.mapCourses(club, this.getStore());
		this.mapPlayers(club, this.getStore());
		this.mapTournaments(club, this.getStore());
		return this.getStore();
	}

	private void mapSeasons(GolfClub club, DataStore store) {
		for (com.jnaka.golf.domain.Season dSeason : club.getSeasons()) {
			Season xSeason = new Season();
			BeanUtils.copyProperties(dSeason, xSeason);
			store.getSeasons().add(xSeason);
			this.seasons.put(xSeason.getId(), xSeason);
		}
	}

	private void mapCourses(GolfClub aClub, DataStore store) {
		for (com.jnaka.golf.domain.Course dCourse : aClub.getCourses()) {
			Course xCourse = new Course();
			BeanUtils.copyProperties(dCourse, xCourse, new String[] { "tees" });
			this.mapCourseTees(dCourse, xCourse);
			store.getCourses().add(xCourse);
			this.courses.put(xCourse.getId(), xCourse);
		}
	}

	private void mapCourseTees(com.jnaka.golf.domain.Course dCourse, Course xCourse) {
		for (com.jnaka.golf.domain.CourseTee dTee : dCourse.getTees()) {
			CourseTee xTee = new CourseTee();
			BeanUtils.copyProperties(dTee, xTee);
			xCourse.getTees().add(xTee);
		}
	}

	private void mapPlayers(GolfClub club, DataStore store) {
		for (com.jnaka.golf.domain.Player dPlayer : club.getPlayers()) {
			Player xPlayer = new Player();
			BeanUtils.copyProperties(dPlayer, xPlayer, new String[] { "seasonSummaries", "active" });
			xPlayer.setActive(dPlayer.getActive());
			this.mapSeasonSummaries(dPlayer, xPlayer);
			store.getPlayers().add(xPlayer);
			this.players.put(xPlayer.getId(), xPlayer);
		}
	}

	private void mapSeasonSummaries(com.jnaka.golf.domain.Player dPlayer, Player xPlayer) {
		for (com.jnaka.golf.domain.SeasonSummary dSummary : dPlayer.getSeasonSummaries()) {
			SeasonSummary xSummary = new SeasonSummary();
			BeanUtils.copyProperties(dSummary, xSummary, new String[] { "season" });
			xSummary.setSeason(this.lookupSeason(dSummary.getSeason()));
			xPlayer.getSeasonSummaries().add(xSummary);
		}
	}

	private void mapTournaments(GolfClub club, DataStore store) {
		for (com.jnaka.golf.domain.Tournament dTour : club.getTournaments()) {
			Tournament xTour = new Tournament();
			BeanUtils.copyProperties(dTour, xTour, new String[] { "season", "course", "winners", "kps", "rounds" });
			xTour.setSeason(this.lookupSeason(dTour.getSeason()));
			xTour.setCourse(this.lookupCourse(dTour));
			this.mapRounds(dTour, xTour);
			this.mapKps(dTour, xTour);
			this.mapWinners(dTour, xTour);
			store.getTournaments().add(xTour);
		}
	}

	private void mapRounds(com.jnaka.golf.domain.Tournament dTour, Tournament xTour) {
		for (com.jnaka.golf.domain.Round dRound : dTour.getRounds()) {
			Round xRound = new Round();
			BeanUtils.copyProperties(dRound, xRound, new String[] { "player" });
			xRound.setPlayer(this.lookupPlayer(dRound.getPlayer()));
			xTour.getRounds().add(xRound);
		}
	}

	private void mapKps(com.jnaka.golf.domain.Tournament dTour, Tournament xTour) {
		for (com.jnaka.golf.domain.Kp dKp : dTour.getKps()) {
			Kp xKp = new Kp();
			BeanUtils.copyProperties(dKp, xKp, new String[] { "player" });
			xKp.setPlayer(this.lookupPlayer(dKp.getPlayer()));
			xTour.getKps().add(xKp);
		}
	}

	private void mapWinners(com.jnaka.golf.domain.Tournament dTour, Tournament xTour) {
		for (com.jnaka.golf.domain.Winner dWinner : dTour.getWinners()) {
			Winner xWinner = new Winner();
			BeanUtils.copyProperties(dWinner, xWinner, new String[] { "player" });
			com.jnaka.golf.domain.Round dRound = dWinner.getRound();
			if (dRound == null) {
				System.out.println(dWinner);
			} else {
				xWinner.setNet(dRound.getTotalNet().intValue());
			}
			com.jnaka.golf.domain.Player dPlayer = dRound.getPlayer();
			xWinner.setPlayer(this.lookupPlayer(dPlayer));
			xTour.getWinners().add(xWinner);
		}
	}

	private Player lookupPlayer(com.jnaka.golf.domain.Player dPlayer) {
		return this.players.get(dPlayer.getId());
	}

	private Course lookupCourse(com.jnaka.golf.domain.Tournament dTour) {
		return this.courses.get(dTour.getCourse().getId());
	}

	private Season lookupSeason(com.jnaka.golf.domain.Season dSeason) {
		return this.seasons.get(dSeason.getId());
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

}

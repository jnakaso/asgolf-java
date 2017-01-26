package com.jnaka.golf.domain.xml;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.BeanUtils;

import com.jnaka.golf.domain.GolfClub;

public class GolfClubReaderImpl implements GolfClubReader {

	private GolfClub club;
	private Map<Number, com.jnaka.golf.domain.Season> seasons = new HashMap<Number, com.jnaka.golf.domain.Season>();
	private Map<Number, com.jnaka.golf.domain.Course> courses = new HashMap<Number, com.jnaka.golf.domain.Course>();
	private Map<Number, com.jnaka.golf.domain.Player> players = new HashMap<Number, com.jnaka.golf.domain.Player>();

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnaka.golf.domain.xml.GolfClubReader#read(java.io.Reader)
	 */
	public GolfClub read(Reader reader) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(DataStore.class);
		Unmarshaller u = jc.createUnmarshaller();
		u.setListener(new GolfIDResolver());
		DataStore store = (DataStore) u.unmarshal(reader);
		return this.map(store);
	}

	private GolfClub map(DataStore store) {
		this.setClub(new GolfClub());
		this.mapSeasons(store, this.getClub());
		this.mapCourses(store, this.getClub());
		this.mapPlayers(store, this.getClub());
		this.mapTournaments(store, this.getClub());
		return this.getClub();
	}

	private void mapCourses(DataStore store, GolfClub aClub) {
		for (Course xCourse : store.getCourses()) {
			com.jnaka.golf.domain.Course dCourse = new com.jnaka.golf.domain.Course();
			BeanUtils.copyProperties(xCourse, dCourse, new String[] { "tees" });
			dCourse.setId(xCourse.getId().intValue());
			this.mapCourseTees(xCourse, dCourse);
			aClub.addCourse(dCourse);
			this.courses.put(dCourse.getId(), dCourse);
		}
	}

	private void mapCourseTees(Course xCourse, com.jnaka.golf.domain.Course dCourse) {
		for (CourseTee xTee : xCourse.getTees()) {
			if (xTee.getPars().length == 0) {
				xTee.setPars(new Integer[18]);
				for (int i = 0; i < 18; i++) {
					xTee.getPars()[i] = 0;
				}
			}
			if (xTee.getHandicaps().length == 0) {
				xTee.setHandicaps(new Integer[18]);
				for (int i = 0; i < 18; i++) {
					xTee.getHandicaps()[i] = 0;
				}
			}
			com.jnaka.golf.domain.CourseTee dTee = new com.jnaka.golf.domain.CourseTee();
			BeanUtils.copyProperties(xTee, dTee);
			dCourse.addTee(dTee);
		}
	}

	private void mapSeasons(DataStore store, GolfClub club) {
		for (Season xSeason : store.getSeasons()) {
			com.jnaka.golf.domain.Season dSeason = new com.jnaka.golf.domain.Season();
			BeanUtils.copyProperties(xSeason, dSeason);
			club.addSeason(dSeason);
			this.seasons.put(dSeason.getId(), dSeason);
		}
	}

	private void mapPlayers(DataStore store, GolfClub club) {
		for (Player xPlayer : store.getPlayers()) {
			com.jnaka.golf.domain.Player dPlayer = new com.jnaka.golf.domain.Player();
			BeanUtils.copyProperties(xPlayer, dPlayer, new String[] { "seasonSummaries" });
			dPlayer.setId(xPlayer.getId().intValue());
			dPlayer.setActive(xPlayer.isActive());
			this.mapSeasonSummaries(xPlayer, dPlayer);
			club.addPlayer(dPlayer);
			this.players.put(dPlayer.getId(), dPlayer);
		}
	}

	private void mapSeasonSummaries(Player xPlayer, com.jnaka.golf.domain.Player dPlayer) {
		for (SeasonSummary xSummary : xPlayer.getSeasonSummaries()) {
			com.jnaka.golf.domain.SeasonSummary dSummary = new com.jnaka.golf.domain.SeasonSummary();
			BeanUtils.copyProperties(xSummary, dSummary, new String[] { "season" });
			dSummary.setSeason(this.lookupSeason(xSummary.getSeason()));
			dPlayer.addSeasonSummary(dSummary);
		}
	}

	private void mapTournaments(DataStore store, GolfClub club) {
		for (Tournament xTour : store.getTournaments()) {
			com.jnaka.golf.domain.Tournament dTour = new com.jnaka.golf.domain.Tournament();
			BeanUtils.copyProperties(xTour, dTour, new String[] { "season", "course", "winners", "kps", "rounds" });
			dTour.setId(xTour.getId().intValue());
			dTour.setSeason(this.lookupSeason(xTour.getSeason()));
			dTour.setCourse(this.lookupCourse(xTour));
			this.mapRounds(xTour, dTour);
			this.mapKps(xTour, dTour);
			this.mapWinners(xTour, dTour);
			club.addTournament(dTour);
		}
	}

	private void mapRounds(Tournament xTour, com.jnaka.golf.domain.Tournament dTour) {
		for (Round xRound : xTour.getRounds()) {
			com.jnaka.golf.domain.Round dRound = new com.jnaka.golf.domain.Round();
			BeanUtils.copyProperties(xRound, dRound, new String[] { "player" });
			dRound.setId(xRound.getId().intValue());
			dRound.setPlayer(this.lookupPlayer(xRound.getPlayerReference().getPlayer()));
			dTour.addRound(dRound);
		}
	}

	private void mapKps(Tournament xTour, com.jnaka.golf.domain.Tournament dTour) {
		for (Kp xKp : xTour.getKps()) {
			com.jnaka.golf.domain.Kp dKp = new com.jnaka.golf.domain.Kp();
			BeanUtils.copyProperties(xKp, dKp, new String[] { "player" });
			dKp.setPlayer(this.lookupPlayer(xKp.getPlayerReference().getPlayer()));
			dTour.addKp(dKp);
		}
	}

	private void mapWinners(Tournament xTour, com.jnaka.golf.domain.Tournament dTour) {
		for (Winner xWinner : xTour.getWinners()) {
			com.jnaka.golf.domain.Winner dWinner = new com.jnaka.golf.domain.Winner();
			BeanUtils.copyProperties(xWinner, dWinner, new String[] { "player" });
			dWinner.setRound(this.lookupRound(dTour, xWinner));
			dTour.addWinner(dWinner);
		}
	}

	private com.jnaka.golf.domain.Round lookupRound(com.jnaka.golf.domain.Tournament dTour, Winner xWinner) {
		for (com.jnaka.golf.domain.Round dRound : dTour.getRounds()) {
			if (dRound.getPlayer().getId().equals(xWinner.getPlayerReference().getPlayer().getId())) {
				return dRound;
			}
		}
		return null;
	}

	private com.jnaka.golf.domain.Player lookupPlayer(Player xPlayer) {
		return this.players.get(xPlayer.getId());
	}

	private com.jnaka.golf.domain.Course lookupCourse(Tournament xTour) {
		Course xCourse = xTour.getCourse();
		if (xCourse == null) {
			for (com.jnaka.golf.domain.Course test : this.courses.values()) {
				if (test.getName().equalsIgnoreCase(xTour.getCourseName())) {
					return test;
				}
			}
		}
		return this.courses.get(xCourse.getId());
	}

	private com.jnaka.golf.domain.Season lookupSeason(Season xSeason) {
		return this.seasons.get(xSeason.getId());
	}

	private GolfClub getClub() {
		return club;
	}

	private void setClub(GolfClub club) {
		this.club = club;
	}
}

package com.jnaka.golf.domain.xml;

import java.io.FileWriter;
import java.util.Date;

import org.junit.Test;

import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.GolfClub;
import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;

public class GolfClubWriterImplTest {

	@Test
	public void testWrite() throws Exception {
		FileWriter writer = new FileWriter("data/asgolf_out.xml");
		GolfClubWriterImpl clubWriter = new GolfClubWriterImpl();

		GolfClub club = new GolfClub();

		Season dSeason = new Season();
		dSeason.setId(1776);
		club.addSeason(dSeason);

		SeasonSummary seasonSummary = new SeasonSummary();
		seasonSummary.setSeason(dSeason);

		Player dPlayer = new Player();
		dPlayer.setId(1);
		dPlayer.setFirstName("firstName");
		dPlayer.setLastName("lastName");
		dPlayer.addSeasonSummary(seasonSummary);

		club.addPlayer(dPlayer);

		CourseTee tee = new CourseTee();
		tee.setName("white");
		tee.setHandicaps(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18 });

		Course course = new Course();
		course.setId(1);
		course.setName("AAA");
		course.addTee(tee);

		club.addCourse(course);

		Kp kp = new Kp();
		kp.setPlayer(dPlayer);

		Round round = new Round();
		round.setId(1);
		round.setPlayer(dPlayer);
		round.setScores(new Integer[] { 5, 4, 3, 4, 5, 6, 4, 5, 6, 4, 5, 6, 4, 5, 6, 4, 5, 6 });

		Winner winner = new Winner();
		winner.setRound(round);

		Tournament dTour = new Tournament();
		dTour.setId(1);
		dTour.setDate(new Date(0));
		dTour.setCourse(course);
		dTour.setSeason(dSeason);
		dTour.addKp(kp);
		dTour.addWinner(winner);
		dTour.addRound(round);

		club.addTournament(dTour);

		clubWriter.write(club, writer);

	}
}

package com.jnaka.golf.dao;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@Ignore
public class TournamentWriterImplTest {

	@Autowired
	TournamentWriterImpl writer;

	@Test
	public void testWrite() {
		Course course = new Course();
		course.setName("pebble");

		Tournament tournament = new Tournament();
		tournament.setCourse(course);
		tournament.setRating(72.0f);
		tournament.setSlope(113f);
		tournament.setDate(new Date(100000l));
		tournament.setId(13);
		tournament.setSeasonID(2010);

		Player player = new Player();
		player.setId(5);
		player.setFirstName("first");
		player.setLastName("last");

		Round round = new Round();
		round.setFlight(Flight.A);
		round.setHandicap(16);
		round.setPlayer(player);
		round.setTotalNet(68f);
		round.setFrontNet(35f);
		round.setBackNet(33f);
		round.setTotal(85);
		round.setFront(40);
		round.setBack(35);
		Integer scores[] = { 3, 4, 5, 5, 4, 3, 3, 4, 5, 3, 4, 5, 5, 4, 3, 3, 4, 5 };
		round.setScores(scores);

		Winner winner = new Winner(round, "A1", 10f, 20f);

		tournament.addWinner(winner);

		tournament.addRound(round);

		writer.write(tournament, "src/test/data/temp.xml");
	}
}

package com.jnaka.golf.test;

import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.domain.Season.ScoringPolicy;
import com.jnaka.golf.domain.Tournament.Type;

public class ObjectMother {

	public static Tournament createNormal(Integer id) {
		Tournament tournament = new Tournament();
		tournament.setId(id);
		tournament.setType(Type.NORMAL);
		tournament.setCourse(createCourse(1));
		tournament.setRating(70.0f);
		tournament.setSlope(113f);
		return tournament;
	}

	public static Round createRound(Integer id, Player player, Flight flight, Integer handicap, Integer[] scores) {
		Round round = new Round();
		round.setId(id);
		round.setPlayer(player);
		round.setFlight(flight);
		round.setScores(scores);
		round.setHandicap(handicap);
		int total = 0;
		for (int hole : scores) {
			total += hole;
		}
		round.setTotal(total);
		round.setTotalNet(Float.valueOf(total - handicap));
		return round;
	}

	public static Player createPlayer(Integer id) {
		Player player = new Player();
		player.setId(id);
		return player;
	}

	public static Course createCourse(Integer id) {
		Course course = new Course();
		course.setName("CourseA");
		course.setId(id);
		return course;
	}

	public static Season createSeason() {
		Season season = new Season();
		season.setId(2010);
		season.setScoringPolicy(ScoringPolicy.DEFAULT_20);
		return season;
	}

	public static Season createSeason(Integer id) {
		Season season = new Season();
		season.setId(id);
		season.setScoringPolicy(ScoringPolicy.DEFAULT_20);
		return season;
	}

	public static Kp createKp(Player player, Flight flight, String hole) {
		Kp kp = new Kp();
		kp.setPlayer(player);
		kp.setFlight(flight);
		kp.setHole(hole);
		return kp;
	}

	public static Winner createWinner(Round round, float earnings, float points, String finish) {
		Winner winner = new Winner();
		winner.setRound(round);
		winner.setEarnings(earnings);
		winner.setFinish(finish);
		winner.setPoints(points);
		return winner;
	}

	private ObjectMother() {

	}

}

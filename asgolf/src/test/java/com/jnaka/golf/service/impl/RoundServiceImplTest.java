package com.jnaka.golf.service.impl;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.RoundService;

public class RoundServiceImplTest {

	@Test
	public void testGetHandicapIndex() {
		RoundServiceImpl service = this.createService();
		Round round = new Round();
		round.setAdjusted(86);
		Tournament tournament = new Tournament();
		tournament.setRating(71.5f);
		tournament.setSlope(113f);

		round.setTournament(tournament);
		Assert.assertEquals(14.5f, service.getHandicapIndex(round), 0.001f);
	}

	@Test
	public void testUpdateTotals() {
		RoundServiceImpl service = this.createService();

		Round round1 = new Round();
		round1.setHandicap(18);
		Integer[] scores = { 3, 4, 5, 3, 4, 5, 3, 4, 5, 4, 5, 6, 4, 5, 6, 4, 5, 6 };
		round1.setScores(scores);

		service.updateTotals(round1);
		Assert.assertEquals(36, round1.getFront().intValue());
		Assert.assertEquals(45, round1.getBack().intValue());
		Assert.assertEquals(81, round1.getTotal().intValue());
		Assert.assertEquals(27, round1.getFrontNet().floatValue(), 0f);
		Assert.assertEquals(36, round1.getBackNet().floatValue(), 0f);
		Assert.assertEquals(63, round1.getTotalNet().floatValue(), 0f);
		Assert.assertEquals(81, round1.getAdjusted().intValue());

		Round round2 = new Round();
		round2.setHandicap(15);
		Integer[] scores2 = { 3, 4, 5, 3, 4, 5, 3, 4, 5, 4, 5, 6, 4, 5, 6, 4, 5, 6 };
		round2.setScores(scores2);

		service.updateTotals(round2);
		Assert.assertEquals(36, round2.getFront().intValue());
		Assert.assertEquals(45, round2.getBack().intValue());
		Assert.assertEquals(81, round2.getTotal().intValue());
		Assert.assertEquals(28.5, round2.getFrontNet().floatValue(), 0f);
		Assert.assertEquals(37.5, round2.getBackNet().floatValue(), 0f);
		Assert.assertEquals(66, round2.getTotalNet().floatValue(), 0f);
		Assert.assertEquals(81, round2.getAdjusted().intValue());

		Round round3 = new Round();
		round3.setHandicap(20);
		Integer[] scores3 = { 3, 4, 5, 3, 4, 5, 3, 4, 5, 4, 5, 6, 4, 5, 6, 4, 5, 9 };
		round3.setScores(scores3);

		service.updateTotals(round3);
		Assert.assertEquals(36, round3.getFront().intValue());
		Assert.assertEquals(48, round3.getBack().intValue());
		Assert.assertEquals(84, round3.getTotal().intValue());
		Assert.assertEquals(26, round3.getFrontNet().floatValue(), 0f);
		Assert.assertEquals(38, round3.getBackNet().floatValue(), 0f);
		Assert.assertEquals(64, round3.getTotalNet().floatValue(), 0f);
		Assert.assertEquals(83, round3.getAdjusted().intValue());
	}

	@Test
	public void testFindHighest() {
		RoundServiceImpl service = this.createService();

		Float highest1 = service.highestScore(new ArrayList<Round>(), RoundService.Filter.TOTAL_NET);
		Assert.assertNull(highest1);

		Round round1 = new Round();
		round1.setTotalNet(Float.valueOf("88"));
		Round round2 = new Round();
		round2.setTotalNet(Float.valueOf("89"));
		Float highest2 = service.highestScore(Arrays.asList(round1, round2), RoundService.Filter.TOTAL_NET);
		Assert.assertNotNull(highest2);
		Assert.assertEquals(89f, highest2.floatValue(), 0.01f);

	}

	@Test
	public void testFindLowest() {
		RoundServiceImpl service = this.createService();

		Round round1 = new Round();
		round1.setTotalNet(Float.valueOf("88"));
		Round round2 = new Round();
		round2.setTotalNet(Float.valueOf("89"));

		Float lowest2 = service.lowestScore(Arrays.asList(round1, round2), RoundService.Filter.TOTAL_NET);
		Assert.assertNotNull(lowest2);
		Assert.assertEquals(88f, lowest2.floatValue(), 0.01f);
	}

	@Test
	public void testGetPlusMinus() {
		RoundServiceImpl service = this.createService();

		Tournament tournament = this.createTournament();

		Round round = new Round();
		round.setTournament(tournament);
		Integer[] scores = { 2, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 4, 5, 6, 4, 5, 6 };
		round.setScores(scores);

		Integer[] plusMinus = service.getPlusMinus(round);
		Integer[] expected = { -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1 };
		Assert.assertArrayEquals(expected, plusMinus);
	}

	@Test
	public void testGetPlusMinusAdjusted() {
		RoundServiceImpl service = this.createService();

		Tournament tournament = this.createTournament();

		Round round = new Round();
		round.setTournament(tournament);
		round.setHandicap(2);
		Integer[] scores = { 2, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 4, 5, 6, 4, 5, 6 };
		round.setScores(scores);
		Integer[] expected = { -2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1 };

		Assert.assertArrayEquals(expected, service.getPlusMinusAdjusted(round));

		Round round2 = new Round();
		round2.setTournament(tournament);
		round2.setHandicap(20);
		Integer[] scores2 = { 2, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 4, 5, 6, 4, 5, 6 };
		round2.setScores(scores2);
		Integer[] expected2 = { -3, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0 };

		Assert.assertArrayEquals(expected2, service.getPlusMinusAdjusted(round2));

		Round round3 = new Round();
		round3.setTournament(tournament);
		round3.setHandicap(11);
		Integer[] scores3 = { 2, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 4, 5, 6, 4, 5, 6 };
		round3.setScores(scores3);
		Integer[] expected3 = { -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, 1, 1, 1 };

		Assert.assertArrayEquals(expected3, service.getPlusMinusAdjusted(round3));
	}

	private Tournament createTournament() {
		Integer[] handicaps = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18 };
		Integer[] pars = { 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 };

		CourseTee tee = new CourseTee();
		tee.setHandicaps(handicaps);
		tee.setPars(pars);

		Course course = new Course();
		course.addTee(tee);

		Tournament tournament = new Tournament();
		tournament.setCourse(course);
		return tournament;
	}

	private RoundServiceImpl createService() {
		return new RoundServiceImpl();
	}

}

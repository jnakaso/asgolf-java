package com.jnaka.golf.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.RoundService;
import com.jnaka.golf.test.ObjectMother;

public class FiveOfTenHandicapCalculatorTest {

	Mockery mockery = new Mockery();

	@Test
	public void testUpdateHandicap() {
		final FiveOfTenHandicapCalculator calc = this.createCalculator();

		final Player player = ObjectMother.createPlayer(1);

		Integer[] scores = new Integer[] { 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 };

		final List<Round> rounds = new ArrayList<Round>();
		for (int i = 100; i < 105; i++) {
			Round round = ObjectMother.createRound(i, player, Flight.A, 10, scores);
			round.setTournament(ObjectMother.createNormal(i));
			rounds.add(round);
		}

		scores = new Integer[] { 4, 5, 6, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 };
		for (int i = 105; i < 110; i++) {
			Round round = ObjectMother.createRound(i, player, Flight.A, 11, scores);
			round.setTournament(ObjectMother.createNormal(i));
			rounds.add(round);
		}

		scores = new Integer[] { 4, 5, 6, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 4, 5, 6 };
		for (int i = 110; i < 115; i++) {
			Round round = ObjectMother.createRound(i, player, Flight.A, 9, scores);
			round.setTournament(ObjectMother.createNormal(i));
			rounds.add(round);
		}

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(calc.getPlayerService()).findRounds(player);
				this.will(returnValue(rounds));
				this.atLeast(1).of(calc.getRoundService()).getHandicapIndex(with(any(Round.class)));
				this.will(returnValue(1.0f));
			}
		};
		this.mockery.checking(expectations);
		calc.updateHandicap(player);

		Assert.assertEquals(0.96f, player.getHandicap(), 0.01f);
	}

	@Test
	public void testFindAcceptedRounds() {
		final FiveOfTenHandicapCalculator calc = this.createCalculator();

		List<Round> rounds = new ArrayList<Round>();
		final Round r1 = this.createRound(this.createTournament(new Date(100), 72f, 113f), 84);
		final Round r2 = this.createRound(this.createTournament(new Date(200), 71f, 113f), 85);
		final Round r3 = this.createRound(this.createTournament(new Date(300), 70f, 113f), 86);

		rounds.add(r1);
		rounds.add(r2);
		rounds.add(r3);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(calc.getRoundService()).getHandicapIndex(r1);
				this.will(returnValue(20f));
				this.one(calc.getRoundService()).getHandicapIndex(r1);
				this.will(returnValue(19f));
				this.one(calc.getRoundService()).getHandicapIndex(r1);
				this.will(returnValue(18f));
			}
		};
		this.mockery.checking(expectations);
		List<Round> accepted = calc.findAcceptedRounds(rounds);

		Assert.assertEquals(true, accepted.contains(r2));
		Assert.assertEquals(true, accepted.contains(r3));
	}

	@Test
	public void testAverage() {
		final FiveOfTenHandicapCalculator calc = this.createCalculator();

		Round r1 = new Round();
		Round r2 = new Round();

		final List<Round> recentRounds = Arrays.asList(r1, r2);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(calc.getRoundService()).getHandicapIndex(recentRounds.get(0));
				this.will(returnValue(16f));
				this.one(calc.getRoundService()).getHandicapIndex(recentRounds.get(1));
				this.will(returnValue(17f));
			}
		};
		this.mockery.checking(expectations);
		Assert.assertEquals(16.5f, calc.averageIndex(recentRounds), 0.001f);
	}

	@Test
	public void testGetMaxBestRounds() {
		FiveOfTenHandicapCalculator calc = this.createCalculator();
		List<Round> recentRounds = new ArrayList<Round>();
		try {
			calc.getMaxBestRounds(recentRounds);
			Assert.fail("Shold throw.");
		} catch (Exception e) {
			Assert.assertTrue(e instanceof IllegalStateException);
		}
		recentRounds.add(new Round());
		recentRounds.add(new Round());
		recentRounds.add(new Round());
		Assert.assertEquals(2, calc.getMaxBestRounds(recentRounds));
		recentRounds.add(new Round());
		Assert.assertEquals(2, calc.getMaxBestRounds(recentRounds));
		recentRounds.add(new Round());
		Assert.assertEquals(3, calc.getMaxBestRounds(recentRounds));
		recentRounds.add(new Round());
		Assert.assertEquals(3, calc.getMaxBestRounds(recentRounds));
		recentRounds.add(new Round());
		Assert.assertEquals(4, calc.getMaxBestRounds(recentRounds));
		recentRounds.add(new Round());
		Assert.assertEquals(4, calc.getMaxBestRounds(recentRounds));
		recentRounds.add(new Round());
		Assert.assertEquals(5, calc.getMaxBestRounds(recentRounds));
		recentRounds.add(new Round());
		Assert.assertEquals(5, calc.getMaxBestRounds(recentRounds));

	}

	private Round createRound(final Tournament tournament, final int adjusted) {
		Round round = new Round();
		round.setTournament(tournament);
		round.setAdjusted(adjusted);
		return round;
	}

	private Tournament createTournament(final Date date, final Float rating, final Float slope) {
		Tournament tourn = new Tournament();
		tourn.setDate(date);
		tourn.setRating(rating);
		tourn.setSlope(slope);
		return tourn;
	}

	private FiveOfTenHandicapCalculator createCalculator() {
		FiveOfTenHandicapCalculator calc = new FiveOfTenHandicapCalculator();
		calc.setPlayerService(this.mockery.mock(PlayerService.class));
		calc.setRoundService(this.mockery.mock(RoundService.class));
		return calc;
	}
}

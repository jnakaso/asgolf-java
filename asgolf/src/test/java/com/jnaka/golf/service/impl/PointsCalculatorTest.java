package com.jnaka.golf.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.Test;

import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.test.ObjectMother;

public class PointsCalculatorTest {

	private Mockery mockery = new Mockery();

	static class SummaryMatcher extends BaseMatcher<Collection<SeasonSummary>> {

		final Collection<SeasonSummary> expected;

		public SummaryMatcher(Collection<SeasonSummary> expected) {
			super();
			this.expected = expected;
		}

		@Override
		public boolean matches(Object item) {
			boolean match = true;
			@SuppressWarnings("unchecked")
			Collection<SeasonSummary> actual = (Collection<SeasonSummary>) item;
			match = match && CollectionUtils.isEqualCollection(actual, expected);
			return match;
		}

		@Override
		public void describeTo(Description description) {
			// TODO Auto-generated method stub

		}

	}

	@Test
	public void testUpdate_full() {
		final Season season = ObjectMother.createSeason(2010);
		final PointsCalculatorImpl calc = this.createCalculator(season);
		Tournament tour1 = ObjectMother.createNormal(1);
		Player player1 = ObjectMother.createPlayer(1);
		tour1.addKp(ObjectMother.createKp(player1, Flight.A, "03"));
		Round round1 = ObjectMother.createRound(1, player1, Flight.A, 10, new Integer[] { 3, 4, 5, 3, 4, 5, 3, 4, 5, 3,
				4, 5, 3, 4, 5, 3, 4, 5 });
		Winner winner = ObjectMother.createWinner(round1, 10f, 3f, "A3");
		tour1.addWinner(winner);
		final List<Tournament> tournaments = Arrays.asList(tour1);

		SeasonSummary expectedSummaryKp = new SeasonSummary();
		expectedSummaryKp.setSeason(season);
		expectedSummaryKp.setPlayer(player1);
		expectedSummaryKp.setAttendance(0);
		expectedSummaryKp.setEarnings(3.0f);
		expectedSummaryKp.setPoints(0f);
		expectedSummaryKp.setKps(1);

		SeasonSummary expectedWinner = new SeasonSummary();
		expectedWinner.setSeason(season);
		expectedWinner.setPlayer(player1);
		expectedWinner.setAttendance(0);
		expectedWinner.setEarnings(10.0f);
		expectedWinner.setPoints(3f);
		expectedWinner.setKps(0);

		Collection<SeasonSummary> expected = Arrays.asList(expectedSummaryKp, expectedWinner);
		final Matcher<Collection<SeasonSummary>> summaryMatcher = new SummaryMatcher(expected);

		final SeasonSummary player1Summary = new SeasonSummary();
		player1Summary.setSeason(season);
		player1Summary.setPlayer(player1);
		player1Summary.setAttendance(0);
		player1Summary.setEarnings(13.0f);
		player1Summary.setPoints(3f);
		player1Summary.setKps(1);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(calc.getSeasonService()).getKpPrizeMoney(season);
				this.will(returnValue(new PrizeMoney(0, 3f, 0f)));
				this.one(calc.getSeasonService()).findTournaments(season);
				this.will(returnValue(tournaments));
				this.one(calc.getSeasonService()).getAttendancePrizeMoney(season);
				this.will(returnValue(new PrizeMoney(0, 0f, 3f)));
				this.one(calc.getSeasonService()).sum(with(summaryMatcher));
				this.will(returnValue(player1Summary));
				this.one(calc.getSeasonService()).updateSummaries(season, Arrays.asList(player1Summary));
			}
		};
		this.mockery.checking(expectations);
		calc.update();
	}

	@Test
	public void testUpdate_empty() {
		final Season season = ObjectMother.createSeason(2010);
		final PointsCalculatorImpl calc = this.createCalculator(season);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(calc.getSeasonService()).getKpPrizeMoney(season);
				this.will(returnValue(new PrizeMoney(0, 3f, 0f)));
				this.one(calc.getSeasonService()).findTournaments(season);
				this.will(returnValue(Arrays.asList()));
				this.one(calc.getSeasonService()).getAttendancePrizeMoney(season);
				this.will(returnValue(new PrizeMoney(0, 0f, 3f)));

				this.one(calc.getSeasonService()).updateSummaries(season, new ArrayList<SeasonSummary>());
			}
		};
		this.mockery.checking(expectations);
		calc.update();
	}

	private PointsCalculatorImpl createCalculator(Season season) {
		PointsCalculatorImpl calc = new PointsCalculatorImpl(season);
		calc.setSeasonService(this.mockery.mock(SeasonService.class));
		return calc;
	}

}

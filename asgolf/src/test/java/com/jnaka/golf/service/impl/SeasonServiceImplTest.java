package com.jnaka.golf.service.impl;

import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.jnaka.golf.dao.SeasonDao;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Season.HandicapPolicy;
import com.jnaka.golf.domain.Season.ScoringPolicy;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.PointsCalculator;
import com.jnaka.golf.service.PointsCalculatorFactory;
import com.jnaka.golf.service.PrizeCalculator;
import com.jnaka.golf.service.PrizeCalculatorFactory;
import com.jnaka.golf.test.ObjectMother;

public class SeasonServiceImplTest {

	Mockery mockery = new Mockery();

	@Test
	public void testUpdateSummaries() {
		final SeasonServiceImpl service = this.createService();
		Season season = ObjectMother.createSeason();
		Player player = ObjectMother.createPlayer(10);

		final SeasonSummary summary = new SeasonSummary();
		summary.setSeason(season);
		summary.setPlayer(player);

		List<SeasonSummary> newSummaries = Arrays.asList(summary);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(service.getPlayerService()).updateSummary(summary);
			}
		};
		this.mockery.checking(expectations);
		service.updateSummaries(season, newSummaries);
		Assert.assertEquals(season, summary.getSeason());
	}

	@Test
	public void testSum() {
		final SeasonServiceImpl service = this.createService();

		SeasonSummary summary1 = new SeasonSummary();
		summary1.setAttendance(1);
		summary1.setEarnings(3.33f);
		summary1.setPoints(25.0f);
		summary1.setKps(3);
		SeasonSummary summary2 = new SeasonSummary();
		summary2.setAttendance(1);
		summary2.setEarnings(3.33f);
		summary2.setPoints(3.0f);

		SeasonSummary total = service.sum(Arrays.asList(summary1, summary2));
		Assert.assertEquals(2, total.getAttendance());
		Assert.assertEquals(6.66, total.getEarnings(), 0.01f);
		Assert.assertEquals(28.0, total.getPoints(), 0.01f);
		Assert.assertEquals(3, total.getKps());
	}

	@Test
	public void testGetAttendancePrizeMoney() {
		final SeasonServiceImpl service = this.createService();
		final Season season = ObjectMother.createSeason();

		PrizeMoney kp = service.getAttendancePrizeMoney(season);
		Assert.assertEquals(0, kp.getEarnings().intValue());
		Assert.assertEquals(3, kp.getPoints().intValue());
	}

	@Test
	public void testGetKpPrizeMoney() {
		final SeasonServiceImpl service = this.createService();
		final Season season = ObjectMother.createSeason();

		PrizeMoney kp = service.getKpPrizeMoney(season);
		Assert.assertEquals(3, kp.getEarnings().intValue());
		Assert.assertEquals(0, kp.getPoints().intValue());
	}

	@Test
	public void testFindTournaments() {
		final SeasonServiceImpl service = this.createService();

		final Season season2009 = ObjectMother.createSeason(2009);

		final Tournament tour2009 = ObjectMother.createNormal(2);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(service.getSeasonDao()).findTournamentsBySeason(season2009);
				this.will(returnValue(Arrays.asList(tour2009)));
			}
		};
		this.mockery.checking(expectations);
		List<Tournament> tournaments = service.findTournaments(season2009);
		Assert.assertEquals(1, tournaments.size());
		Assert.assertEquals(2, tournaments.get(0).getId().intValue());
	}

	@Test
	public void testAddTournament() {
		final SeasonServiceImpl service = this.createService();

		final Season season = new Season(2010, ScoringPolicy.DEFAULT_20, HandicapPolicy.FIVE_OF_TEN);
		final PointsCalculator calculator = this.mockery.mock(PointsCalculator.class);
		final PrizeCalculator prizeCalculator = this.mockery.mock(PrizeCalculator.class);
		final Tournament tournament = ObjectMother.createNormal(2010);

		Winner winner = new Winner();
		final List<Winner> winners = Arrays.asList(winner);
		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(service.getPointsCalculatorFactory()).getCalculator(season);
				this.will(returnValue(calculator));
				this.one(calculator).update();
				this.one(service.getPrizeCalculatorFactory()).getCalculator(tournament);
				this.will(returnValue(prizeCalculator));
				this.one(prizeCalculator).findWinners();
				this.will(returnValue(winners));
			}
		};
		this.mockery.checking(expectations);
		service.addTournament(season, tournament);
		Assert.assertEquals(season, tournament.getSeason());
	}

	@Test
	public void testFindSummaries() {
		final SeasonServiceImpl service = this.createService();

		Season season2010 = new Season(2010, null, null);
		Season season2009 = new Season(2009, null, null);
		Player player1 = new Player();
		SeasonSummary seasonSummary1 = new SeasonSummary();
		seasonSummary1.setSeason(season2010);
		SeasonSummary seasonSummary2 = new SeasonSummary();
		seasonSummary2.setSeason(season2009);
		player1.addSeasonSummary(seasonSummary1);
		player1.addSeasonSummary(seasonSummary2);

		final List<Player> players = Arrays.asList(player1);
		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(service.getPlayerService()).getAll();
				this.will(returnValue(players));
			}
		};
		this.mockery.checking(expectations);
		List<SeasonSummary> summaries = service.findSummaries(season2010);
		Assert.assertEquals(true, summaries.contains(seasonSummary1));
		Assert.assertEquals(false, summaries.contains(seasonSummary2));
	}

	@Test
	public void testGetActivePlayers() {
		final SeasonServiceImpl service = this.createService();
		Season season = new Season(2010, null, null);
		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(service.getPlayerService()).getAll(true);
			}
		};
		this.mockery.checking(expectations);
		service.getActivePlayers(season);
	}

	@Test
	public void testGetAll() {
		final SeasonServiceImpl service = this.createService();
		final Season s2008 = new Season(2008, null, null);
		final Season s2009 = new Season(2009, null, null);
		final Season s2010 = new Season(2010, null, null);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(service.getSeasonDao()).getAll();
				this.will(returnValue(Arrays.asList(s2010, s2009, s2008)));
			}
		};
		this.mockery.checking(expectations);
		List<Season> found = service.getAll();
		Assert.assertEquals(s2010, found.get(0));
		Assert.assertEquals(s2008, found.get(2));
	}

	private SeasonServiceImpl createService() {
		SeasonServiceImpl service = new SeasonServiceImpl();
		service.setPlayerService(this.mockery.mock(PlayerService.class));
		service.setSeasonDao(this.mockery.mock(SeasonDao.class));
		service.setPointsCalculatorFactory(this.mockery.mock(PointsCalculatorFactory.class));
		service.setPrizeCalculatorFactory(this.mockery.mock(PrizeCalculatorFactory.class));
		return service;
	}
}

package com.jnaka.golf.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.Ignore;
import org.junit.Test;

import com.jnaka.golf.dao.TournamentDao;
import com.jnaka.golf.dao.TournamentReader;
import com.jnaka.golf.dao.TournamentWriter;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.service.PrizeCalculator;
import com.jnaka.golf.service.PrizeCalculatorFactory;
import com.jnaka.golf.service.RoundService;

public class TournamentServiceImplTest {

	private static final String TEST_FILE = "test/output/tour_export.xml";
	private Mockery mockery = new Mockery();

	@Test
	@Ignore
	public void testUpdateTotals() {
		final TournamentServiceImpl service = this.createService();

		final Tournament tournament = new Tournament();
		tournament.setId(1);

		Player player = new Player();
		player.setId(3);

		Round round1 = new Round();
		round1.setPlayer(player);
		round1.setFlight(Flight.A);

		tournament.addRound(round1);

		final PrizeCalculator calculator = this.mockery.mock(PrizeCalculator.class);
		final List<Winner> aWinners = new ArrayList<Winner>();
		final List<Winner> bWinners = new ArrayList<Winner>();

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(service.getPrizeCalculatorFactory()).getCalculator(tournament);
				this.will(returnValue(calculator));
				this.one(calculator).findWinners();
				this.will(returnValue(aWinners));

				this.one(service.getPrizeCalculatorFactory()).getCalculator(tournament);
				this.will(returnValue(calculator));
				this.one(calculator).findWinners();
				this.will(returnValue(bWinners));
			}
		};
		this.mockery.checking(expectations);
		service.updateTotals(tournament);
		Assert.assertEquals(0, tournament.getWinners().size());
	}

	@Test
	public void testImport() {
		final TournamentServiceImpl service = this.createService();

		final Tournament tournament = new Tournament();
		final Round round = new Round();
		round.setFlight(Flight.A);
		tournament.addRound(round);
		final File file = new File(TEST_FILE);

		final PrizeCalculator calculator = this.mockery.mock(PrizeCalculator.class);
		final List<Winner> aWinners = new ArrayList<Winner>();
		final List<Winner> bWinners = new ArrayList<Winner>();
		
		Expectations expectations = new Expectations() {
			{
				this.one(service.getReader()).read(file);
				this.will(returnValue(tournament));
				this.one(service.getRoundService()).updateTotals(round);
				
				this.one(service.getPrizeCalculatorFactory()).getCalculator(tournament);
				this.will(returnValue(calculator));
				this.one(calculator).findWinners();
				this.will(returnValue(aWinners));

				this.one(service.getPrizeCalculatorFactory()).getCalculator(tournament);
				this.will(returnValue(calculator));
				this.one(calculator).findWinners();
				this.will(returnValue(bWinners));
			
			}
		};
		this.mockery.checking(expectations);
		Tournament actual = service.importTournament(file);
		Assert.assertEquals(tournament, actual);
		Assert.assertEquals(0, tournament.getWinners().size());
	}

	@Test
	public void testExport() {
		final TournamentServiceImpl service = this.createService();

		final Tournament tournament = new Tournament();
		final File file = new File(TEST_FILE);

		Expectations expectations = new Expectations() {
			{
				this.one(service.getWriter()).write(tournament, file);
			}
		};
		this.mockery.checking(expectations);
		service.export(tournament, file);
	}

	@Test
	public void testFindRoundsBySeason() {
		final TournamentServiceImpl service = this.createService();

		Player player = new Player();
		player.setId(3);

		Round round1 = new Round();
		round1.setPlayer(player);

		Tournament tournament = new Tournament();
		tournament.setSeasonID(1996);
		tournament.addRound(round1);

		final List<Tournament> tournaments = Arrays.asList(tournament);

		Expectations expectations = new Expectations() {
			{
				this.one(service.getDao()).findBySeason(1996);
				this.will(returnValue(tournaments));
			}
		};
		this.mockery.checking(expectations);
		List<Round> rounds = service.findRoundsBySeason(1996, player);
		Assert.assertEquals(false, rounds.isEmpty());
	}

	@Test
	public void testFindWinners() {
		final Tournament tournament = new Tournament();

		final TournamentServiceImpl service = this.createService();

		final PrizeCalculator calculator = this.mockery.mock(PrizeCalculator.class);

		Round aRound = new Round();
		aRound.setFlight(Flight.A);
		final Winner aWinner = new Winner();
		aWinner.setRound(aRound);

		Round bRound = new Round();
		bRound.setFlight(Flight.B);
		final Winner bWinner = new Winner();
		bWinner.setRound(bRound);

		final List<Winner> winners = Arrays.asList(aWinner, bWinner);
		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(service.getPrizeCalculatorFactory()).getCalculator(tournament);
				this.will(returnValue(calculator));
				this.one(calculator).findWinners();
				this.will(returnValue(winners));
			}
		};
		this.mockery.checking(expectations);
		List<Winner> actual = service.findWinners(tournament, Flight.A);
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(aWinner, actual.get(0));
	}

	@Test
	public void testCreateWinners_split() {
		List<Round> winnerRounds = new ArrayList<Round>();
		Round round1 = new Round();
		round1.setFlight(Flight.A);
		winnerRounds.add(round1);

		Round round2 = new Round();
		round2.setFlight(Flight.A);
		winnerRounds.add(round2);

		List<PrizeMoney> placePrizeMoney = new ArrayList<PrizeMoney>();
		placePrizeMoney.add(new PrizeMoney(1, Float.valueOf("20"), Float.valueOf("9")));
		placePrizeMoney.add(new PrizeMoney(2, Float.valueOf("15"), Float.valueOf("6")));
		placePrizeMoney.add(new PrizeMoney(3, Float.valueOf("10"), Float.valueOf("3")));

		List<Winner> winners = this.createService().createWinners(winnerRounds, placePrizeMoney);
		Assert.assertEquals("A1", winners.get(0).getFinish());
		Assert.assertEquals(2, winners.size());
		Assert.assertEquals(1, placePrizeMoney.size());
	}

	@Test
	public void testDivideWinnings_simple() {
		List<Round> winnerRounds = new ArrayList<Round>();
		Round round1 = new Round();
		round1.setFlight(Flight.A);
		winnerRounds.add(round1);
		List<PrizeMoney> placePrizeMoney = new ArrayList<PrizeMoney>();
		placePrizeMoney.add(new PrizeMoney(2, Float.valueOf("15"), Float.valueOf("6")));

		List<Winner> winners = this.createService().divideWinnings(winnerRounds, placePrizeMoney);
		Assert.assertEquals("A2", winners.get(0).getFinish());
		Assert.assertEquals(15f, winners.get(0).getEarnings().floatValue());
		Assert.assertEquals(6f, winners.get(0).getPoints().floatValue());
	}

	@Test
	public void testDivideWinnings_split() {
		List<Round> winnerRounds = new ArrayList<Round>();

		Round round1 = new Round();
		round1.setFlight(Flight.A);
		winnerRounds.add(round1);

		Round round2 = new Round();
		round2.setFlight(Flight.A);
		winnerRounds.add(round2);

		List<PrizeMoney> placePrizeMoney = new ArrayList<PrizeMoney>();
		placePrizeMoney.add(new PrizeMoney(2, Float.valueOf("15"), Float.valueOf("6")));
		placePrizeMoney.add(new PrizeMoney(3, Float.valueOf("10"), Float.valueOf("3")));

		List<Winner> winners = this.createService().divideWinnings(winnerRounds, placePrizeMoney);
		Assert.assertEquals("A2", winners.get(0).getFinish());
		Assert.assertEquals(12.5f, winners.get(0).getEarnings().floatValue());
		Assert.assertEquals(4.5f, winners.get(0).getPoints().floatValue());
	}

	private TournamentServiceImpl createService() {
		TournamentServiceImpl service = new TournamentServiceImpl();
		service.setDao(this.mockery.mock(TournamentDao.class));
		service.setWriter(this.mockery.mock(TournamentWriter.class));
		service.setReader(this.mockery.mock(TournamentReader.class));
		service.setRoundService(this.mockery.mock(RoundService.class));
		service.setPrizeCalculatorFactory(this.mockery.mock(PrizeCalculatorFactory.class));
		return service;
	}
}

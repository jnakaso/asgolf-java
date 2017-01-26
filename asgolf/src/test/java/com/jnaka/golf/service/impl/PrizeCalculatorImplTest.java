package com.jnaka.golf.service.impl;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.test.ObjectMother;

public class PrizeCalculatorImplTest {

	@Test
	public void testMultiDay() {
		Tournament tournamentOne = ObjectMother.createNormal(1);

		Player player111 = ObjectMother.createPlayer(111);
		Round round11 = ObjectMother.createRound(11, player111, Flight.A, 10, //
				new Integer[] { 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		Player player222 = ObjectMother.createPlayer(222);
		Round round22 = ObjectMother.createRound(22, player222, Flight.A, 10, //
				new Integer[] { 3, 4, 6, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		Player player333 = ObjectMother.createPlayer(333);
		Round round33 = ObjectMother.createRound(33, player333, Flight.A, 10, //
				new Integer[] { 3, 4, 6, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		Player player444 = ObjectMother.createPlayer(444);
		Round round44 = ObjectMother.createRound(33, player444, Flight.A, 10, //
				new Integer[] { 3, 4, 6, 4, 5, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		tournamentOne.addRound(round33);
		tournamentOne.addRound(round11);
		tournamentOne.addRound(round22);
		tournamentOne.addRound(round44);

		Tournament tournamentTwo = ObjectMother.createNormal(2);

		Round round111 = ObjectMother.createRound(11, player111, Flight.A, 10, //
				new Integer[] { 3, 4, 6, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		Round round222 = ObjectMother.createRound(22, player222, Flight.A, 10, //
				new Integer[] { 3, 4, 6, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		Round round333 = ObjectMother.createRound(33, player333, Flight.A, 10, //
				new Integer[] { 3, 4, 6, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		tournamentTwo.addRound(round333);
		tournamentTwo.addRound(round111);
		tournamentTwo.addRound(round222);

		List<PrizeMoney> prizes = Arrays.asList( //
				new PrizeMoney(1, Float.valueOf("100"), Float.valueOf("18")), //
				new PrizeMoney(2, Float.valueOf("75"), Float.valueOf("12")), //
				new PrizeMoney(3, Float.valueOf("50"), Float.valueOf("6")));
		PrizeCalculatorImpl calc = new PrizeCalculatorImpl(prizes, tournamentTwo, tournamentOne);
		List<Winner> winners = calc.findWinners();
		Assert.assertEquals(false, winners.isEmpty());

		Assert.assertEquals(63, winners.get(0).getRound().getTotalNet().intValue());
		Assert.assertEquals("A1", winners.get(0).getFinish());
		Assert.assertEquals(100, winners.get(0).getEarnings().floatValue(), 0f);
		Assert.assertEquals(18, winners.get(0).getPoints().floatValue(), 0f);

		Assert.assertEquals(63, winners.get(1).getRound().getTotalNet().intValue());
		Assert.assertEquals("A2", winners.get(1).getFinish());
		Assert.assertEquals(62.50, winners.get(1).getEarnings().floatValue(), 0f);
		Assert.assertEquals(9, winners.get(1).getPoints().floatValue(), 0f);

		Assert.assertEquals(63, winners.get(2).getRound().getTotalNet().intValue());
		Assert.assertEquals("A2", winners.get(2).getFinish());
		Assert.assertEquals(62.50, winners.get(2).getEarnings().floatValue(), 0f);
		Assert.assertEquals(9, winners.get(2).getPoints().floatValue(), 0f);
	}

	@Test
	public void testFindWinners() {
		Tournament tournament = ObjectMother.createNormal(1);

		Player player111 = ObjectMother.createPlayer(111);
		Round round11 = ObjectMother.createRound(11, player111, Flight.A, 10, //
				new Integer[] { 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		Player player222 = ObjectMother.createPlayer(222);
		Round round22 = ObjectMother.createRound(22, player222, Flight.A, 10, //
				new Integer[] { 3, 4, 6, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		Player player333 = ObjectMother.createPlayer(333);
		Round round33 = ObjectMother.createRound(33, player333, Flight.A, 10, //
				new Integer[] { 3, 4, 6, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		Player player444 = ObjectMother.createPlayer(444);
		Round round44 = ObjectMother.createRound(33, player444, Flight.A, 10, //
				new Integer[] { 3, 4, 6, 4, 5, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5, 3, 4, 5 });

		tournament.addRound(round33);
		tournament.addRound(round11);
		tournament.addRound(round22);
		tournament.addRound(round44);

		List<PrizeMoney> prizes = Arrays.asList( //
				new PrizeMoney(1, Float.valueOf("15"), Float.valueOf("9")), //
				new PrizeMoney(2, Float.valueOf("10"), Float.valueOf("6")), //
				new PrizeMoney(3, Float.valueOf("5"), Float.valueOf("3")));
		PrizeCalculatorImpl calc = new PrizeCalculatorImpl(prizes, tournament);
		List<Winner> winners = calc.findWinners();
		Assert.assertEquals(false, winners.isEmpty());

		Assert.assertEquals(62, winners.get(0).getRound().getTotalNet().intValue());
		Assert.assertEquals("A1", winners.get(0).getFinish());
		Assert.assertEquals(15, winners.get(0).getEarnings().floatValue(), 0f);
		Assert.assertEquals(9, winners.get(0).getPoints().floatValue(), 0f);

		Assert.assertEquals(63, winners.get(1).getRound().getTotalNet().intValue());
		Assert.assertEquals("A2", winners.get(1).getFinish());
		Assert.assertEquals(7.5, winners.get(1).getEarnings().floatValue(), 0f);
		Assert.assertEquals(4.5, winners.get(1).getPoints().floatValue(), 0f);

		Assert.assertEquals(63, winners.get(2).getRound().getTotalNet().intValue());
		Assert.assertEquals("A2", winners.get(2).getFinish());
		Assert.assertEquals(7.5, winners.get(2).getEarnings().floatValue(), 0f);
		Assert.assertEquals(4.5, winners.get(2).getPoints().floatValue(), 0f);
	}

}

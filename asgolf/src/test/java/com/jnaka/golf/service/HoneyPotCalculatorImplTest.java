package com.jnaka.golf.service;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.HoneyPotCalculator.Type;
import com.jnaka.golf.service.HoneyPotCalculator.Winner;

public class HoneyPotCalculatorImplTest {

	@Test
	public void test_ties() {
		HoneyPotCalculatorImpl calc = new HoneyPotCalculatorImpl();

		Player player1 = new Player();
		Round round1 = new Round();
		round1.setId(1);
		round1.setTotalNet(70f);
		round1.setFrontNet(35f);
		round1.setBackNet(35f);
		round1.setPlayer(player1);

		Player player2 = new Player();
		Round round2 = new Round();
		round2.setId(2);
		round2.setTotalNet(71f);
		round2.setFrontNet(35f);
		round2.setBackNet(36f);
		round2.setPlayer(player2);

		Player player3 = new Player();
		Round round3 = new Round();
		round3.setId(3);
		round3.setTotalNet(72f);
		round3.setFrontNet(37f);
		round3.setBackNet(35f);
		round3.setPlayer(player3);

		Player player4 = new Player();
		Round round4 = new Round();
		round4.setId(4);
		round4.setTotalNet(70f);
		round4.setFrontNet(35f);
		round4.setBackNet(35f);
		round4.setPlayer(player4);

		Tournament tournament = new Tournament();
		tournament.addRound(round1);
		tournament.addRound(round2);
		tournament.addRound(round3);
		tournament.addRound(round4);

		Map<Type, List<Winner>> winners = calc.findWinners(tournament);

		Assert.assertEquals(2, winners.get(Type.OVERALL).size());
		Assert.assertEquals(5.0f, winners.get(Type.OVERALL).get(0).getEarnings().floatValue(), 0.0f);
		Assert.assertEquals(70f, winners.get(Type.OVERALL).get(0).getRound().getTotalNet().floatValue(), 0.0f);

		Assert.assertEquals(round2, winners.get(Type.FRONT).get(0).getRound());
		Assert.assertEquals(5.0f, winners.get(Type.FRONT).get(0).getEarnings().floatValue(), 0.0f);
		Assert.assertEquals(35f, winners.get(Type.FRONT).get(0).getRound().getFrontNet().floatValue(), 0.0f);

		Assert.assertEquals(round3, winners.get(Type.BACK).get(0).getRound());
		Assert.assertEquals(5.0f, winners.get(Type.BACK).get(0).getEarnings().floatValue(), 0.0f);
		Assert.assertEquals(35f, winners.get(Type.BACK).get(0).getRound().getBackNet().floatValue(), 0.0f);
		Assert.assertEquals(3, winners.get(Type.BACK).get(0).getRound().getId().intValue());
	}

	@Test
	public void test() {
		HoneyPotCalculatorImpl calc = new HoneyPotCalculatorImpl();

		Player player1 = new Player();
		Round round1 = new Round();
		round1.setId(1);
		round1.setTotalNet(72f);
		round1.setFrontNet(36f);
		round1.setBackNet(36f);
		round1.setPlayer(player1);

		Player player2 = new Player();
		Round round2 = new Round();
		round2.setId(2);
		round2.setTotalNet(71f);
		round2.setFrontNet(35f);
		round2.setBackNet(36f);
		round2.setPlayer(player2);

		Player player3 = new Player();
		Round round3 = new Round();
		round3.setId(3);
		round3.setTotalNet(72f);
		round3.setFrontNet(37f);
		round3.setBackNet(35f);
		round3.setPlayer(player3);

		Player player4 = new Player();
		Round round4 = new Round();
		round4.setId(4);
		round4.setTotalNet(70f);
		round4.setFrontNet(35f);
		round4.setBackNet(35f);
		round4.setPlayer(player4);

		Tournament tournament = new Tournament();
		tournament.addRound(round1);
		tournament.addRound(round2);
		tournament.addRound(round3);
		tournament.addRound(round4);

		Map<Type, List<Winner>> winners = calc.findWinners(tournament);

		Assert.assertEquals(round4, winners.get(Type.OVERALL).get(0).getRound());
		Assert.assertEquals(10.0f, winners.get(Type.OVERALL).get(0).getEarnings().floatValue(), 0.0f);
		Assert.assertEquals(70f, winners.get(Type.OVERALL).get(0).getRound().getTotalNet().floatValue(), 0.0f);
		Assert.assertEquals(4, winners.get(Type.OVERALL).get(0).getRound().getId().intValue());

		Assert.assertEquals(round2, winners.get(Type.FRONT).get(0).getRound());
		Assert.assertEquals(5.0f, winners.get(Type.FRONT).get(0).getEarnings().floatValue(), 0.0f);
		Assert.assertEquals(35f, winners.get(Type.FRONT).get(0).getRound().getFrontNet().floatValue(), 0.0f);
		Assert.assertEquals(2, winners.get(Type.FRONT).get(0).getRound().getId().intValue());

		Assert.assertEquals(round3, winners.get(Type.BACK).get(0).getRound());
		Assert.assertEquals(5.0f, winners.get(Type.BACK).get(0).getEarnings().floatValue(), 0.0f);
		Assert.assertEquals(35f, winners.get(Type.BACK).get(0).getRound().getBackNet().floatValue(), 0.0f);
		Assert.assertEquals(3, winners.get(Type.BACK).get(0).getRound().getId().intValue());
	}
}

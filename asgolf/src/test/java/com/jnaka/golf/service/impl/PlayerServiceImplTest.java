package com.jnaka.golf.service.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.test.ObjectMother;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@Ignore
public class PlayerServiceImplTest {

	@Autowired
	PlayerServiceImpl service;

	@Test
	public void testCetCourseAdjustedHandicap() {
		Player player = ObjectMother.createPlayer(1);
		player.setHandicap(20.0f);
		CourseTee courseTee = new CourseTee();
		courseTee.setSlope(120);
		Integer adj = this.service.getCourseAdjustedHandicap(player, courseTee);
		Assert.assertEquals(21, adj.intValue());
	}

	@Test
	public void test() {
		final Player player = new Player();
		player.setId(3);

		Season season = ObjectMother.createSeason(2010);

		SeasonSummary summary = new SeasonSummary();
		summary.setPlayer(player);
		summary.setSeason(season);

		service.updateSummary(summary);
	}

	@Test
	public void testFindRounds() {
		final Player player = new Player();
		player.setId(3);
		List<Round> rounds = service.findRounds(player);
		Assert.assertEquals(135, rounds.size());

	}

	@Test
	public void testFindRoundsSeason() {
		final Player player = new Player();
		player.setId(3);
		Assert.assertEquals(4, service.findRounds(player, 2010).size());
		Assert.assertEquals(11, service.findRounds(player, 2009).size());
	}

	@Test
	public void testGetActivePlayers() {
		List<Player> activePlayers = service.getAll(true);
		Assert.assertEquals(26, activePlayers.size());
	}
}

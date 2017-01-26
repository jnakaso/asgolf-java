package com.jnaka.golf.domain.json;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.test.ObjectMother;

@Ignore
public class PlayerWriterImplTest {

	private Mockery mockery = new Mockery();

	@After
	public void teardown() {
		this.mockery.assertIsSatisfied();
	}

	@Test
	public void testWritePlayers() {
		final PlayersWriterImpl writer = this.createWriter();
		Player player = new Player();
		player.setId(1);
		player.setFirstName("first");
		player.setLastName("last");
		player.setHandicap(21f);

		final List<Player> players = Arrays.asList(player);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(writer.getPlayerService()).getAll();
				this.will(returnValue(players));
			}
		};
		this.mockery.checking(expectations);
		String actual = writer.writePlayers(players);
		Assert.assertNotNull(actual);
	}

	@Test
	public void testWritePlayer() {
		final PlayersWriterImpl writer = this.createWriter();
		final Player player = new Player();
		player.setId(1);
		player.setFirstName("first");
		player.setLastName("last");
		player.setHandicap(21f);

		Round round1 = ObjectMother.createRound(6, player, Flight.A, 18, new Integer[] { 0, 1 });
		Tournament tournament1 = new Tournament();
		Course course1 = ObjectMother.createCourse(20);
		tournament1.setCourse(course1);
		round1.setTournament(tournament1);

		Round round2 = ObjectMother.createRound(7, player, Flight.B, 19, new Integer[] { 2, 1, 3 });
		Tournament tournament2 = new Tournament();
		Course course2 = ObjectMother.createCourse(21);
		tournament2.setCourse(course2);
		round2.setTournament(tournament2);

		final List<Round> rounds = Arrays.asList(round1, round2);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(writer.getPlayerService()).get(1);
				this.will(returnValue(player));
				this.one(writer.getPlayerService()).findRounds(player);
				this.will(returnValue(rounds));
			}
		};
		this.mockery.checking(expectations);
		String actual = writer.writePlayers(Arrays.asList(player));
		Assert.assertNotNull(actual);
	}

	private PlayersWriterImpl createWriter() {
		PlayersWriterImpl writer = new PlayersWriterImpl();
		writer.setPlayerService(this.mockery.mock(PlayerService.class));
		return writer;
	}

}

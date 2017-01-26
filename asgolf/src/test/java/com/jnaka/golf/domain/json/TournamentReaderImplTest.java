package com.jnaka.golf.domain.json;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.jnaka.golf.dao.CourseDao;
import com.jnaka.golf.dao.PlayerDao;
import com.jnaka.golf.dao.SeasonDao;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.test.ObjectMother;

public class TournamentReaderImplTest {

	static String fileName = "./src/test/resources/tournamentImport.json";

	private Mockery mockery = new Mockery();

	@After
	public void teardown() {
		this.mockery.assertIsSatisfied();
	}

	@Test
	public void testRead() throws IOException, URISyntaxException {
		final TournamentReaderImpl tReader = createReader();

		final Player player = ObjectMother.createPlayer(68);
		final Course course = ObjectMother.createCourse(129);
		final Season season = ObjectMother.createSeason();
		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(tReader.getSeasonDao()).load(2012);
				this.will(returnValue(season));
				this.one(tReader.getCourseDao()).findByName("New course");
				this.will(returnValue(course));
				this.one(tReader.getPlayerDao()).load(68);
				this.will(returnValue(player));
				this.one(tReader.getPlayerDao()).load(1);
				this.will(returnValue(player));
			}
		};
		this.mockery.checking(expectations);
		System.out.print((new File(".").getAbsolutePath()));
		Tournament tournament = tReader.read(new File(fileName));
		Assert.assertNotNull(tournament);
		Assert.assertEquals(season, tournament.getSeason());
		Assert.assertEquals(course, tournament.getCourse());
		Assert.assertEquals(1331366400000l, tournament.getDate().getTime());
		Assert.assertEquals(115f, tournament.getSlope(), 0.0f);
		Assert.assertEquals(1, tournament.getRounds().size());
		Assert.assertEquals(Flight.A, tournament.getRounds().get(0).getFlight());
		Assert.assertEquals(15, tournament.getRounds().get(0).getHandicap().intValue());
		Assert.assertEquals(5, tournament.getRounds().get(0).getScores()[0].intValue());
		Assert.assertEquals(1, tournament.getKps().size());
		Assert.assertEquals(Flight.A, tournament.getKps().get(0).getFlight());
	}

	private TournamentReaderImpl createReader() {
		TournamentReaderImpl reader = new TournamentReaderImpl();
		reader.setPlayerDao(this.mockery.mock(PlayerDao.class));
		reader.setCourseDao(this.mockery.mock(CourseDao.class));
		reader.setSeasonDao(this.mockery.mock(SeasonDao.class));
		return reader;
	}
}

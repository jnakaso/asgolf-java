package com.jnaka.golf.domain.xml;

import java.io.FileReader;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.jnaka.golf.domain.GolfClub;

public class GolfClubReaderImplIT {

	@Test
	public void testMapping() throws Exception {
		FileReader reader = new FileReader("./src/test/data/asgolf.xml");
		GolfClubReader clubReader = new GolfClubReaderImpl();

		GolfClub club = clubReader.read(reader);

		Assert.assertEquals(1, club.getSeasons().size());

		Assert.assertEquals(2, club.getPlayers().size());
		Assert.assertEquals("Bruce", club.getPlayers().get(0).getFirstName());
		Assert.assertEquals(true, club.getPlayers().get(0).getActive().booleanValue());
		Assert.assertEquals(7.2f, club.getPlayers().get(0).getHandicap().floatValue(), 0.1f);
		Assert.assertEquals(2010, club.getPlayers().get(0).getSeasonSummaries().first().getSeason().getId().intValue());

		Assert.assertEquals(1, club.getCourses().size());
		Assert.assertEquals("Auburn", club.getCourses().get(0).getName());
		Assert.assertEquals("RED", club.getCourses().get(0).getTees().get(0).getName());

		// Tournaments
		Assert.assertEquals(1, club.getTournaments().size());
		Assert.assertEquals("Auburn", club.getTournaments().get(0).getCourse().getName());
		Assert.assertEquals(1, club.getTournaments().get(0).getRounds().size());
		Assert.assertEquals(19, club.getTournaments().get(0).getRounds().get(0).getHandicap().intValue());

		Assert.assertEquals(1, club.getTournaments().get(0).getKps().size());
		Assert.assertEquals("Nate", club.getTournaments().get(0).getKps().iterator().next().getPlayer().getFirstName());

		Assert.assertEquals(1, club.getTournaments().get(0).getWinners().size());

	}

	@Test
	@Ignore()
	public void testFile() throws Exception {
		GolfClubReader clubReader = new GolfClubReaderImpl();

		GolfClub club = clubReader.read(new FileReader("src/test/resources/asgolf.xml"));
		Assert.assertNotNull(club);
		Assert.assertFalse(club.getSeasons().isEmpty());
		Assert.assertEquals(15, club.getSeasons().size());
	}

}

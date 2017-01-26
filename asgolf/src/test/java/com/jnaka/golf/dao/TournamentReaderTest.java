package com.jnaka.golf.dao;

import java.io.File;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Tournament;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@Ignore
public class TournamentReaderTest extends AbstractJUnit4SpringContextTests {

	private static final String TEST_IN_FILE = "src/test/data/tournament_input.xml";

	@Autowired
	private TournamentReaderImpl reader;

	@Test
	public void testLoad() {
		Tournament t = this.reader.read(new File(TEST_IN_FILE));
		Assert.assertEquals(6, t.getRounds().size());
		Assert.assertEquals(Flight.A, t.getRounds().get(0).getFlight());
		Assert.assertEquals(4, t.getRounds().get(0).getScores()[0].intValue());
		Assert.assertEquals(7, t.getKps().size());
		Assert.assertEquals("Bruce", t.getKps().get(0).getPlayer().getFirstName());
	}
}

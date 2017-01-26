package com.jnaka.golf.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jnaka.golf.domain.Tournament;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@Ignore
public class TournamentDaoTest {

	@Autowired
	private TournamentJaxbDao dao;

	public TournamentJaxbDao getDao() {
		return dao;
	}

	@Test
	public void testLastId() {
		TournamentDao dao = this.getDao();
		int last = dao.getLastId();
		Assert.assertEquals(165, last);
	}

	@Test
	public void testGetAll() {
		TournamentDao dao = this.getDao();
		List<Tournament> tournaments = dao.getAll();
		Assert.assertEquals(163, tournaments.size());
	}

	@Test
	public void testGetCount() {
		TournamentDao dao = this.getDao();
		int count = dao.getCount();
		Assert.assertEquals(163, count);
	}

	@Test
	public void testLoad() {
		TournamentDao dao = this.getDao();
		Tournament tournament = dao.load(Integer.valueOf("160"));
		Assert.assertNotNull(tournament);
		Assert.assertEquals(17, tournament.getRounds().size());
	}

	@Test
	public void testFindBySeason() {
		TournamentDao dao = this.getDao();
		List<Tournament> tournaments = dao.findBySeason(2009);
		Assert.assertEquals(12, tournaments.size());
	}
}

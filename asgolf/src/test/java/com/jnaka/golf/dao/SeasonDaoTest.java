package com.jnaka.golf.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@Ignore
public class SeasonDaoTest {

	@Resource(name = "SeasonDao")
	private SeasonJaxbDao dao;

	@Test
	public void testFindTournamentBySeason() {
		Season season = new Season();
		season.setId(2009);
		SeasonJaxbDao dao = this.getDao();
		List<Tournament> tournaments = dao.findTournamentsBySeason(season);
		Assert.assertEquals(12, tournaments.size());
		Assert.assertEquals(160, tournaments.get(0).getId().intValue());
		}

	@Test
	public void testLastId() {
		SeasonJaxbDao dao = this.getDao();
		int last = dao.getLastId();
		Assert.assertEquals(2010, last);
	}

	@Test
	public void testGetAll() {
		SeasonJaxbDao dao = this.getDao();
		List<Season> course = dao.getAll();
		Assert.assertEquals(15, course.size());
	}

	private SeasonJaxbDao getDao() {
		return this.dao;
	}

}

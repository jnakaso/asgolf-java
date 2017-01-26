package com.jnaka.golf.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jnaka.dao.ObjectDao;
import com.jnaka.golf.domain.Player;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@Ignore
public class PlayerDaoTest {

	@Autowired
	private PlayerJaxbDao dao;

	@Test
	public void testLastId() {
		ObjectDao<Player> dao = this.getDao();
		int last = dao.getLastId();
		Assert.assertEquals(65, last);

	}

	@Test
	public void testGetAll() {
		ObjectDao<Player> dao = this.getDao();
		List<Player> players = dao.getAll();
		Assert.assertEquals(62, players.size());
	}

	@Test
	public void testGetCount() {
		ObjectDao<Player> dao = this.getDao();
		int count = dao.getCount();
		Assert.assertEquals(62, count);
	}

	@Test
	public void testLoad() {
		ObjectDao<Player> dao = this.getDao();
		Player player = dao.load(Integer.valueOf("2"));
		Assert.assertNotNull(player);
		Assert.assertEquals(3, player.getSeasonSummaries().size());
	}

	private ObjectDao<Player> getDao() {
		return this.dao;
	}

}

package com.jnaka.golf.domain.xml;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class DataStoreTest {



	@Test
	@Ignore()
	public void testBindPlayer() throws Exception {
		String string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><GolfClub>" + //
				"<Seasons>" + //
				"<Season seasonID=\"2000\" />" + //
				"</Seasons>" + //
				"<PlayerHistory>" + //
				"<Player playerID=\"1\" " + //
				"firstName=\"Bruce\" " + // 
				"lastName=\"Kaneshiro\" " + //
				"active=\"yes\" " + //
				"hdcp=\"7.18\" >" + //
				"<Season seasonID=\"2000\" points=\"43.50\" " + //
				"earnings=\"$62.00\" kps=\"9\" " + //
				"attendance=\"10\" />" + //
				"</Player>" + //
				"</PlayerHistory>" + //
				"</GolfClub>";
		JAXBContext jc = JAXBContext.newInstance(DataStore.class);
		Unmarshaller u = jc.createUnmarshaller();
		DataStore store = (DataStore) u.unmarshal(new StringReader(string));
		Assert.assertEquals(1, store.getPlayers().size());
		Assert.assertEquals(1, store.getPlayers().get(0).getId().intValue());
		Assert.assertEquals("Bruce", store.getPlayers().get(0).getFirstName());
		Assert.assertEquals("Kaneshiro", store.getPlayers().get(0).getLastName());
		Assert.assertEquals(true, store.getPlayers().get(0).isActive());
		Assert.assertEquals(7.18f, store.getPlayers().get(0).getHandicap(), 0f);
		Assert.assertEquals(2000, store.getPlayers().get(0).getSeasonSummaries().get(0).getSeason().getId().intValue());
	}

	@Test
	public void testBindSeason() throws Exception {
		String string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><GolfClub>" + //
				"<Seasons>" + //
				"<Season seasonID=\"2010\" />" + //
				"</Seasons>" + //
				"</GolfClub>";
		JAXBContext jc = JAXBContext.newInstance(DataStore.class);
		Unmarshaller u = jc.createUnmarshaller();
		DataStore store = (DataStore) u.unmarshal(new StringReader(string));
		Assert.assertEquals(1, store.getSeasons().size());
		Assert.assertEquals(2010, store.getSeasons().get(0).getId().intValue());
	}

	@Test
	public void testBindCourse() throws Exception {
		String string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><GolfClub>" + //
				"<Courses>" + //
				"<Course courseID=\"2\" name=\"Auburn\">" + //
				"<tee name=\"RED\" >" + //
				"<rating>68.3</rating>" + //
				"<slope>115.0</slope>" + //
				"<holePars>4,3,4,4,4,5,4,3,5,4,5,3,4,4,4,3,4,4</holePars>" + //
				"<holeHdcp>11,17,5,15,1,3,9,13,7,10,4,18,12,14,2,16,6,8</holeHdcp>" + //
				"</tee>" + //
				"<tee name=\"WHITE\" />" + //
				"</Course></Courses>" + //
				"</GolfClub>";
		JAXBContext jc = JAXBContext.newInstance(DataStore.class);
		Unmarshaller u = jc.createUnmarshaller();
		DataStore store = (DataStore) u.unmarshal(new StringReader(string));
		Assert.assertEquals(1, store.getCourses().size());
		Assert.assertEquals(2, store.getCourses().get(0).getId().intValue());
		Assert.assertEquals(2, store.getCourses().get(0).getTees().size());
		Assert.assertEquals("RED", store.getCourses().get(0).getTees().iterator().next().getName());
		Assert.assertEquals(68.3f, store.getCourses().get(0).getTees().iterator().next().getRating().floatValue(), 0f);
		Assert.assertEquals(115, store.getCourses().get(0).getTees().iterator().next().getSlope().intValue());
		Assert.assertArrayEquals(new Integer[] { 4, 3, 4, 4, 4, 5, 4, 3, 5, 4, 5, 3, 4, 4, 4, 3, 4, 4 }, store
				.getCourses().get(0).getTees().iterator().next().getPars());
		Assert.assertArrayEquals(new Integer[] { 11, 17, 5, 15, 1, 3, 9, 13, 7, 10, 4, 18, 12, 14, 2, 16, 6, 8 }, store
				.getCourses().get(0).getTees().iterator().next().getHandicaps());
	}
}

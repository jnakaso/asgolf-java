package com.jnaka.golf.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
@Ignore
public class CourseDaoTest {

	@Resource(name="CourseDao")
	private CourseDao dao;

	@Test
	public void testLastId() {
		CourseDao dao = this.getDao();
		int last = dao.getLastId();
		Assert.assertEquals(68, last);
	}

	@Test
	public void testGetAll() {
		CourseDao dao = this.getDao();
		List<Course> course = dao.getAll();
		Assert.assertEquals(48, course.size());
	}

	@Test
	public void testGetCount() {
		CourseDao dao = this.getDao();
		int count = dao.getCount();
		Assert.assertEquals(48, count);
	}

	@Test
	public void testLoad() {
		CourseDao dao = this.getDao();
		Course course = dao.load(Integer.valueOf("2"));
		Assert.assertNotNull(course);
		Assert.assertEquals(Integer.valueOf("2"), course.getId());
		Assert.assertEquals("Allenmore", course.getName());
		Assert.assertEquals(1, course.getTees().size());
		CourseTee tee = course.getTees().iterator().next();
		Assert.assertEquals("white", tee.getName());
		Assert.assertEquals(68.3f, tee.getRating().floatValue(), 0.01f);
		Assert.assertEquals(115, tee.getSlope().intValue());
		Integer pars[] = { 4, 3, 4, 4, 4, 5, 4, 3, 5, 4, 5, 3, 4, 4, 4, 3, 4, 4 };
		Assert.assertArrayEquals(pars, tee.getPars());
		Integer hdcps[] = { 11, 17, 5, 15, 1, 3, 9, 13, 7, 10, 4, 18, 12, 14,
				2, 16, 6, 8 };
		Assert.assertArrayEquals(hdcps, tee.getHandicaps());
	}

	@Test
	public void testCreate() {
		CourseDao dao = this.getDao();
		int start = dao.getCount();
		int last = dao.getLastId();

		Course course = new Course();
		course.setName("Pebble");

		dao.create(course);
		Assert.assertEquals(start + 1, dao.getCount());
		Assert.assertEquals(last + 1, course.getId().intValue());
	}

	@Test
	public void testUpdate() {
		CourseDao dao = this.getDao();
		Course course = dao.load(1);
		course.setName("Pebble");

		dao.update(course);

		Course found = dao.load(1);

		Assert.assertEquals("Pebble", found.getName());
	}
	
	@Test
	public void testFindByName() {
		CourseDao dao = this.getDao();
		Course found = dao.findByName("Allenmore");
		Assert.assertEquals(2, found.getId().intValue());
	}

	private CourseDao getDao() {
		return this.dao;
	}

}

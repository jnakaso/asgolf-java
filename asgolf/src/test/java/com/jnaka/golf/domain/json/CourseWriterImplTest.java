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

import com.jnaka.golf.dao.CourseDao;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;

@Ignore
public class CourseWriterImplTest {

	private Mockery mockery = new Mockery();

	@After
	public void teardown() {
		this.mockery.assertIsSatisfied();
	}

	@Test
	public void testWriteCourses() {
		final CourseWriterImpl writer = this.createWriter();
		Course course = new Course();
		course.setId(1);
		course.setName("name");
		CourseTee courseTee = new CourseTee();
		courseTee.setName("white");
		courseTee.setRating(72.0f);
		courseTee.setSlope(115);
		courseTee.setPars(new Integer[] { 3, 4, 5, 3, 4, 5, 3, 4, 5, 5, 4, 3, 5, 4, 3, 5, 4, 3 });
		courseTee.setHandicaps(new Integer[] { 3, 4, 5, 3, 4, 5, 3, 4, 5, 5, 4, 3, 5, 4, 3, 5, 4, 3 });

		course.addTee(courseTee);
		final List<Course> courses = Arrays.asList(course);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(writer.getCourseDao()).getAll();
				this.will(returnValue(courses));
			}
		};
		this.mockery.checking(expectations);
		String actual = writer.writeCourses(courses);
		Assert.assertNotNull(actual);
	}

	private CourseWriterImpl createWriter() {
		CourseWriterImpl writer = new CourseWriterImpl();
		writer.setCourseDao(this.mockery.mock(CourseDao.class));
		return writer;
	}

}

package com.jnaka.golf.domain.xml;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

public class DataMappingTest {

	@Test
	public void testCourseMap() throws IllegalAccessException, InvocationTargetException {
		Course xCourse = new Course();
		xCourse.setId(1);
		xCourse.setName("bob");
		xCourse.setDirection("Directions");
		xCourse.setPhone("PHONON");
		CourseTee testTee = new CourseTee();
		testTee.setName("Red");
		xCourse.getTees().add(testTee);

		com.jnaka.golf.domain.Course dCourse = new com.jnaka.golf.domain.Course();
		BeanUtils.copyProperties(xCourse, dCourse, new String[] { "tees" });
		for (CourseTee xTee : new ArrayList<CourseTee>(xCourse.getTees())) {
			com.jnaka.golf.domain.CourseTee dTee = new com.jnaka.golf.domain.CourseTee();
			BeanUtils.copyProperties(xTee, dTee);
			dCourse.addTee(dTee);
		}

		Assert.assertEquals(1, dCourse.getId().intValue());
		Assert.assertEquals("bob", dCourse.getName());
		Assert.assertEquals("Directions", dCourse.getDirection());
		Assert.assertEquals("PHONON", dCourse.getPhone());
		Assert.assertEquals("Red", dCourse.getTees().get(0).getName());
	}

}

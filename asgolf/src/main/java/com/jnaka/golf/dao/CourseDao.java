package com.jnaka.golf.dao;

import com.jnaka.dao.ObjectDao;
import com.jnaka.golf.domain.Course;

public interface CourseDao extends ObjectDao<Course> {

	public Course findByName(String string);

}

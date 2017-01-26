package com.jnaka.golf.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Repository;

import com.jnaka.golf.domain.Course;

@Repository("CourseDao")
public class CourseJaxbDao extends ObjectJaxbDao<Course> implements CourseDao {

	@Override
	public void create(Course course) {
		course.setId(this.getLastId() + 1);
		this.getGolfClub().addCourse(course);
	}

	@Override
	public boolean delete(Course course) {
		return this.getGolfClub().removeCourse(course);
	}

	@Override
	public List<Course> getAll() {
		return new ArrayList<Course>(this.getGolfClub().getCourses());
	}

	@Override
	public Course findByName(final String name) {
		return (Course) CollectionUtils.find(this.getAll(), new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				Course object = (Course) arg0;
				return object.getName().equals(name);
			}
		});
	}

}

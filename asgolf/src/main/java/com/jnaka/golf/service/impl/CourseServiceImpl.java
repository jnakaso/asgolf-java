package com.jnaka.golf.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.jnaka.dao.ObjectDao;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.service.CourseService;

@Service("CourseService")
public class CourseServiceImpl implements CourseService {

	@Autowired
	@Qualifier("CourseDao")
	private ObjectDao<Course> courseDao;

//	@Autowired
//	private com.jnaka.asgolf.data.CoursesDao cDao;
	
	@Override
	public Course get(Number id) {
		return this.getCourseDao().load(id);
	}

	@Override
	public List<Course> getAll() {
		Set<Course> courses = new TreeSet<Course>(new Comparator<Course>() {
			@Override
			public int compare(Course o1, Course o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		courses.addAll(this.getCourseDao().getAll());
		return new ArrayList<Course>(courses);
	}

	@Override
	public void create(Course course) {
		this.getCourseDao().create(course);
	}

	@Override
	public boolean delete(Course course) {
		return this.getCourseDao().delete(course);
	}

	@Override
	public void update(Course course) {
		this.getCourseDao().update(course);
	}

	@Override
	public Course newInstance() {
		Course course = new Course();
		course.setName(StringUtils.EMPTY);
		course.setDirection(StringUtils.EMPTY);
		course.setPhone(StringUtils.EMPTY);
		return course;
	}

	@Override
	public CourseTee newCourseTeeInstance() {
		CourseTee tee = new CourseTee();
		tee.setName("New Tee");
		tee.setRating(72f);
		tee.setSlope(113);
		for (int i = 0; i < 18; i++) {
			tee.getHandicaps()[i] = 0;
			tee.getPars()[i] = 0;
		}
		return tee;
	}

	@Override
	public void addCourseTee(Course course, CourseTee courseTee) {
		course.addTee(courseTee);
	}

	@Override
	public boolean deleteCourseTee(CourseTee courseTee) {
		Assert.notNull(courseTee, "Course tee is null");
		Course course = courseTee.getCourse();
		if (course == null) {
			return false;
		}
		return course.removeTee(courseTee);
	}

	@Override
	public Integer getCourseAdjustedHandicap(Player player, CourseTee courseTee) {
		float slope = courseTee.getSlope().floatValue();
		float handicap = player.getHandicap().floatValue();
		return new Integer(new Float(slope * handicap / 113.0).intValue());
	}

	public ObjectDao<Course> getCourseDao() {
		return courseDao;
	}

	public void setCourseDao(ObjectDao<Course> dao) {
		this.courseDao = dao;
	}
}

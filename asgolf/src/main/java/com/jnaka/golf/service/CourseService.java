package com.jnaka.golf.service;

import com.jnaka.domain.EntityObjectService;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.Player;

public interface CourseService extends EntityObjectService<Course> {

	CourseTee newCourseTeeInstance();

	public Integer getCourseAdjustedHandicap(Player player, CourseTee courseTee);
	
	boolean deleteCourseTee(CourseTee courseTee);

	void addCourseTee(Course course, CourseTee courseTee);

}

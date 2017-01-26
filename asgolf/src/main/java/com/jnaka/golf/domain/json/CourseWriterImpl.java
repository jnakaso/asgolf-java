package com.jnaka.golf.domain.json;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.dao.CourseDao;
import com.jnaka.golf.domain.Course;

@Component("coursesWriter")
public class CourseWriterImpl implements JsonWriter<List<Course>> {

	private static Transformer COURSE_TO_JS_COURSE = new Transformer() {
		@Override
		public Object transform(Object input) {
			Course course = (Course) input;
			JsonConfig config = new JsonConfig();
			config.setIgnoreTransientFields(true);
			JSONObject js = JSONObject.fromObject(course, config);
			// CourseTee whiteTee = (CourseTee)
			// CollectionUtils.find(course.getTees(), WHITE_COURSE_TEE);
			// if (whiteTee != null) {
			// js.element("rating", whiteTee.getRating());
			// js.element("slope", whiteTee.getSlope());
			// }
			return js;
		}
	};

	@Autowired
	@Qualifier("CourseDao")
	private CourseDao courseDao;

	@Override
	public boolean export(List<Course> courses, File file) {
		try {
			String data = this.writeCourses(courses);
			FileUtils.writeStringToFile(file, data);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	String writeCourses(List<Course> courses) {
		@SuppressWarnings("unchecked")
		Collection<JSONObject> jsCourses = CollectionUtils.collect(courses, COURSE_TO_JS_COURSE);
		JSONArray jsonArray = JSONArray.fromObject(jsCourses);
		return jsonArray.toString(1);
	}

	public CourseDao getCourseDao() {
		return courseDao;
	}

	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

}

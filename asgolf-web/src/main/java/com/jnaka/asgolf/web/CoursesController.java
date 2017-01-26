package com.jnaka.asgolf.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.service.CourseService;
import com.jnaka.golf.service.stats.WhereWePlayedCalculator;

@RestController
public class CoursesController {

	public interface CourseTeeMixIn {
		@JsonIgnore
		List<Integer> getCourse();
	}

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ObjectMapper mapper;

	@Autowired
	private CourseService courseService;

	@Autowired
	private WhereWePlayedCalculator calc;

	@RequestMapping("/courses")
	public JsonNode getCourses() throws Exception {
		List<Course> courses = this.getCourseService().getAll();
		JsonNode node = this.getMapper().valueToTree(courses);
		return node;
	}

	@RequestMapping("/course/{id}")
	public JsonNode getCourse(final @PathVariable Integer id) {
		Course course = this.getCourseService().get(id);
		return this.getMapper().valueToTree(course);
	}

	@RequestMapping(value = "/course-update", method = RequestMethod.POST)
	public JsonNode updateCourse(final @RequestBody Course course) {
		this.getLogger().debug("course= {}", course);
		this.getCourseService().update(course);
		return this.getMapper().valueToTree(course);
	}

	@RequestMapping(value = "/course-create", method = RequestMethod.POST)
	public JsonNode createCourse() {
		this.getLogger().debug("create course");
		Course course = this.getCourseService().newInstance();
		return this.getMapper().valueToTree(course);
	}

	@RequestMapping(value = "/courses-where", method = RequestMethod.GET)
	public List<WhereWePlayedCalculator.Entry> getWhere() {
		this.getLogger().debug("create course");
		return this.getCalc().getEntries(null);
	}

	ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			Map<Class<?>, Class<?>> mixin = new HashMap<>();
			mixin.put(CourseTee.class, CourseTeeMixIn.class);
			mapper.setMixInAnnotations(mixin);
		}
		return mapper;
	}

	void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	WhereWePlayedCalculator getCalc() {
		return calc;
	}

	void setCalc(WhereWePlayedCalculator calc) {
		this.calc = calc;
	}

	CourseService getCourseService() {
		return courseService;
	}

	void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}

	Logger getLogger() {
		return logger;
	}

}
package com.jnaka.golf.reports.xml;

import static com.jnaka.golf.reports.XmlConstants.XML_COURSE;
import static com.jnaka.golf.reports.XmlConstants.XML_COURSES;
import static com.jnaka.golf.reports.XmlConstants.XML_DIRECTIONS;
import static com.jnaka.golf.reports.XmlConstants.XML_HOLE_HDCP;
import static com.jnaka.golf.reports.XmlConstants.XML_HOLE_PAR;
import static com.jnaka.golf.reports.XmlConstants.XML_ID_COURSE;
import static com.jnaka.golf.reports.XmlConstants.XML_NAME;
import static com.jnaka.golf.reports.XmlConstants.XML_PHONE;
import static com.jnaka.golf.reports.XmlConstants.XML_RATING;
import static com.jnaka.golf.reports.XmlConstants.XML_SLOPE;
import static com.jnaka.golf.reports.XmlConstants.XML_TEE;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import com.jnaka.dao.ObjectDao;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.reports.AbstractDocumentFactory;

@Component
public class Courses extends AbstractDocumentFactory {

	private static final String XML_ROOT = XML_COURSES;
	private static final String XML_ROW_ELEMENT = XML_COURSE;
	private static final String XML_ID = XML_ID_COURSE;

	private ObjectDao<Course> dao;

	@Override
	protected void updateDocument() {
		for (Course course : this.getDao().getAll()) {
			this.process(course);
		}
	}

	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}

	public void process(Course course) {
		for (CourseTee tee : course.getTees()) {
			Integer id = course.getId().intValue();
			String name = course.getName();
			String directions = course.getDirection();
			String phone = course.getPhone();
			String tees = tee.getName();
			Float rating = tee.getRating();
			Integer slope = tee.getSlope();
			String holePars = this.join(tee.getPars());
			String holeHandicaps = this.join(tee.getHandicaps());

			Element root = this.getDocument().getRootElement();

			String xQuery = String.format("//%s[@%s='%s']", XML_ROW_ELEMENT,
					XML_NAME, name);
			Element element = (Element) root.selectSingleNode(xQuery);
			if (element == null) {
				element = DocumentHelper.createElement(XML_ROW_ELEMENT);
				root.add(element);

				element.addAttribute(XML_ID, id.toString());
				element.addAttribute(XML_NAME, name);

			} else {
				this.getLog().info("found " + name);
			}

			if (StringUtils.isNotEmpty(directions)) {
				element.addElement(XML_DIRECTIONS).setText(directions);
			}
			if (StringUtils.isNotEmpty(phone)) {
				element.addElement(XML_PHONE).setText(phone);
			}

			if (StringUtils.isNotEmpty(tees)) {
				Element teeElement = element.addElement(XML_TEE);
				teeElement.addAttribute(XML_NAME, tees);
				teeElement.addElement(XML_RATING).setText(rating.toString());
				teeElement.addElement(XML_SLOPE).setText(slope.toString());
				if (StringUtils.isNotEmpty(holePars)) {
					teeElement.addElement(XML_HOLE_PAR).setText(holePars);
				}
				if (StringUtils.isNotEmpty(holeHandicaps)) {
					teeElement.addElement(XML_HOLE_HDCP).setText(holeHandicaps);
				}
			}
		}

	}

	@Override
	public void setSeasonID(int seasonID) {
		// Don't care
	}

	public ObjectDao<Course> getDao() {
		return dao;
	}

	public void setDao(ObjectDao<Course> dao) {
		this.dao = dao;
	}
}

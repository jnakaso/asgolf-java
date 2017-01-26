package com.jnaka.golf.domain.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.jnaka.domain.xml.IdAdapter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Course {

	@XmlID
	@XmlAttribute(name = "courseID")
	@XmlJavaTypeAdapter(IdAdapter.class)
	private Number id;
	@XmlAttribute
	private String name;
	private String direction;
	private String phone;
	@XmlElement(name = "tee")
	private List<CourseTee> tees = new ArrayList<CourseTee>();

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<CourseTee> getTees() {
		return tees;
	}

	public void setTees(List<CourseTee> tees) {
		this.tees = tees;
	}

}

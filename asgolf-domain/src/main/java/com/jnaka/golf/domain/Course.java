package com.jnaka.golf.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.jnaka.domain.EntityObjectImpl;

public class Course extends EntityObjectImpl {

	private String name;
	private String direction;
	private String phone;

	private List<CourseTee> tees = new ArrayList<CourseTee>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirection() {
		return StringUtils.trimToEmpty(direction);
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public List<CourseTee> getTees() {
		return tees;
	}

	public void setTees(List<CourseTee> tees) {
		this.tees = tees;
	}

	public boolean addTee(CourseTee tee) {
		if (tee != null) {
			tee.setCourse(this);
			return this.tees.add(tee);
		}
		return false;
	}

	public boolean removeTee(CourseTee tee) {
		if (tee != null) {
			tee.setCourse(null);
			return this.tees.remove(tee);
		}
		return false;
	}

	public String getPhone() {
		return StringUtils.trimToEmpty(this.phone);
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this) //
				.appendSuper(super.toString()) //
				.append("name", this.name) //
				.toString();
	}

	@Override
	public int hashCode() {
		if (this.getId() == null) {
			return super.hashCode();
		}
		return new HashCodeBuilder(7, 13) //
				.append(this.getId()) //
				.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Course)) {
			return false;
		}
		if (this.getId() == null) {
			return super.equals(obj);
		}
		Course rhs = (Course) obj;
		return new EqualsBuilder() //
				.append(this.getId(), rhs.getId()) //
				.isEquals();
	}

}

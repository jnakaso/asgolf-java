package com.jnaka.golf.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.jnaka.domain.EntityObject;

@Entity
@Table(name = "Course")
public class Course  implements EntityObject {

	@Id
	@Column(name = "courseID")
	private Integer id;

	private String name;
	private String direction;
	private String phone;

	@OneToMany(mappedBy = "course")
	private Set<CourseTee> tees = new HashSet<CourseTee>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Set<CourseTee> getTees() {
		return tees;
	}

	public void setTees(Set<CourseTee> tees) {
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
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this) //
				.append("id", this.id.toString()) //
				.append("name", this.name.toString()) //
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(7, 13) //
				.appendSuper(super.hashCode()) //
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
		if (this.id == null) {
			return super.equals(obj);
		}
		Course rhs = (Course) obj;
		return new EqualsBuilder() //
				.append(this.id, rhs.id) //
				.isEquals();
	}

}

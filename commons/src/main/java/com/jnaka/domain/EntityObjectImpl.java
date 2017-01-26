package com.jnaka.domain;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class EntityObjectImpl implements EntityObject {

	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this) //
				.append("id", ObjectUtils.toString(this.id)) //
				.toString();
	}

}

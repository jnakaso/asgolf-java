package com.jnaka.golf.reports.json;

public interface JsonReport<T> {

	public Object create(T root);

}

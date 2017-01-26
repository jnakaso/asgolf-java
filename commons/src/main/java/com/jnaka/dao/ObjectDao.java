package com.jnaka.dao;

import java.util.List;

import com.jnaka.domain.EntityObject;

public interface ObjectDao<T extends EntityObject> {

	int getCount();

	int getLastId();

	List<T> getAll();

	// <R> List<R> getAll(Class<R> clazz);

	void create(T anObject);

	T load(final Number id);

	void update(T anObject);

	boolean delete(T anObject);

}

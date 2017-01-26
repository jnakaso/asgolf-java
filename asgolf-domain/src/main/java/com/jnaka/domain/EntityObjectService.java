package com.jnaka.domain;

import java.util.List;

public interface EntityObjectService<T> {

	public List<T> getAll();

	public T get(Number id);

	public boolean delete(T entityObject);

	public void update(T entityObject);

	public void create(T entityObject);

	public T newInstance();

}

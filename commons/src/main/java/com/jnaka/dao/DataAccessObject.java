package com.jnaka.dao;

import java.util.List;

public interface DataAccessObject<T> {

	public T load(Number id);

	public List<T> getAll();
}
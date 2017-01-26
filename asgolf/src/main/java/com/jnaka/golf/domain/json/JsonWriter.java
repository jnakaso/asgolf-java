package com.jnaka.golf.domain.json;

import java.io.File;

public interface JsonWriter<T> {

	public boolean export(T object, File file);

}
package com.jnaka.reports;

import org.dom4j.Document;

public interface DocumentFactory {

	Document create();

	String getRootName();

	void setSeasonID(int seasonID);

}

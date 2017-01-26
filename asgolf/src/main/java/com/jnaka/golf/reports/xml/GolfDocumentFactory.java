package com.jnaka.golf.reports.xml;

import org.dom4j.Document;

import com.jnaka.reports.AbstractDocumentFactory;

abstract public class GolfDocumentFactory extends AbstractDocumentFactory {

	public GolfDocumentFactory() {
		super();
	}


	/***************************************************************************
	 * UTILITY
	 **************************************************************************/

	public Document create() {
		this.setDocument(null);
		// this.getJdbcTemplate().query(this.getQueryString(), this);
		return this.getDocument();
	}

	/***************************************************************************
	 * UTILITY
	 **************************************************************************/

}

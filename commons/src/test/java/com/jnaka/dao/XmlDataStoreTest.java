package com.jnaka.dao;

import org.dom4j.Document;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class XmlDataStoreTest {

	@Test
	@Ignore("Need to get data")
	public void testGetDocument() {
		XmlDataStore dataStore = this.createDataStore();
		Document document = dataStore.getDataStore();
		Assert.assertNotNull(document);
	}

	@Test
	@Ignore("Need to get data")
	public void testSave() {
		XmlDataStore dataStore = this.createDataStore();
		dataStore.save("data/asgolf.xml.bak");
	}

	
	private XmlDataStore createDataStore() {
		XmlDataStore dataStore = new XmlDataStore();
		dataStore.setFileName("data/asgolf.xml");
		return dataStore;
	}
}

package com.jnaka.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XmlDataStore {

	private final Log log = LogFactory.getLog(this.getClass());
	private String fileName;
	private Document dataStore;

	public Document getDataStore() {
		if (this.dataStore == null) {
			this.setDataStore(this.defaultDataStore());
		}
		return dataStore;
	}

	private Document defaultDataStore() {
		try {
			InputStream ip = new FileInputStream(this.getFileName());
			SAXReader saxReader = new SAXReader();
			return saxReader.read(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setDataStore(Document dataStore) {
		this.dataStore = dataStore;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void save(String fileName) {
		this.printDocument(this.getDataStore(), fileName);
	}

	protected void printDocument(Document document, String filename) {
		try {
			FileOutputStream out = new FileOutputStream(filename);
			PrintStream ps = new PrintStream(out);
			ps.print(document.asXML());
			ps.close();
			this.getLog().info("Printed " + filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected Log getLog() {
		return log;
	}
}

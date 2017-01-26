package com.jnaka.reports;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

abstract public class AbstractReportWriter  implements BeanFactoryAware {

	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	private final Log log = LogFactory.getLog(this.getClass());
	private String fileName;
	private String rootElementName;
	private BeanFactory beanFactory;

	public void extract() {
		Document doc = this.createDocument();
		this.updateDocument(doc);
		this.printDocument(doc, this.getFileName());
	}

	abstract protected void updateDocument(Document doc);

	protected Document createDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.addElement(this.getRootElementName());
		return doc;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRootElementName() {
		return rootElementName;
	}

	public void setRootElementName(String rootElementName) {
		this.rootElementName = rootElementName;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	protected Log getLog() {
		return this.log;
	}

}

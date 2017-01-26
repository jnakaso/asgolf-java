package com.jnaka.golf.reports;

import org.dom4j.Document;

import com.jnaka.reports.AbstractReportWriter;
import com.jnaka.reports.DocumentFactory;

public class CoursesWriter extends AbstractReportWriter {

	private static final String DEFAULT_ROOT_NAME = "Courses";
	private static final String DEFAULT_FILE_NAME = "courses.xml";

	public static void main(String args[]) {
		CoursesWriter extractor = new CoursesWriter();
		extractor.extract();
	}

	public CoursesWriter() {
		super();
		this.setFileName(DEFAULT_FILE_NAME);
		this.setRootElementName(DEFAULT_ROOT_NAME);
	}

	@Override
	protected void updateDocument(Document doc) {
		this.extractCourses(doc);
	}

	private void extractCourses(Document doc) {
		DocumentFactory factory = (DocumentFactory) this.getBeanFactory().getBean("Courses");
		doc.getRootElement().add(factory.create().getRootElement().detach());
		this.getLog().info("Finished for: Courses");
	}

}

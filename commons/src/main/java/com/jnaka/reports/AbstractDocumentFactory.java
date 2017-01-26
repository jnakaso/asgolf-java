package com.jnaka.reports;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.jnaka.reports.DocumentFactory;

abstract public class AbstractDocumentFactory implements DocumentFactory {

	private static final String STRING_SEPARATOR = ",";

	public static String convertMoney(Double earn) {
		if (earn == null) {
			return StringUtils.EMPTY;
		}
		NumberFormat form = NumberFormat.getCurrencyInstance();
		return form.format(earn);
	}

	public static String convert(Float score) {
		if (score == null) {
			return StringUtils.EMPTY;
		}
		NumberFormat form = new DecimalFormat("#0.00");
		return form.format(score);
	}

	public static String convert(Double score) {
		if (score == null) {
			return StringUtils.EMPTY;
		}
		NumberFormat form = new DecimalFormat("#0.00");
		return form.format(score);
	}

	public static String convertToInteger(Double score) {
		if (score == null) {
			return StringUtils.EMPTY;
		}
		NumberFormat form = new DecimalFormat("#0");
		return form.format(score);
	}

	public static String convert(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		DateFormat form = DateFormat.getDateInstance();
		return form.format(date);
	}

	private final Log log = LogFactory.getLog(this.getClass());
	private Document document;
	private String rootName;

	/***************************************************************************
	 * UTILITY
	 **************************************************************************/

	@Override
	public Document create() {
		this.setDocument(null);
		this.updateDocument();
		return this.getDocument();
	}

	abstract protected void updateDocument();

	/***************************************************************************
	 * ACCESSORS
	 **************************************************************************/

	protected Log getLog() {
		return log;
	}

	public String getRootName() {
		if (this.rootName == null) {
			this.setRootName(this.defaultRootName());
		}
		return rootName;
	}

	void setRootName(String rootName) {
		this.rootName = rootName;
	}

	protected void setDocument(Document document) {
		this.document = document;
	}

	protected Document getDocument() {
		if (this.document == null) {
			this.setDocument(this.defaultDocument());
		}
		return document;
	}

	protected Document defaultDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.addElement(this.getRootName());
		return doc;
	}

	abstract protected String defaultRootName();

	/***************************************************************************
	 * UTILITY
	 **************************************************************************/

	protected String join(Integer[] integerArray) {
		if (!this.isAnyEmpty(integerArray)) {
			return StringUtils.join(integerArray, STRING_SEPARATOR);
		}
		return null;
	}

	protected boolean isAnyEmpty(Integer[] numberArray) {
		for (Integer number : numberArray) {
			if (number == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this)//
				.append(this.getClass().getSimpleName());
		return builder.toString();
	}
}

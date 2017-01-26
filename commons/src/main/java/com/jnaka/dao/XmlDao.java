package com.jnaka.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnaka.domain.EntityObject;

abstract public class XmlDao<T extends EntityObject> implements ObjectDao<T> {

	public static final String STRING_SEPARATOR = ",";

	/****** Utility ********/

	public static Integer parseInteger(Node xml, String xpath) {
		String value = xml.selectSingleNode(xpath).getText();
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		return Integer.valueOf(value);
	}

	public static Float parseFloat(Node xml, String xpath) {
		String value = xml.selectSingleNode(xpath).getText();
		return Float.valueOf(value);
	}

	public static Integer[] parseIntegerArray(Node xml, String xpath) {
		String value = xml.selectSingleNode(xpath).getText();
		String[] values = StringUtils.split(value, STRING_SEPARATOR);

		Integer[] scores = new Integer[values.length];
		for (int i = 0; i < values.length; i++) {
			String score = values[i].trim();
			scores[i] = Integer.parseInt(score);
		}
		return scores;
	}

	public static Date parseDate(String dateString) throws ParseException {
		return DateFormat.getDateInstance().parse(dateString);
	}

	@Autowired
	private XmlDataStore dataStore;
	private String objectName;
	private String objectId;
	private DomMapper domMapper;

	public String getObjectName() {
		return this.objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public XmlDataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(XmlDataStore dataStore) {
		this.dataStore = dataStore;
	}

	public void create(T anObject) {
		int id = this.getLastId() + 1;
		anObject.setId(id);
		this.getGroupElement().add(this.map(anObject));
	}

	public void update(T anObject) {
		Node xml = this.selectNode(anObject.getId());
		xml.detach();
		this.getGroupElement().add(this.map(anObject));
	}

	public boolean delete(T anObject) {
		Node xml = this.selectNode(anObject.getId());
		if (xml != null) {
			xml.detach();
			return true;
		}
		return false;
	}

	public List<T> getAll() {
		Element rootElement = this.getRootElement();
		@SuppressWarnings("unchecked")
		List<Node> nodes = rootElement.selectNodes(getObjectPath());
		List<T> objs = new ArrayList<T>(nodes.size());
		for (Node node : nodes) {
			objs.add(this.map(node));
		}
		return objs;
	}

	@Override
	public int getCount() {
		Element rootElement = this.getRootElement();
		List<?> selectNodes = rootElement.selectNodes(getObjectPath());
		return selectNodes.size();
	}

	public int getLastId() {
		Element rootElement = this.getRootElement();
		@SuppressWarnings("unchecked")
		List<Node> nodes = rootElement.selectNodes(getObjectPath());
		List<T> objs = new ArrayList<T>(nodes.size());
		for (Node node : nodes) {
			objs.add(this.lightMap(node));
		}

		Collections.sort(objs, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				if (o1.getId().intValue() > o2.getId().intValue()) {
					return -1;
				}
				if (o1.getId().intValue() < o2.getId().intValue()) {
					return 1;
				}
				return 0;
			}
		});

		if (objs.isEmpty()) {
			return 0;
		} else {
			return objs.get(0).getId().intValue();
		}
	}

	public T load(Number id) {
		Node xml = selectNode(id);
		if (xml != null) {
			return this.map(xml);
		} else {
			return null;
		}
	}

	abstract public T findByName(String aName);

	protected Node selectNode(Number id) {
		String xquery = String.format("%s[@%s='%s']", //
				this.getObjectPath(), //
				this.getObjectId(), //
				id.toString());
		Node xml = this.getRootElement().selectSingleNode(xquery);
		return xml;
	}

	protected Element getRootElement() {
		return this.getDataStore().getDataStore().getRootElement();
	}

	protected T lightMap(Node xml) {
		T object = this.newInstance();
		object.setId(this.parseId(xml));
		return object;
	}

	abstract protected String getObjectPath();

	abstract protected T map(Node xml);

	abstract protected T newInstance();

	abstract protected Element getGroupElement();

	protected Element map(T anObject) {
		Element elem = DocumentHelper.createElement(this.getObjectName());
		elem.addAttribute(this.getObjectId(), anObject.getId().toString());
		return elem;
	}

	protected Integer parseId(Node xml) {
		Number idAttr = xml.numberValueOf("@" + this.getObjectId());
		if (idAttr != null) {
			return idAttr.intValue();
		} else {
			return null;
		}
	}

	public DomMapper getDomMapper() {
		return this.domMapper;
	}

	public void setDomMapper(DomMapper domMapper) {
		this.domMapper = domMapper;
	}

}

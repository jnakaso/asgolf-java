package com.jnaka.domain.xml;

import java.text.DateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {

	@Override
	public String marshal(Date v) throws Exception {
		DateFormat instance = DateFormat.getDateInstance();
		return instance.format(v);
	}

	@Override
	public Date unmarshal(String v) throws Exception {
		DateFormat instance = DateFormat.getDateInstance();
		return instance.parse(v);
	}

}

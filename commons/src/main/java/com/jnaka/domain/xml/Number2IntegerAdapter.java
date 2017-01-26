package com.jnaka.domain.xml;

import java.text.NumberFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Number2IntegerAdapter extends XmlAdapter<String, Integer> {

	@Override
	public String marshal(Integer v) throws Exception {
		return v.toString();
	}

	@Override
	public Integer unmarshal(String v) throws Exception {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		return formatter.parse(v).intValue();
	}

}

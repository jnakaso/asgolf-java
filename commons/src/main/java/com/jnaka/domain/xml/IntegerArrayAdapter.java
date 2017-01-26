package com.jnaka.domain.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.StringUtils;

public class IntegerArrayAdapter extends XmlAdapter<String, Integer[]> {

	private static final char SEPARATOR = ',';

	@Override
	public String marshal(Integer[] v) throws Exception {
		return StringUtils.join(v, SEPARATOR);
	}

	@Override
	public Integer[] unmarshal(String v) throws Exception {
		String[] split = StringUtils.split(v, SEPARATOR);
		Integer[] bound = new Integer[split.length];
		for (int i = 0; i < split.length; i++) {
			bound[i] = Integer.valueOf(split[i]);
		}
		return bound;
	}

}
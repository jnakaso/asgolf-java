package com.jnaka.domain.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IdAdapter extends XmlAdapter<String, Number> {

	@Override
	public String marshal(Number v) throws Exception {
		return v.toString();
	}

	@Override
	public Number unmarshal(String v) throws Exception {
		return Integer.valueOf(v);
	}

}

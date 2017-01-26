package com.jnaka.domain.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class YesNoAdapter extends XmlAdapter<String, Boolean> {

	@Override
	public String marshal(Boolean v) throws Exception {
		if (v == null) {
			return "no";
		}
		return v ? "yes" : "no";
	}

	@Override
	public Boolean unmarshal(String v) throws Exception {
		if ("yes".equalsIgnoreCase(v))
			return true;
		return false;
	}

}

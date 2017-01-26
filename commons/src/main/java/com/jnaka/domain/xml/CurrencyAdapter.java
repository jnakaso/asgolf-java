package com.jnaka.domain.xml;

import java.text.NumberFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CurrencyAdapter extends XmlAdapter<String, Float> {

	@Override
	public String marshal(Float v) throws Exception {
		NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
		return currencyInstance.format(v);
	}

	@Override
	public Float unmarshal(String v) throws Exception {
		NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
		return currencyInstance.parse(v).floatValue();
	}

}

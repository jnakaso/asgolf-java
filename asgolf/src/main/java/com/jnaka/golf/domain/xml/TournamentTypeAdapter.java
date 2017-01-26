package com.jnaka.golf.domain.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.jnaka.golf.domain.Tournament;

public class TournamentTypeAdapter extends XmlAdapter<String, Tournament.Type> {

	@Override
	public String marshal(Tournament.Type v) throws Exception {
		return v.getCode();
	}

	@Override
	public Tournament.Type unmarshal(String v) throws Exception {
		return Tournament.Type.valueOfByCode(v);
	}

}

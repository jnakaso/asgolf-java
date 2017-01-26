package com.jnaka.golf.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jnaka.dao.ObjectDao;
import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;

public interface TournamentDao extends ObjectDao<Tournament> {
	
	public static Tournament.Type parseTournamentType(String value) {
		String code = StringUtils.trimToEmpty(value);
		return Tournament.Type.valueOfByCode(code);
	}

	List<Tournament> findBySeason(int season);

	boolean addRound(Tournament tournament, Round round);

	boolean addKp(Tournament tournament, Kp kp);

	boolean removeRound(Tournament tournament, Round round);

	boolean removeKp(Tournament tournament, Kp kp);

}

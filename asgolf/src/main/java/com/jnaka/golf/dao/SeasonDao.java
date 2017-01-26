package com.jnaka.golf.dao;

import java.util.List;

import com.jnaka.dao.ObjectDao;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;

public interface SeasonDao extends ObjectDao<Season> {

	public List<Tournament> findTournamentsBySeason(Season season);

}

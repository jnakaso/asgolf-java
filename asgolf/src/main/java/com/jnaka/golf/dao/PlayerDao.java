package com.jnaka.golf.dao;

import java.util.List;

import com.jnaka.dao.ObjectDao;
import com.jnaka.golf.domain.Player;

public interface PlayerDao extends ObjectDao<Player> {

	public List<Player> findActive();

}

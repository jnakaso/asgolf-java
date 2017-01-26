package com.jnaka.golf.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Repository;

import com.jnaka.golf.domain.Player;

@Repository("PlayerDao")
public class PlayerJaxbDao extends ObjectJaxbDao<Player> implements PlayerDao {

	@Override
	public void create(Player player) {
		player.setId(this.getLastId() + 1);
		this.getGolfClub().addPlayer(player);
	}

	@Override
	public boolean delete(Player player) {
		return this.getGolfClub().removePlayer(player);
	}

	@Override
	public List<Player> getAll() {
		return new ArrayList<Player>(this.getGolfClub().getPlayers());
	}

	@Override
	public List<Player> findActive() {
		List<Player> filtered = new ArrayList<Player>(this.getGolfClub().getPlayers());
		CollectionUtils.filter(filtered, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return ((Player) object).getActive();
			}
		});
		return filtered;
	}
}

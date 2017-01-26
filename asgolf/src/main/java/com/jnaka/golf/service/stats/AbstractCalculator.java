package com.jnaka.golf.service.stats;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnaka.golf.dao.PlayerDao;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.RoundService;
import com.jnaka.golf.service.TournamentService;

public abstract class AbstractCalculator<T> implements StatsCalculator<T>{

	@Autowired
	private PlayerDao playerDao;
	@Autowired
	private TournamentService tournamentService;
	@Autowired
	private RoundService roundService;

	abstract public List<T> getEntries(Season season);

	protected List<Player> lookupPlayers() {
		List<Player> players = this.getPlayerDao().getAll();
		Collections.sort(players, PlayerService.PLAYER_LAST_NAME_COMPARATOR);
		return players;
	}

	public PlayerDao getPlayerDao() {
		return playerDao;
	}

	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	public TournamentService getTournamentService() {
		return tournamentService;
	}

	public void setTournamentService(TournamentService tournamentService) {
		this.tournamentService = tournamentService;
	}

	public RoundService getRoundService() {
		return roundService;
	}

	public void setRoundService(RoundService roundService) {
		this.roundService = roundService;
	}
}

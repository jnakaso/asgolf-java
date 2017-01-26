package com.jnaka.golf.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jnaka.dao.ObjectDao;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.TournamentService;

@Service("PlayerService")
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	@Qualifier("PlayerDao")
	private ObjectDao<Player> playerDao;
	@Autowired
	private TournamentService tournamentService;

	@Override
	public Player get(Number id) {
		return this.getPlayerDao().load(id);
	}

	@Override
	public Integer getCourseAdjustedHandicap(Player player, CourseTee courseTee) {
		float slope = courseTee.getSlope().floatValue();
		float handicap = player.getHandicap().floatValue();
		return new Integer(new Float(slope * handicap / 113.0).intValue());
	}

	@Override
	public void updateSummary(SeasonSummary summary) {
		Player player = summary.getPlayer();
		if (player.getSeasonSummaries() == null) {
			System.out.print(player);
		}
		for (SeasonSummary exist : player.getSeasonSummaries()) {
			if (exist.getSeason().equals(summary.getSeason())) {
				if (player.removeSeasonSummary(exist)) {
					player.addSeasonSummary(summary);
				}
				return;
			}
		}
		player.addSeasonSummary(summary);
	}

	public List<Round> findRounds(final Player player) {
		return this.getTournamentService().findRoundsByPlayer(player);
	}

	@Override
	public NavigableSet<SeasonSummary> getSeasonSummaries(Integer id) {
		Player player = this.get(id);
		return player.getSeasonSummaries();
	}

	@SuppressWarnings("unchecked")
	public List<Round> findRounds(final Player player, int season) {
		List<Tournament> tournaments = this.getTournamentService().findBySeason(season);
		Predicate predicate = new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				Round round = (Round) arg0;
				return (round.getPlayer().equals(player));
			}
		};
		List<Round> rounds = new ArrayList<Round>();
		for (Tournament tour : tournaments) {
			rounds.addAll(CollectionUtils.select(tour.getRounds(), predicate));
		}
		return rounds;
	}

	@Override
	public List<Player> getAll() {
		List<Player> players = this.getPlayerDao().getAll();
		Collections.sort(players, PLAYER_LAST_NAME_COMPARATOR);
		return players;
	}

	@Override
	public List<Player> getAll(final boolean active) {
		List<Player> players = new ArrayList<Player>(this.getPlayerDao().getAll());
		CollectionUtils.filter(players, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return ((Player) object).getActive() == active;
			}
		});
		Collections.sort(players, PLAYER_LAST_NAME_COMPARATOR);
		return players;
	}

	@Override
	public Player newInstance() {
		Player player = new Player();
		player.setFirstName(StringUtils.EMPTY);
		player.setLastName(StringUtils.EMPTY);
		player.setHandicap(0f);
		player.setActive(true);
		return player;
	}

	@Override
	public void create(Player player) {
		this.getPlayerDao().create(player);
	}

	@Override
	public boolean delete(Player player) {
		return this.getPlayerDao().delete(player);
	}

	@Override
	public void update(Player player) {
		this.getPlayerDao().update(player);
	}

	public ObjectDao<Player> getPlayerDao() {
		return playerDao;
	}

	public void setPlayerDao(ObjectDao<Player> playerDao) {
		this.playerDao = playerDao;
	}

	public TournamentService getTournamentService() {
		return this.tournamentService;
	}

	public void setTournamentService(TournamentService service) {
		this.tournamentService = service;
	}

}

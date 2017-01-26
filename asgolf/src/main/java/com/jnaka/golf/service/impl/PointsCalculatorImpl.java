package com.jnaka.golf.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.service.PointsCalculator;
import com.jnaka.golf.service.SeasonService;

@Service("PointsCalculator")
@Scope("prototype")
public class PointsCalculatorImpl implements PointsCalculator {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private SeasonService seasonService;

	private Season season;
	private MultiMap playerMap = new MultiValueMap();
	private List<Tournament> tournaments;
	
	public PointsCalculatorImpl(Season season) {
		super();
		this.season = season;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	MultiMap getPlayerMap() {
		return playerMap;
	}

	void setPlayerMap(MultiMap playerMap) {
		this.playerMap = playerMap;
	}

	List<Tournament> getTournaments() {
		if (this.tournaments == null) {
			this.setTournaments(this.getSeasonService().findTournaments(this.getSeason()));
		}
		return tournaments;
	}

	void setTournaments(List<Tournament> tournaments) {
		this.tournaments = tournaments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnaka.golf.service.impl.PointsCalculator#update()
	 */
	public void update() {
		this.updateKpWinnings();
		this.updateTournamentWinnings();
		this.updateAttendancePoints();
		this.updateSeasonSummaries();
	}

	void updateSeasonSummaries() {
		List<SeasonSummary> newSummaries = new ArrayList<SeasonSummary>();
		for (Object object : this.getPlayerMap().keySet()) {
			Player player = (Player) object;
			@SuppressWarnings("unchecked")
			Collection<SeasonSummary> summaries = (Collection<SeasonSummary>) this.getPlayerMap().get(player);
			SeasonSummary sum = this.getSeasonService().sum(summaries);
			this.logger.info(sum.toString());
			newSummaries.add(sum);
		}
		this.getSeasonService().updateSummaries(this.getSeason(), newSummaries);
	}

	void updateAttendancePoints() {
		PrizeMoney roundMoney = this.getAttendanceMoney();
		for (Tournament tournament : this.getTournaments()) {
			for (Round round : tournament.getRounds()) {
				SeasonSummary holder = this.createSummary(round.getPlayer(), roundMoney.getEarnings(),
						roundMoney.getPoints(), 1, 0);
				this.getPlayerMap().put(round.getPlayer(), holder);
			}
		}
	}

	void updateTournamentWinnings() {
		for (Tournament tournament : this.getTournaments()) {
			for (Winner winner : tournament.getWinners()) {
				SeasonSummary holder = this.createSummary(winner.getRound().getPlayer(), winner.getEarnings(),
						winner.getPoints(), 0, 0);
				this.getPlayerMap().put(winner.getRound().getPlayer(), holder);
			}
		}
	}

	void updateKpWinnings() {
		PrizeMoney kpMoney = this.getKpMoney();
		for (Tournament tournament : this.getTournaments()) {
			for (Kp kp : tournament.getKps()) {
				SeasonSummary holder = this.createSummary(kp.getPlayer(), kpMoney.getEarnings(), kpMoney.getPoints(),
						0, 1);
				this.getPlayerMap().put(kp.getPlayer(), holder);
			}
		}
	}

	SeasonSummary createSummary(Player player, Float earnings, Float points, int attendance, int kp) {
		SeasonSummary summary = new SeasonSummary();
		summary.setSeason(this.getSeason());
		summary.setPlayer(player);
		summary.setEarnings(earnings);
		summary.setPoints(points);
		summary.setAttendance(attendance);
		summary.setKps(kp);
		return summary;
	}

	PrizeMoney getAttendanceMoney() {
		return this.getSeasonService().getAttendancePrizeMoney(this.getSeason());
	}

	PrizeMoney getKpMoney() {
		return this.getSeasonService().getKpPrizeMoney(this.getSeason());
	}

	public SeasonService getSeasonService() {
		return seasonService;
	}

	public void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}

}

package com.jnaka.golf.service.stats;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.SeasonService;

/**
 * 
 * <pre>
 * <PlayerScoring playerId="1">
 * 	<firstName>Bruce</firstName>
 * 	<lastName>Kaneshiro</lastName>
 * 	<points>71.5</points>
 * 	<earnings>160.83</earnings>
 * 	<hdcp>7.18</hdcp>
 * 	<attendance>12</attendance>
 * 	<kps>5</kps>
 *  <pointsByTour></pointsByTour>
 *  <earningsByTour></earningsByTour>
 *  <kpsByTour></kpsByTour>
 * </PlayerScoring>
 * </pre>
 * 
 * @author jnakaso
 * 
 */
@Component
public class StandingsCalculator extends AbstractCalculator<StandingsCalculator.Entry> {

	public class Entry {
		final public Player player;
		public Float points = Float.valueOf("0.0");
		public Float earnings = Float.valueOf("0.00");
		public Float hdcp = Float.valueOf("0.0");
		public Integer attendance = Integer.valueOf("0");
		public Integer kps = Integer.valueOf("0");
		public List<BigDecimal> pointsByTournament = new ArrayList<BigDecimal>();
		public List<Integer> kpsByTournament = new ArrayList<Integer>();
		public List<BigDecimal> earningsByTournament = new ArrayList<BigDecimal>();

		public Entry(Player player, Float points, Float earnings, Float hdcp, Integer attendance, Integer kps,
				List<BigDecimal> pointsByTournament, List<Integer> kpsByTournament,
				List<BigDecimal> earningsByTournament) {
			super();
			this.player = player;
			this.points = points;
			this.earnings = earnings;
			this.hdcp = hdcp;
			this.attendance = attendance;
			this.kps = kps;
			this.pointsByTournament.addAll(pointsByTournament);
			this.kpsByTournament.addAll(kpsByTournament);
			this.earningsByTournament.addAll(earningsByTournament);
		}

		public Entry(Player player, Float points, Float earnings, Float hdcp, Integer attendance, Integer kps) {
			super();
			this.player = player;
			this.points = points;
			this.earnings = earnings;
			this.hdcp = hdcp;
			this.attendance = attendance;
			this.kps = kps;
		}

		public Entry(Player player) {
			super();
			this.player = player;
		}
	}

	private static Comparator<Entry> BY_POINTS = new Comparator<Entry>() {
		@Override
		public int compare(Entry o1, Entry o2) {
			return o2.points.compareTo(o1.points);
		}
	};

	@Autowired
	private SeasonService seasonService;

	@Autowired
	private PlayerService playerService;

	@Override
	public List<Entry> getEntries(Season season) {
		List<Entry> entries = new ArrayList<Entry>();
		for (Player player : this.getPlayerDao().getAll()) {
			if (player.getActive()) {
				SeasonSummary seasonSummary = this.getSummary(player, season);
				Entry entry = this.createEntry(player, seasonSummary);
				entries.add(entry);
				this.addByTournamentInfo(entry, season);
			}
		}
		Collections.sort(entries, BY_POINTS);
		return entries;
	}

	private void addByTournamentInfo(Entry entry, Season season) {
		for (Tournament tour : this.getTournamentService().findBySeason(season.getId())) {
			entry.pointsByTournament.add(this.getPoints(tour, entry.player));
			entry.kpsByTournament.add(this.getKps(tour, entry.player));
			entry.earningsByTournament.add(this.getEarnings(tour, entry.player));
		}
	}

	private BigDecimal getPoints(Tournament tour, final Player player) {
		BigDecimal points = BigDecimal.ZERO;
		if (this.getTournamentService().hasPlayed(tour, player)) {
			points = points.add(new BigDecimal("3.0"));
		}
		for (Winner winner : tour.getWinners()) {
			if (winner.getRound().getPlayer().equals(player)) {
				points = points.add(new BigDecimal(winner.getPoints()));
				return points;
			}
		}
		return points;
	}

	private Integer getKps(Tournament tour, Player player) {
		int count = 0;
		for (Kp kp : tour.getKps()) {
			if (kp.getPlayer().equals(player)) {
				count++;
			}
		}
		return count;
	}

	private BigDecimal getEarnings(Tournament tour, Player player) {

		PrizeMoney kpMoney = this.getSeasonService().getKpPrizeMoney(tour.getSeason());

		BigDecimal points = BigDecimal.ZERO;
		for (Kp kp : tour.getKps()) {
			if (kp.getPlayer().equals(player)) {
				points = points.add(new BigDecimal(kpMoney.getEarnings()));
			}
		}

		for (Winner winner : tour.getWinners()) {
			if (winner.getRound().getPlayer().equals(player)) {
				points = points.add(BigDecimal.valueOf(winner.getEarnings()));
			}
		}

		return points;
	}

	private Entry createEntry(Player player, SeasonSummary seasonSummary) {
		if (seasonSummary == null) {
			return new Entry(player);
		} else {
			return new Entry(player, seasonSummary.getPoints(), seasonSummary.getEarnings(),
					seasonSummary.getHandicap(), seasonSummary.getAttendance(), seasonSummary.getKps());
		}
	}

	private SeasonSummary getSummary(Player player, Season season) {
		for (SeasonSummary summary : this.getPlayerService().getSeasonSummaries(player.getId())) {
			if (summary.getSeason().getId().intValue() == season.getId()) {
				return summary;
			}
		}
		return null;
	}

	PlayerService getPlayerService() {
		return this.playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	SeasonService getSeasonService() {
		return seasonService;
	}

	void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}

}

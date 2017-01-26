package com.jnaka.golf.service.stats;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.service.stats.MoneyPlayerCalculator.Entry;

/**
 * 
 * 
 * <pre>
 * <BigMoney>
 *  <player playerId="1" active="true">
 *   <playerName>Bruce Kaneshiro</playerName>
 *   <tournamentCount>4</tournamentCount>
 *   <points>42</points>
 *   <earnings>85</earnings>
 *   <finishes>2,4,2</finishes>
 *  </player>
 * </BigMoney>
 * </pre>
 * 
 * @author jnakaso
 * 
 */
@Component
public class MoneyPlayerCalculator extends AbstractCalculator<Entry> {
	public class Entry {
		private Player player;
		private Integer tournaments = 0;
		private BigDecimal points = BigDecimal.ZERO;
		private BigDecimal earnings = BigDecimal.ZERO;
		private final Integer finishes[] = { 0, 0, 0 };

		public Player getPlayer() {
			return player;
		}

		public void setPlayer(Player player) {
			this.player = player;
		}

		public Integer getTournaments() {
			return tournaments;
		}

		public void setTournaments(Integer tournaments) {
			this.tournaments = tournaments;
		}

		public BigDecimal getPoints() {
			return points;
		}

		public void setPoints(BigDecimal points) {
			this.points = points;
		}

		public BigDecimal getEarnings() {
			return earnings;
		}

		public void setEarnings(BigDecimal earnings) {
			this.earnings = earnings;
		}

		public Integer[] getFinishes() {
			return finishes;
		}
	}

	public static class Finish {
		public BigDecimal earnings = BigDecimal.ZERO;
		public BigDecimal points = BigDecimal.ZERO;
		public int standing = 0;

		public Finish() {
			super();
		}

		public Finish(BigDecimal earnings, BigDecimal points, int standing) {
			super();
			this.earnings = earnings;
			this.points = points;
			this.standing = standing;
		}
	}

	@Autowired
	private SeasonService seasonService;

	public List<Entry> getEntries(Season season) {
		List<Entry> entries = new ArrayList<Entry>();
		for (Player player : this.lookupPlayers()) {
			List<Finish> finishes = this.lookupTournaments(player);
			entries.add(this.createEntry(player, finishes));
		}
		return entries;
	}

	Entry createEntry(Player player, List<Finish> finishes) {
		Entry entry = new Entry();
		entry.setPlayer(player);
		for (Finish finish : finishes) {
			entry.setTournaments(entry.getTournaments() + 1);
			entry.setEarnings(entry.getEarnings().add(finish.earnings));
			entry.setPoints(entry.getPoints().add(finish.points));
			if (1 <= finish.standing && finish.standing <= 3) {
				int index = finish.standing - 1;
				entry.getFinishes()[index] = entry.getFinishes()[index] + 1;
			}
		}
		return entry;
	}

	List<Finish> lookupTournaments(final Player player) {
		List<Finish> finishes = new ArrayList<MoneyPlayerCalculator.Finish>();
		for (Season aSeason : this.getSeasonService().getAll()) {
			List<Tournament> tournaments = this.getTournamentService().findBySeason(aSeason.getId());
			if (this.playedDayOne(tournaments, player)) {
				Tournament dayTwo = this.findDayTwo(tournaments, player);
				if (dayTwo != null) {
					finishes.add(this.createFinish(player, dayTwo));
				}
			}
		}
		return finishes;
	}

	private Finish createFinish(Player player, Tournament dayTwo) {
		Winner winner = this.findWinner(player, dayTwo);
		if (winner == null) {
			return new Finish();
		} else {
			BigDecimal earnings = new BigDecimal(winner.getEarnings());
			BigDecimal points = new BigDecimal(winner.getPoints());
			int standing = Integer.parseInt(winner.getFinish().substring(1, winner.getFinish().length()));
			return new Finish(earnings, points, standing);
		}
	}

	private Winner findWinner(final Player player, Tournament dayTwo) {
		return (Winner) CollectionUtils.find(dayTwo.getWinners(), new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				Winner winner = (Winner) object;
				return winner.getRound().getPlayer().equals(player);
			}
		});
	}

	private Tournament findDayTwo(List<Tournament> tournaments, final Player player) {
		return (Tournament) CollectionUtils.find(tournaments, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				Tournament tour = (Tournament) object;
				if (Tournament.Type.DAY_TWO == tour.getType()) {
					return CollectionUtils.exists(tour.getRounds(), new Predicate() {
						@Override
						public boolean evaluate(Object object) {
							Round round = (Round) object;
							return round.getPlayer().equals(player);
						}
					});
				}
				return false;
			}
		});
	}

	private boolean playedDayOne(List<Tournament> tournaments, final Player player) {
		return CollectionUtils.exists(tournaments, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				Tournament tour = (Tournament) object;
				if (Tournament.Type.DAY_ONE == tour.getType()) {
					return CollectionUtils.exists(tour.getRounds(), new Predicate() {
						@Override
						public boolean evaluate(Object object) {
							Round round = (Round) object;
							return round.getPlayer().equals(player);
						}
					});
				}
				return false;
			}
		});
	}

	public SeasonService getSeasonService() {
		return seasonService;
	}

	public void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}

}

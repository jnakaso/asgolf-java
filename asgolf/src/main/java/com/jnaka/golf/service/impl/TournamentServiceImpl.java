package com.jnaka.golf.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jnaka.golf.dao.TournamentDao;
import com.jnaka.golf.dao.TournamentReader;
import com.jnaka.golf.dao.TournamentWriter;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.TwoDaySummary;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.service.HandicapCalculator;
import com.jnaka.golf.service.HandicapCalculatorFactory;
import com.jnaka.golf.service.PrizeCalculator;
import com.jnaka.golf.service.PrizeCalculatorFactory;
import com.jnaka.golf.service.RoundService;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.service.TournamentService;

@Service("TournamentService")
public class TournamentServiceImpl implements TournamentService {

	static Comparator<Round> DATE_COMPARATOR = new Comparator<Round>() {
		@Override
		public int compare(Round o1, Round o2) {
			try {
				return o2.getTournament().getDate().compareTo(o1.getTournament().getDate());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
	};

	static Comparator<Kp> KP_HOLE_COMPARATOR = new Comparator<Kp>() {
		@Override
		public int compare(Kp o1, Kp o2) {
			return o1.getHole().compareTo(o2.getHole());
		}
	};

	@Autowired
	@Qualifier("TournamentDao")
	private TournamentDao dao;
	@Autowired
	private TournamentWriter writer;
	@Autowired
	@Qualifier("TournamentReader")
	private TournamentReader reader;
	@Autowired
	private RoundService roundService;
	@Autowired
	private SeasonService seasonService;
	@Autowired
	private PrizeCalculatorFactory prizeCalculatorFactory;
	@Autowired
	private HandicapCalculatorFactory handicapCalculatorFactory;

	public TournamentDao getDao() {
		return dao;
	}

	public void setDao(TournamentDao dao) {
		this.dao = dao;
	}

	public TournamentWriter getWriter() {
		return writer;
	}

	public void setWriter(TournamentWriter writer) {
		this.writer = writer;
	}

	public TournamentReader getReader() {
		return this.reader;
	}

	public void setReader(TournamentReader reader) {
		this.reader = reader;
	}

	public RoundService getRoundService() {
		return this.roundService;
	}

	public void setRoundService(RoundService roundService) {
		this.roundService = roundService;
	}

	public SeasonService getSeasonService() {
		return seasonService;
	}

	public void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}

	public PrizeCalculatorFactory getPrizeCalculatorFactory() {
		return this.prizeCalculatorFactory;
	}

	public void setPrizeCalculatorFactory(PrizeCalculatorFactory prizeCalculatorFactory) {
		this.prizeCalculatorFactory = prizeCalculatorFactory;
	}

	public HandicapCalculatorFactory getHandicapCalculatorFactory() {
		return handicapCalculatorFactory;
	}

	public void setHandicapCalculatorFactory(HandicapCalculatorFactory handicapCalculatorFactory) {
		this.handicapCalculatorFactory = handicapCalculatorFactory;
	}

	@Override
	public Tournament get(Number id) {
		return this.getDao().load(id);
	}

	@Override
	public List<Tournament> getAll() {
		return this.getDao().getAll();
	}
	

	@Override
	public Tournament getLastTournament() {
		 List<Tournament> tournaments = this.getDao().getAll();
		Collections.sort(tournaments,
				new Comparator<Tournament>() {

					@Override
					public int compare(Tournament o1, Tournament o2) {
						return o1.getId().compareTo(o2.getId());
					}
				});
		return tournaments.stream().findFirst().get();
	}

	@Override
	public List<Tournament> findBySeason(int seasonID) {
		return this.getDao().findBySeason(seasonID);
	}

	private Predicate createPlayerPredicate(final Player player) {
		return new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				Round round = (Round) arg0;
				return round.getPlayer().equals(player);
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Round> findRoundsBySeason(int season, final Player player) {
		Predicate predicate = this.createPlayerPredicate(player);
		List<Round> rounds = new ArrayList<Round>();
		for (Tournament tour : this.getDao().findBySeason(season)) {
			rounds.addAll(CollectionUtils.select(tour.getRounds(), predicate));
		}
		return rounds;
	}

	@Override
	public List<Round> findRoundsBySeason(int season) {
		List<Round> rounds = new ArrayList<Round>();
		for (Tournament tour : this.getDao().findBySeason(season)) {
			rounds.addAll(tour.getRounds());
		}
		return rounds;
	}

	@Override
	public List<Round> findRoundsByPlayer(final Player player) {
		List<Round> rounds = new ArrayList<Round>();
		for (Tournament tournament : this.getDao().getAll()) {
			@SuppressWarnings("unchecked")
			Collection<Round> playerRounds = CollectionUtils.select(tournament.getRounds(), new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					Round round = (Round) arg0;
					if (round.getPlayer() == null) {
						return false;
					}
					return round.getPlayer().equals(player);
				}
			});
			rounds.addAll(playerRounds);
		}
		Collections.sort(rounds, DATE_COMPARATOR);
		return rounds;
	}

	@Override
	public List<Round> findKpSweepers(int season) {
		List<Round> rounds = new ArrayList<Round>();
		for (Tournament tour : this.getDao().findBySeason(season)) {
			MultiMap kpMap = new MultiValueMap();
			for (Kp kp : tour.getKps()) {
				kpMap.put(kp.getPlayer(), kp);
			}
			for (Object object : kpMap.keySet()) {
				final Player player = (Player) object;
				@SuppressWarnings("unchecked")
				List<Kp> kps = (List<Kp>) kpMap.get(player);
				if (kps.size() == 4) {
					Round round = (Round) CollectionUtils.find(tour.getRounds(), new Predicate() {
						@Override
						public boolean evaluate(Object object) {
							Round round = (Round) object;
							return round.getPlayer().equals(player);
						}
					});
					rounds.add(round);
				}
			}
		}
		return rounds;
	}

	@Override
	public List<Kp> findKps(Tournament tournament, final Flight flight) {
		Set<Kp> kps = new TreeSet<Kp>(KP_HOLE_COMPARATOR);
		CollectionUtils.select(tournament.getKps(), new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				return ((Kp) arg0).getFlight() == flight;
			}
		}, kps);
		return new ArrayList<Kp>(kps);
	}

	public List<Winner> findWinners(Tournament tournament, final Flight flight) {
		PrizeCalculator calc = this.getPrizeCalculatorFactory().getCalculator(tournament);
		List<Winner> winners = new ArrayList<Winner>(calc.findWinners());
		CollectionUtils.filter(winners, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return ((Winner) object).getRound().getFlight() == flight;
			}
		});
		return winners;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TwoDaySummary> getTwoDayRounds(int seasonID) {
		List<Tournament> tournaments = this.getDao().findBySeason(seasonID);

		CollectionUtils.filter(tournaments, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				Tournament tournament = (Tournament) object;
				return tournament.getType().isTwoDay();
			}
		});

		if (tournaments.isEmpty()) {
			return new ArrayList<TwoDaySummary>();
		}

		if (tournaments.size() == 1) {
			List<TwoDaySummary> summaries = new ArrayList<TwoDaySummary>();
			for (Round round : tournaments.get(0).getRounds()) {
				summaries.add(this.createTwoDaySummary(round));
			}
			return summaries;
		}

		MultiMap multi = new MultiValueMap();
		for (Tournament tournament : tournaments) {
			for (Round round : tournament.getRounds()) {
				if (round.getFlight() != null) {
					multi.put(round.getPlayer().getId(), round);
				}
			}
		}

		List<TwoDaySummary> summaries = new ArrayList<TwoDaySummary>();
		for (Object object : multi.keySet()) {
			Collection<Round> rounds = (Collection<Round>) multi.get(object);
			if (rounds.size() == 2) {
				summaries.add(this.createTwoDaySummary(rounds));
			}
		}
		return summaries;
	}

	TwoDaySummary createTwoDaySummary(Collection<Round> rounds) {
		TwoDaySummary summary = new TwoDaySummary();
		int i = 0;
		for (Round round : rounds) {
			summary.setFlight(round.getFlight());
			summary.setHandicap(round.getHandicap());
			summary.setPlayer(round.getPlayer());
			summary.setTotal(summary.getTotal() + round.getTotal());
			summary.setTotalNet(summary.getTotalNet() + round.getTotalNet().intValue());
			if (i == 0) {
				summary.setDayOne(round.getTotal());
			} else {
				summary.setDayTwo(round.getTotal());
			}
			i++;
			summary.setNumRound(i);
		}
		return summary;
	}

	TwoDaySummary createTwoDaySummary(Round round) {
		TwoDaySummary summary = new TwoDaySummary();
		summary.setFlight(round.getFlight());
		summary.setHandicap(round.getHandicap());
		summary.setNumRound(1);
		summary.setPlayer(round.getPlayer());
		summary.setTotal(round.getTotal());
		summary.setTotalNet(round.getTotalNet().intValue());
		summary.setDayOne(round.getTotal());
		return summary;
	}

	List<Winner> createWinners(List<Round> winnerRounds, List<PrizeMoney> prizeMoney) {
		int numWinners = winnerRounds.size();
		List<PrizeMoney> placePrizeMoney = new ArrayList<PrizeMoney>();
		int count = Math.min(numWinners, prizeMoney.size());
		for (int i = 0; i < count; i++) {
			placePrizeMoney.add(prizeMoney.remove(0));
		}
		return this.divideWinnings(winnerRounds, placePrizeMoney);
	}

	List<Winner> divideWinnings(List<Round> winnerRounds, List<PrizeMoney> placePrizeMoney) {
		int place = Integer.MAX_VALUE;
		float earnings = 0;
		float points = 0;
		for (PrizeMoney prize : placePrizeMoney) {
			place = Math.min(place, prize.getPlace());
			earnings += prize.getEarnings();
			points += prize.getPoints();
		}

		int count = winnerRounds.size();
		List<Winner> winners = new ArrayList<Winner>();
		for (Round round : winnerRounds) {
			String finish = round.getFlight() + Integer.toString(place);
			Winner winner = new Winner(round, finish, points / count, earnings / count);
			winners.add(winner);
		}
		return winners;
	}

	@Override
	public void create(Tournament anObject) {
		this.getDao().create(anObject);
	}

	@Override
	public boolean delete(Tournament anObject) {
		return this.getDao().delete(anObject);
	}

	@Override
	public Tournament newInstance() {
		Tournament tour = new Tournament();
		tour.setDate(new Date());
		return tour;
	}

	@Override
	public void update(Tournament tournament) {
		this.updateRounds(tournament);
		tournament.getWinners().clear();
		for (Flight flight : Flight.values()) {
			List<Winner> winners = this.findWinners(tournament, flight);
			for (Winner winner : winners) {
				tournament.addWinner(winner);
			}
		}
		this.updatePlayers(tournament);
		this.getSeasonService().updateTotals(tournament.getSeason());
	}

	void updateRounds(Tournament tournament) {
		CollectionUtils.forAllDo(tournament.getRounds(), new Closure() {
			@Override
			public void execute(Object input) {
				Round round = (Round) input;
				getRoundService().updateTotals(round);
			}
		});
	}

	void updatePlayers(Tournament tournament) {
		final HandicapCalculator calc = this.getHandicapCalculatorFactory().getCalculator(tournament.getSeason());
		CollectionUtils.forAllDo(tournament.getRounds(), new Closure() {
			@Override
			public void execute(Object input) {
				Round round = (Round) input;
				Player player = round.getPlayer();
				calc.updateHandicap(player);
			}
		});
	}

	public boolean export(Tournament tournament, File file) {
		this.getWriter().write(tournament, file);
		return true;
	}

	public Tournament importTournament(File file) {
		Tournament tournament = this.getReader().read(file);
		return tournament;
	}

	@Override
	public void updateTotals(Tournament tournament) {
		tournament.clearWinners();

		for (Flight flight : Flight.values()) {
			List<Winner> winners = this.findWinners(tournament, flight);
			for (Winner winner : winners) {
				tournament.addWinner(winner);
			}
		}
	}

	@Override
	public float averageNet(Tournament tournament) {
		List<Round> rounds = new ArrayList<Round>(tournament.getRounds());
		return getRoundService().average(rounds, RoundService.Filter.TOTAL_NET);
	}

	@Override
	public boolean addRound(Tournament tournament, Round round) {
		return this.getDao().addRound(tournament, round);
	}

	@Override
	public boolean addKp(Tournament tournament, Kp kp) {
		return this.getDao().addKp(tournament, kp);
	}

	@Override
	public boolean removeRound(Tournament tournament, Round round) {
		return this.getDao().removeRound(tournament, round);
	}

	@Override
	public boolean removeKp(Tournament tournament, Kp kp) {
		return this.getDao().removeKp(tournament, kp);
	}

	@Override
	public boolean hasPlayed(Tournament tournament, final Player player) {
		return CollectionUtils.exists(tournament.getRounds(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return ((Round) object).getPlayer().equals(player);
			}
		});
	}


}

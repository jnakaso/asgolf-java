package com.jnaka.golf.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jnaka.golf.dao.SeasonDao;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Season.HandicapPolicy;
import com.jnaka.golf.domain.Season.ScoringPolicy;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.TwoDaySummary;
import com.jnaka.golf.reports.StatsWriter;
import com.jnaka.golf.service.HandicapCalculator;
import com.jnaka.golf.service.HandicapCalculatorFactory;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.PointsCalculator;
import com.jnaka.golf.service.PointsCalculatorFactory;
import com.jnaka.golf.service.PrizeCalculatorFactory;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.service.TournamentService;


@Service("SeasonService")
public class SeasonServiceImpl implements SeasonService {

	static final Comparator<? super Season> DATE_COMPARATOR = new Comparator<Season>() {
		@Override
		public int compare(Season o1, Season o2) {
			Integer id1 = Integer.valueOf(o1.getId().intValue());
			Integer id2 = Integer.valueOf(o2.getId().intValue());
			return id2.compareTo(id1);
		}
	};

	private static final Comparator<? super TwoDaySummary> TWO_SUMMARY_COMPARATOR = new Comparator<TwoDaySummary>() {
		@Override
		public int compare(TwoDaySummary o1, TwoDaySummary o2) {
			return o1.getTotalNet().compareTo(o2.getTotalNet());
		}
	};
	
	private static final Comparator<Season> NEWEST = new Comparator<Season>() {
		@Override
		public int compare(Season arg0, Season arg1) {
			return arg1.getId().compareTo(arg0.getId());
		}
	};

	@Autowired
	private TournamentService tournamentService;
	@Autowired
	private PlayerService playerService;
	@Autowired
	@Qualifier("SeasonDao")
	private SeasonDao seasonDao;
	@Autowired
	private StatsWriter writer;

	@Autowired
	private PrizeCalculatorFactory prizeCalculatorFactory;
	@Autowired
	private PointsCalculatorFactory pointsCalculatorFactory;
	@Autowired
	private HandicapCalculatorFactory handicapCalculatorFactory;

	@Override
	public Season currentSeason() {
		List<Season> seasons = this.getSeasonDao().getAll();
		if (seasons.isEmpty()) {
			return null;
		}
		seasons = new ArrayList<Season>(seasons);
		Collections.sort(seasons, NEWEST);
		return seasons.get(0);
	}

	@Override
	public Season get(Number id) {
		return this.getSeasonDao().load(id);
	}

	public boolean export(Season season, File selected) {
		this.getWriter().setFileName(selected.getAbsolutePath());
		this.getWriter().setSeasonID(season.getId().intValue());
		this.getWriter().extract();
		return true;
	}

	@Override
	public void updateSummaries(Season season, List<SeasonSummary> newSummaries) {
		for (SeasonSummary summary : newSummaries) {
			summary.setSeason(season);
			this.getPlayerService().updateSummary(summary);
		}
	}

	public SeasonSummary sum(Collection<SeasonSummary> summaries) {
		SeasonSummary total = new SeasonSummary();
		for (SeasonSummary summary : summaries) {
			total.setSeason(summary.getSeason());
			total.setPlayer(summary.getPlayer());
			total.setAttendance(total.getAttendance() + summary.getAttendance());
			total.setKps(total.getKps() + summary.getKps());
			total.setEarnings(total.getEarnings() + summary.getEarnings());
			total.setPoints(total.getPoints() + summary.getPoints());
		}
		return total;
	}

	public PrizeMoney getAttendancePrizeMoney(Season season) {
		return PrizeCalculatorFactory.ATTENDANCE_PRIZE;
	}

	public PrizeMoney getKpPrizeMoney(Season season) {
		return PrizeCalculatorFactory.KP_PRIZES.get(season.getScoringPolicy()).get(0);
	}

	public List<Tournament> findTournaments(final Season season) {
		return this.getSeasonDao().findTournamentsBySeason(season);
	}

	@SuppressWarnings("unchecked")
	public List<Tournament> findTournaments(final Season season, final Tournament.Type type) {
		return new ArrayList<Tournament>(CollectionUtils.select(this.getSeasonDao().findTournamentsBySeason(season),
				new Predicate() {
					@Override
					public boolean evaluate(Object object) {
						return ((Tournament) object).getType() == type;
					}
				}));
	}

	@Override
	public boolean addTournament(Season season, Tournament tournament) {
		tournament.setSeason(season);
		this.updateTotals(season);
		return true;
	}

	@Override
	public List<SeasonSummary> findSummaries(Season season) {
		List<SeasonSummary> summaries = new ArrayList<SeasonSummary>();
		for (Player player : this.getPlayerService().getAll()) {
			for (SeasonSummary summary :  this.getPlayerService().getSeasonSummaries(player.getId())) {
				if (summary.getSeason().getId().equals(season.getId())) {
					summaries.add(summary);
				}
			}
		}
		return summaries;
	}

	@Override
	public List<Player> getActivePlayers(Season season) {
		return this.getPlayerService().getAll(true);
	}

	@Override
	public List<Season> getAll() {
		List<Season> seasons = this.getSeasonDao().getAll();
		Collections.sort(seasons, DATE_COMPARATOR);
		return seasons;
	}

	@Override
	public void create(Season season) {
		this.getSeasonDao().create(season);
	}

	@Override
	public boolean delete(Season season) {
		return this.getSeasonDao().delete(season);
	}

	@Override
	public Season newInstance() {
		Season season = new Season();
		season.setId(this.getSeasonDao().getLastId() + 1);
		season.setHandicapPolicy(HandicapPolicy.FIVE_OF_TEN);
		season.setScoringPolicy(ScoringPolicy.DEFAULT_20);
		return season;
	}

	@Override
	public void update(Season season) {
		this.getSeasonDao().update(season);
	}

	public void updateTotals(Season season) {
		PointsCalculator pointsCalc = this.getPointsCalculatorFactory().getCalculator(season);
		pointsCalc.update();
	}

	public void updateHandicaps(Season season) {
		HandicapCalculator calc = this.getHandicapCalculatorFactory().getCalculator(season);
		List<Player> players = this.getActivePlayers(season);
		for (Player player : players) {
			calc.updateHandicap(player);
		}
	}

	public List<TwoDaySummary> findTwoDaySummaries(Season season, Flight flight) {
		List<TwoDaySummary> list = new ArrayList<TwoDaySummary>();
		for (TwoDaySummary summary : this.getTournamentService().getTwoDayRounds(season.getId().intValue())) {
			if (flight == summary.getFlight()) {
				list.add(summary);
			}
		}
		Collections.sort(list, TWO_SUMMARY_COMPARATOR);

		MultiMap standings = MultiValueMap.decorate(new TreeMap<Integer, TwoDaySummary>());
		for (TwoDaySummary summary : list) {
			standings.put(summary.getTotalNet(), summary);
		}
		int standing = 1;
		for (Object object : standings.keySet()) {
			@SuppressWarnings("unchecked")
			Collection<TwoDaySummary> ties = (Collection<TwoDaySummary>) standings.get(object);
			for (TwoDaySummary summary : ties) {
				if (ties.size() == 1) {
					summary.setStanding(Integer.toString(standing));
				} else {
					summary.setStanding("T" + standing);
				}
			}
			standing = standing + ties.size();
		}
		return list;
	}

	public SeasonDao getSeasonDao() {
		return seasonDao;
	}

	public void setSeasonDao(SeasonDao seasonDao) {
		this.seasonDao = seasonDao;
	}

	public PlayerService getPlayerService() {
		return playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public TournamentService getTournamentService() {
		return tournamentService;
	}

	public void setTournamentService(TournamentService tournamentService) {
		this.tournamentService = tournamentService;
	}

	public StatsWriter getWriter() {
		return writer;
	}

	public void setWriter(StatsWriter writer) {
		this.writer = writer;
	}

	public PrizeCalculatorFactory getPrizeCalculatorFactory() {
		return prizeCalculatorFactory;
	}

	public void setPrizeCalculatorFactory(PrizeCalculatorFactory prizeCalculatorFactory) {
		this.prizeCalculatorFactory = prizeCalculatorFactory;
	}

	public PointsCalculatorFactory getPointsCalculatorFactory() {
		return this.pointsCalculatorFactory;
	}

	public void setPointsCalculatorFactory(PointsCalculatorFactory pointsCalculatorFactory) {
		this.pointsCalculatorFactory = pointsCalculatorFactory;
	}

	public HandicapCalculatorFactory getHandicapCalculatorFactory() {
		return handicapCalculatorFactory;
	}

	public void setHandicapCalculatorFactory(HandicapCalculatorFactory handicapCalculatorFactory) {
		this.handicapCalculatorFactory = handicapCalculatorFactory;
	}

}

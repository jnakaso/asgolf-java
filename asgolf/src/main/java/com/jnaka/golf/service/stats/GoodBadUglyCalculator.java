package com.jnaka.golf.service.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.RoundService.Filter;
import com.jnaka.golf.service.stats.GoodBadUglyCalculator.Entry;

/**
 * Good, Bad Ugly Good
 * <ul>
 * <li>low Gross, Player, Date, Course</li>
 * <li>low net, Player, Date, Course</li>
 * </ul>
 * Bad
 * <ul>
 * <li>hi Gross, Player, Date, Course</li>
 * <li>hi net, Player, Date, Course</li>
 * </ul>
 * Ugly
 * <ul>
 * <li>worst hole, player, date, course</li>
 * </ul>
 * 
 * <pre>
 * <entry type="UglyHole">
 * 			<value>11</value>
 * 			<player>Don Chikuma</player>
 * 			<playDate>Mar 16, 2014</playDate>
 * 			<course>Legion Memorial - HOLE 18</course>
 * 		</entry>
 * </pre>
 */
@Component
public class GoodBadUglyCalculator extends AbstractCalculator<Entry> {

	public class Entry {
		public GbuType type;
		public Player player;
		public Integer value;
		public Date date;
		public String course;
	}

	public enum GbuType {
		LOW_GROSS("LowGross") //
		, LOW_NET("LowNet") //
		, HIGH_GROSS("HighGross") //
		, HIGH_NET("HighNet") //
		, UGLY_HOLE("UglyHole");

		private String xmlName;

		private GbuType(String xmlName) {
			this.xmlName = xmlName;
		}

		public String getXmlName() {
			return xmlName;
		}

	}

	private final Log log = LogFactory.getLog(this.getClass());

	@Override
	public List<Entry> getEntries(Season season) {
		List<Entry> entries = new ArrayList<Entry>();
		Collection<Round> rounds = this.lookupRounds(season);
		entries.addAll(this.lookupEntries(rounds, GbuType.LOW_GROSS));
		entries.addAll(this.lookupEntries(rounds, GbuType.LOW_NET));
		entries.addAll(this.lookupEntries(rounds, GbuType.HIGH_GROSS));
		entries.addAll(this.lookupEntries(rounds, GbuType.HIGH_NET));
		entries.addAll(this.findUglyScores(rounds, GbuType.UGLY_HOLE));
		return entries;
	}

	private List<Entry> lookupEntries(Collection<Round> rounds, GbuType type) {
		List<Entry> entries = new ArrayList<Entry>();
		for (Round round : this.filterRounds(rounds, type)) {
			Entry entry = new Entry();
			entry.type = type;
			entry.player = round.getPlayer();
			entry.course = this.getCourseName(round);
			entry.date = round.getTournament().getDate();
			entry.value = this.getValue(round, type);
			entries.add(entry);
		}
		return entries;
	}

	private Integer getValue(Round round, GbuType type) {
		this.getLog().debug(type);
		switch (type) {
		case HIGH_GROSS:
		case LOW_GROSS:
			return round.getTotal();
		case HIGH_NET:
		case LOW_NET:
			return round.getTotalNet().intValue();
		default:
			throw new RuntimeException("Bad Type.");
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<Round> filterRounds(Collection<Round> rounds, GbuType type) {
		this.getLog().info(type);
		switch (type) {
		case LOW_GROSS:
			final int lowGross = this.getRoundService().lowestScore(rounds, Filter.TOTAL).intValue();
			return CollectionUtils.select(rounds, new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					Round round = (Round) arg0;
					return round.getTotal().intValue() == lowGross;
				}
			});
		case LOW_NET:
			final int lowNet = this.getRoundService().lowestScore(rounds, Filter.TOTAL_NET).intValue();
			return CollectionUtils.select(rounds, new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					Round round = (Round) arg0;
					return round.getTotalNet().intValue() == lowNet;
				}
			});
		case HIGH_GROSS:
			final int hiGross = this.getRoundService().highestScore(rounds, Filter.TOTAL).intValue();
			return CollectionUtils.select(rounds, new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					Round round = (Round) arg0;
					return round.getTotal().intValue() == hiGross;
				}
			});
		case HIGH_NET:
			final int hiNet = this.getRoundService().highestScore(rounds, Filter.TOTAL_NET).intValue();
			return CollectionUtils.select(rounds, new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					Round round = (Round) arg0;
					return round.getTotalNet().intValue() == hiNet;
				}
			});
		default:
			throw new RuntimeException("Bad Type.");
		}
	}

	private List<Entry> findUglyScores(Collection<Round> rounds, GbuType type) {
		class Ugly {
			Round round;
			int hole;
			int score;

			public Ugly(Round round, int hole, int score) {
				super();
				this.round = round;
				this.hole = hole;
				this.score = score;
			}
		}
		Set<Ugly> uglies = new HashSet<Ugly>();
		int worst = 0;
		for (Round round : rounds) {
			if (round.getScores().length == 18) {
				for (int i = 0; i < 18; i++) {
					Integer score = round.getScores()[i];
					if (score > worst) {
						uglies.clear();
						uglies.add(new Ugly(round, i + 1, score));
						worst = score;
					} else if (score == worst) {
						uglies.add(new Ugly(round, i + 1, score));
					}
				}
			} else {
				this.getLog().debug(String.format("No scores %d", round.getId()));
			}
		}

		List<Entry> entries = new ArrayList<Entry>();
		for (Ugly ugly : uglies) {
			Entry entry = new Entry();
			entry.type = GbuType.UGLY_HOLE;
			entry.player = ugly.round.getPlayer();
			entry.course = String.format("%s - HOLE %d", this.getCourseName(ugly.round), ugly.hole);
			entry.date = ugly.round.getTournament().getDate();
			entry.value = ugly.score;
			entries.add(entry);
		}
		return entries;
	}

	protected String getCourseName(Round round) {
		return round.getTournament().getCourse().getName();
	}

	private Collection<Round> lookupRounds(Season season) {
		List<Round> allRounds = this.getTournamentService().findRoundsBySeason(season.getId());
		CollectionUtils.filter(allRounds, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return ((Round) object).getFlight() != null;
			}
		});
		return allRounds;
	}

	private Log getLog() {
		return this.log;
	}
}

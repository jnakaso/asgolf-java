package com.jnaka.golf.service.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.service.stats.AbstractPuttingKpCalculator.Entry;

/**
 * @author nakasones
 * 
 * <pre>
 * 		<player player="Bill Tadehara" active="true">
 * 			<kp playDate="Jun 3, 2012" course="Classic" hole="15" score="4"/>
 * 			<kp playDate="Mar 4, 2012" course="North Shore" hole="13" score="4"/>
 * 			<kp playDate="Jun 4, 2011" course="Classic" hole="15" score="4"/>
 * 		</player>
 * </pre>
 */
abstract public class AbstractPuttingKpCalculator extends AbstractCalculator<Entry> {

	public static class Entry {
		public Player player;
		public Kp kp;
		public Round round;
		public Integer score;
		public Date playDate;
	}

	static Comparator<Player> PLAYER_COMPARATOR = new Comparator<Player>() {

		@Override
		public int compare(Player o1, Player o2) {
			return o1.getLastName().compareTo(o2.getLastName());
		}
	};

	@Autowired
	private SeasonService seasonService;

	abstract protected boolean scoreTest(Integer score);

	@Override
	public List<Entry> getEntries(Season season) {
		List<Entry> rows = new ArrayList<Entry>();
		List<Tournament> tournaments = this.getTournamentService().findBySeason(season.getId().intValue());
		Collections.reverse(tournaments);

		for (Tournament tournament : tournaments) {
			for (Kp kp : tournament.getKps()) {
				Optional<Round> round = this.findRound(tournament, kp);
				round.ifPresent(r -> {
					this.findScore(r, kp).ifPresent(s -> {
						rows.add(this.createEntry(tournament, kp, r, s));
					});
				});
			}
		}

		return rows;
	}

	private Entry createEntry(Tournament tournament, Kp kp, Round round, Integer score) {
		Entry entry = new Entry();
		entry.player = kp.getPlayer();
		entry.kp = kp;
		entry.round = round;
		entry.score = score;
		entry.playDate = tournament.getDate();
		return entry;
	}

	private Optional<Round> findRound(Tournament tournament, Kp kp) {
		return tournament.getRounds().stream() //
				.filter(r -> r.getPlayer() == kp.getPlayer()) //
				.findFirst();
	}


	private Optional<Integer> findScore(Round round, Kp kp) {
		try {
			int hole = Integer.valueOf(kp.getHole()) - 1;
			Integer score = round.getScores()[hole];
			return this.scoreTest(score) ? Optional.of(score) : Optional.empty();
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	SeasonService getSeasonService() {
		return seasonService;
	}

	void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}

}

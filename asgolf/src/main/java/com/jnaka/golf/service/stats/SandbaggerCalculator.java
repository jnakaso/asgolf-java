package com.jnaka.golf.service.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.RoundService.Filter;
import com.jnaka.golf.service.stats.SandbaggerCalculator.Entry;

@Component
public class SandbaggerCalculator extends AbstractCalculator<Entry> {
	public static class Entry {
		final public Player player;
		final public Integer count;
		final public Integer high;
		final public Integer low;
		final public Integer range;

		public Entry(Player player, Integer count, Integer high, Integer low, Integer range) {
			super();
			this.player = player;
			this.count = count;
			this.high = high;
			this.low = low;
			this.range = range;
		}
	}

	static Comparator<Entry> SANDBAGGER_COMP = new Comparator<Entry>() {
		@Override
		public int compare(Entry o1, Entry o2) {
			// descending
			return o2.range.compareTo(o1.range);
		}
	};

	public List<Entry> getEntries(Season season) {
		List<Entry> rows = new ArrayList<Entry>();
		for (Player player : this.lookupPlayers()) {
			List<Round> rounds = this.getTournamentService().findRoundsBySeason(season.getId(), player);
			CollectionUtils.filter(rounds, new Predicate() {
				@Override
				public boolean evaluate(Object object) {
					Round round = (Round) object;
					return round.getFlight() != null;
				}
			});
			if (!rounds.isEmpty()) {
				Integer count = rounds.size();
				Float high = this.getRoundService().highestScore(rounds, Filter.TOTAL_NET);
				Float low = this.getRoundService().lowestScore(rounds, Filter.TOTAL_NET);
				Float range = Float.valueOf(high.floatValue() - low.floatValue());
				rows.add(new Entry(player, count, high.intValue(), low.intValue(), range.intValue()));
			}
		}
		Collections.sort(rows, SANDBAGGER_COMP);
		return rows;
	}

}

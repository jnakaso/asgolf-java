package com.jnaka.golf.service.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.RoundService;
import com.jnaka.golf.service.stats.MostImprovedCalculator.Entry;

@Component
public class MostImprovedCalculator extends AbstractCalculator<Entry> {

	public static class Entry {
		final public Player player;
		final public Integer count;
		final public Float change;
		final public Float percentChange;
		final public Float start;
		final public Float end;

		public Entry(Player player, Integer count, Float change, Float percentChange, Float start, Float end) {
			super();
			this.player = player;
			this.count = count;
			this.change = change;
			this.percentChange = percentChange;
			this.start = start;
			this.end = end;
		}

	}

	static Comparator<Entry> MOST_IMPROVED_COMP = new Comparator<Entry>() {
		@Override
		public int compare(Entry o1, Entry o2) {
			if (o1.percentChange == null) {
				return -1;
			}
			if (o2.percentChange == null) {
				return 1;
			}
			return o2.percentChange.compareTo(o1.percentChange);
		}
	};

	@SuppressWarnings("unchecked")
	public List<Entry> getEntries(Season season) {
		List<Entry> rows = new ArrayList<Entry>();
		for (Player player : this.lookupPlayers()) {
			List<Round> rounds = this.getTournamentService().findRoundsBySeason(season.getId(), player);
			rounds = new ArrayList<Round>(CollectionUtils.select(rounds, RoundService.HAS_FLIGHT_PREDICATE));
			Collections.sort(rounds, RoundService.DATE_COMPARATOR);
			if (!rounds.isEmpty()) {
				int count = rounds.size();
				Float start = rounds.get(rounds.size() - 1).getHandicap().floatValue();
				Float end = rounds.get(0).getHandicap().floatValue();
				Float change = start - end;
				Float percentChange = start.intValue() == 0 ? null : change / start;

				rows.add(new Entry(player, count, change, percentChange, start, end));
			}
		}
		Collections.sort(rows, MOST_IMPROVED_COMP);
		return rows;
	}

}

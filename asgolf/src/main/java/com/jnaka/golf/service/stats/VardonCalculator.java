package com.jnaka.golf.service.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.RoundService;
import com.jnaka.golf.service.stats.VardonCalculator.Entry;

@Component
public class VardonCalculator extends AbstractCalculator<Entry> {
	public static class Entry {
		final public Player player;
		final public Integer count;
		final public Float average;

		public Entry(Player player, int count, Float average) {
			this.player = player;
			this.count = count;
			this.average = average;
		}
	}

	static Comparator<Entry> AVERAGE_COMP = new Comparator<Entry>() {
		@Override
		public int compare(Entry o1, Entry o2) {
			// descending
			return o1.average.compareTo(o2.average);
		}
	};

	public List<Entry> getEntries(Season season) {
		List<Entry> entries = new ArrayList<Entry>();
		for (Player player : this.lookupPlayers()) {
			List<Round> rounds = this.getTournamentService().findRoundsBySeason(season.getId(), player);
			if (!rounds.isEmpty()) {
				int count = rounds.size();
				Float average = this.getRoundService().average(rounds, RoundService.Filter.TOTAL_NET);
				entries.add(new Entry(player, count, average));
			}
		}
		Collections.sort(entries, AVERAGE_COMP);
		return entries;
	}

}

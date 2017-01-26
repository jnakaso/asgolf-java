package com.jnaka.golf.service.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.service.stats.DrJekyllCalculator.Entry;

/**
 * @author nakasones
 * 
 *         <pre>
 * 	<entry player="" playDate="Jun 3, 2012" course="Classic" hdcp="15" front="4" back=""/>
 * </pre>
 */
@Component
public class DrJekyllCalculator extends AbstractCalculator<Entry> {

	public static class Entry {
		public Player player;
		public Round round;
		public Date playDate;
		public Integer split;
	}

	private static final int JEKYLL_DEPTH = 5;

	private static final Comparator<Integer> COMP = new Comparator<Integer>() {

		@Override
		public int compare(Integer o1, Integer o2) {
			//want reverse
			return o2.compareTo(o1);
		}
	};

	@Autowired
	private SeasonService seasonService;

	@Override
	public List<Entry> getEntries(Season season) {
		List<Entry> rows = new ArrayList<Entry>();

		Map<Integer, List<Entry>> map = new TreeMap<Integer, List<Entry>>(COMP);

		List<Tournament> tournaments = this.getTournamentService().findBySeason(season.getId().intValue());
		Collections.reverse(tournaments);

		for (Tournament tournament : tournaments) {
			for (Round round : tournament.getRounds()) {
				Entry entry = this.createEntry(round);
				List<Entry> entries = map.get(Math.abs(entry.split));
				if (entries == null) {
					entries = new ArrayList<Entry>();
					map.put(Math.abs(entry.split), entries);
				}
				if (entries != null) {
					entries.add(entry);
				}
			}
		}

		Iterator<List<Entry>> iterator = map.values().iterator();
		while (iterator.hasNext() && rows.size() < JEKYLL_DEPTH) {
			rows.addAll(iterator.next());
		}

		return rows;
	}

	private Entry createEntry(Round round) {
		Entry entry = new Entry();
		entry.round = round;
		entry.playDate = round.getTournament().getDate();
		entry.player = round.getPlayer();
		entry.split = round.getFront() - round.getBack();
		return entry;
	}

	public SeasonService getSeasonService() {
		return seasonService;
	}

	public void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}

}

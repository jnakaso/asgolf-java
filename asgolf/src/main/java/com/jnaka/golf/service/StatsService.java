package com.jnaka.golf.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Season;

public interface StatsService {

	class WherePlayedEntry {
		final public Integer tourId;
		final public Date date;
		final public String course;
		final public float average;

		public WherePlayedEntry(Integer tourId, Date date, String course, float average) {
			super();
			this.tourId = tourId;
			this.date = date;
			this.course = course;
			this.average = average;
		}
	}

	class BirdieEntry {
		final public Player player;
		final Map<Integer, Integer> adjusted;
		final Map<Integer, Integer> counts;

		public BirdieEntry(Player player, Map<Integer, Integer> adjusted, Map<Integer, Integer> counts) {
			super();
			this.player = player;
			this.adjusted = adjusted;
			this.counts = counts;
		}
	}

	List<WherePlayedEntry> getWherePlayedEntries(Season season);

	// List<BirdieEntry> getBirdieEntries(Season season);

}

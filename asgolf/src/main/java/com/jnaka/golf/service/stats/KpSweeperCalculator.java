package com.jnaka.golf.service.stats;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.service.stats.KpSweeperCalculator.Entry;

@Component
public class KpSweeperCalculator extends AbstractCalculator<Entry> {

	public static class Entry {
		final public Player player;
		final public Date playDate;
		final public String course;

		public Entry(Player player, Date playDate, String course) {
			super();
			this.player = player;
			this.playDate = playDate;
			this.course = course;
		}

	}

	private static Transformer transformer = new Transformer() {
		@Override
		public Object transform(Object input) {
			Round round = (Round) input;
			Player player = round.getPlayer();
			Date playDate = round.getTournament().getDate();
			String course = round.getTournament().getCourse().getName();
			return new Entry(player, playDate, course);
		}
	};
	
	@Autowired
	private SeasonService seasonService;

	@SuppressWarnings("unchecked")
	public List<Entry> getEntries(Season season) {
		List<Entry> entries = new ArrayList<Entry>();
		for (Season aSeason : this.getSeasonService().getAll()) {
			List<Round> rounds = this.getTournamentService().findKpSweepers(aSeason.getId().intValue());
			entries.addAll(CollectionUtils.collect(rounds, transformer));
		}
		return entries;
	}

	public SeasonService getSeasonService() {
		return seasonService;
	}

	public void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}

}

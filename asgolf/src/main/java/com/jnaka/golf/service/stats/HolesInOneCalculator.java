package com.jnaka.golf.service.stats;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.service.stats.HolesInOneCalculator.Entry;

@Component
public class HolesInOneCalculator extends AbstractCalculator<Entry> {

	public static class Entry {
		public Player player;
		public Date playDate;
		public String course;
		public Integer hole;
		protected Integer score;
	}

	@Autowired
	private SeasonService seasonService;

	public List<Entry> getEntries(Season season) {
		List<Entry> entries = new ArrayList<Entry>();
		for (Season aSeason : this.getSeasonService().getAll()) {
			for (Round round : this.getTournamentService().findRoundsBySeason(aSeason.getId().intValue())) {
				if (round.getScores().length == 18) {
					for (int i = 0; i < 18; i++) {
						if (round.getScores()[i] == 1) {
							Entry entry = new Entry();
							entry.player = round.getPlayer();
							entry.playDate = round.getTournament().getDate();
							entry.course = ReportHelper.getCourseName(round);
							entry.hole = Integer.valueOf(i + 1);
							entries.add(entry);
						}
					}
				}
			}
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

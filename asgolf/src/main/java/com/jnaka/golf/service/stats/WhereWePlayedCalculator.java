package com.jnaka.golf.service.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.stats.WhereWePlayedCalculator.Entry;

@Component
public class WhereWePlayedCalculator extends AbstractCalculator<Entry> {

	public class Entry {
		final public Integer tourId;
		final public Date date;
		final public String course;
		final public float average;

		public Entry(Integer tourId, Date date, String course, float average) {
			super();
			this.tourId = tourId;
			this.date = date;
			this.course = course;
			this.average = average;
		}
	}


	@SuppressWarnings("unchecked")
	public List<Entry> getEntries(Season season) {
		List<Tournament> tournaments = this.getTournamentService().getAll();
		Collections.sort(tournaments, new Comparator<Tournament>() {
			@Override
			public int compare(Tournament o1, Tournament o2) {
				int value = o1.getCourse().getName().compareTo(o2.getCourse().getName());
				if (value == 0) {
					// date desc
					value = o2.getDate().compareTo(o1.getDate());
				}
				return value;
			}
		});
		return new ArrayList<Entry>(CollectionUtils.collect(tournaments, new Transformer() {
			@Override
			public Object transform(Object arg0) {
				Tournament tournament = (Tournament) arg0;
				Integer tourId = tournament.getId();
				Date tourDate = tournament.getDate();
				String courseName = tournament.getCourse().getName();
				float average = getTournamentService().averageNet(tournament);
				return new Entry(tourId, tourDate, courseName, average);
			}
		}));
	}

}

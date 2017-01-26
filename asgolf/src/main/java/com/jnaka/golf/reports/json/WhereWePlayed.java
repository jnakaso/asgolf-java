package com.jnaka.golf.reports.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.stats.StatsCalculator;
import com.jnaka.golf.service.stats.WhereWePlayedCalculator;

@Component("jsonWhereWePlayed")
public class WhereWePlayed extends AbstractJsonReport<Season, WhereWePlayedCalculator.Entry> {
	private static final String JSON_DATE = "date";
	private static final String JSON_COURSE = "course";
	private static final String JSON_AVG = "average";
	private static final String JSON_ID_TOURNAMENT = "tournamentId";

	public Object create(Season season) {
		List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
		List<WhereWePlayedCalculator.Entry> entries = this.getCalculator().getEntries(season);
		CollectionUtils.collect(entries, new Transformer() {
			@Override
			public Object transform(Object input) {
				Map<String, String> map = new HashMap<String, String>();
				WhereWePlayedCalculator.Entry entry = (WhereWePlayedCalculator.Entry) input;
				map.put(JSON_DATE, convert(entry.date));
				map.put(JSON_COURSE, entry.course);
				map.put(JSON_AVG, convert(entry.average));
				map.put(JSON_ID_TOURNAMENT, entry.tourId.toString());
				return map;
			}
		}, objects);
		return objects;
	}
	
	@Autowired
	@Qualifier("whereWePlayedCalculator")
	public void setCalculator(StatsCalculator<WhereWePlayedCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}

}

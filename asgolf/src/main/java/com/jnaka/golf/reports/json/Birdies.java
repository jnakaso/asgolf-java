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
import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.stats.BirdiesCalculator;
import com.jnaka.golf.service.stats.BirdiesCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

@Component("jsonBirdies")
public class Birdies extends AbstractJsonReport<Season, Entry> {

	private static final String JSON_NAME = "name";
	private static final String JSON_ROUNDS = "rounds";
	private static final String JSON_SCORES = "scores";
	private static final String JSON_ADJUSTED = "adjusted";

	public Object create(Season season) {
		List<Map<String, Object>> objects = new ArrayList<Map<String, Object>>();
		List<Entry> entries = this.getCalculator().getEntries(season);
		CollectionUtils.collect(entries, new Transformer() {
			@Override
			public Object transform(Object input) {
				Map<String, Object> map = new HashMap<String, Object>();
				Entry entry = (Entry) input;
				map.put(JSON_NAME, ReportHelper.getPlayerName(entry.player));
				map.put(JSON_ROUNDS, entry.count.toString());
				map.put(JSON_SCORES, entry.scores);
				map.put(JSON_ADJUSTED, entry.adjusted);
				return map;
			}
		}, objects);
		return objects;
	}
	
	@Autowired
	@Qualifier("birdiesCalculator")
	public void setCalculator(StatsCalculator<BirdiesCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}

}

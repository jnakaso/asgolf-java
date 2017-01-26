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
import com.jnaka.golf.service.stats.SandbaggerCalculator;
import com.jnaka.golf.service.stats.SandbaggerCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

@Component("jsonSandbagger")
public class Sandbagger extends AbstractJsonReport<Season, SandbaggerCalculator.Entry> {

	private static final String JSON_NAME = "name";
	private static final String JSON_HI = "hi";
	private static final String JSON_LO = "lo";
	private static final String JSON_RANGE = "range";
	private static final String JSON_ROUNDS = "rounds";

	public Object create(Season season) {
		List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
		List<Entry> entries = this.getCalculator().getEntries(season);
		CollectionUtils.collect(entries, new Transformer() {
			@Override
			public Object transform(Object input) {
				Map<String, String> map = new HashMap<String, String>();
				Entry entry = (Entry) input;
				map.put(JSON_NAME, ReportHelper.getPlayerName(entry.player));
				map.put(JSON_ROUNDS, entry.count.toString());
				map.put(JSON_LO, entry.low.toString());
				map.put(JSON_HI, entry.high.toString());
				map.put(JSON_RANGE, entry.range.toString());
				return map;
			}
		}, objects);
		return objects;
	}
	
	@Autowired
	@Qualifier("sandbaggerCalculator")
	public void setCalculator(StatsCalculator<SandbaggerCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}

}

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
import com.jnaka.golf.service.stats.MostImprovedCalculator;
import com.jnaka.golf.service.stats.StatsCalculator;

@Component("jsonMostImproved")
public class MostImproved extends AbstractJsonReport<Season, MostImprovedCalculator.Entry> {

	private static final String JSON_NAME = "name";
	private static final String JSON_COUNT = "count";
	private static final String JSON_CHANGE = "change";
	private static final String JSON_PERCENT_CHANGE = "percentChange";

	public Object create(Season season) {
		List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
		List<MostImprovedCalculator.Entry> entries = this.getCalculator().getEntries(season);
		CollectionUtils.collect(entries, new Transformer() {
			@Override
			public Object transform(Object input) {
				Map<String, String> map = new HashMap<String, String>();
				MostImprovedCalculator.Entry entry = (MostImprovedCalculator.Entry) input;
				map.put(JSON_NAME, ReportHelper.getPlayerName(entry.player));
				map.put(JSON_COUNT, entry.count.toString());
				map.put(JSON_CHANGE, convert(entry.change));
				map.put(JSON_PERCENT_CHANGE, convert(entry.percentChange));
				return map;
			}
		}, objects);
		return objects;
	}
	
	@Autowired
	@Qualifier("mostImprovedCalculator")
	public void setCalculator(StatsCalculator<MostImprovedCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}

}

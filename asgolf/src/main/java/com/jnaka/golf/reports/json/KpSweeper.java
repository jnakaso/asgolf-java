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
import com.jnaka.golf.service.stats.KpSweeperCalculator;
import com.jnaka.golf.service.stats.KpSweeperCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

@Component("jsonKpSweeper")
public class KpSweeper extends AbstractJsonReport<Season, KpSweeperCalculator.Entry> {

	private static final String JSON_NAME = "name";
	private static final String JSON_DATE = "playDate";
	private static final String JSON_COURSE = "course";

	public Object create(Season season) {
		List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
		List<Entry> entries = this.getCalculator().getEntries(season);
		CollectionUtils.collect(entries, new Transformer() {
			@Override
			public Object transform(Object input) {
				Map<String, String> map = new HashMap<String, String>();
				Entry entry = (Entry) input;
				map.put(JSON_NAME, ReportHelper.getPlayerName(entry.player));
				map.put(JSON_DATE, convert(entry.playDate));
				map.put(JSON_COURSE, entry.course);
				return map;
			}
		}, objects);
		return objects;
	}
	
	@Autowired
	@Qualifier("kpSweeperCalculator")
	public void setCalculator(StatsCalculator<KpSweeperCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}

}

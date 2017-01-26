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
import com.jnaka.golf.service.stats.HolesInOneCalculator;
import com.jnaka.golf.service.stats.StatsCalculator;
import com.jnaka.golf.service.stats.HolesInOneCalculator.Entry;

@Component("jsonHolesInOne")
public class HolesInOne extends AbstractJsonReport<Season, HolesInOneCalculator.Entry> {
	private static final String JSON_HOLE = "hole";
	private static final String JSON_NAME = "player";
	private static final String JSON_PLAY_DATE = "playDate";
	private static final String JSON_COURSE = "course";

	@Override
	public Object create(Season root) {
		List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
		List<Entry> entries = this.getCalculator().getEntries(null);
		CollectionUtils.collect(entries, new Transformer() {
			@Override
			public Object transform(Object input) {
				Map<String, String> map = new HashMap<String, String>();
				Entry entry = (Entry) input;
				map.put(JSON_NAME, ReportHelper.getPlayerName(entry.player));
				map.put(JSON_PLAY_DATE, convert(entry.playDate));
				map.put(JSON_COURSE, entry.course);
				map.put(JSON_HOLE, Integer.toString(entry.hole));
				return map;
			}
		}, objects);
		return objects;
	}
	
	@Autowired
	@Qualifier("holesInOneCalculator")
	public void setCalculator(StatsCalculator<HolesInOneCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}

}

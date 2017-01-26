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
import com.jnaka.golf.service.stats.StatsCalculator;
import com.jnaka.golf.service.stats.VardonCalculator;
import com.jnaka.golf.service.stats.VardonCalculator.Entry;

@Component("jsonVardon")
public class Vardon extends AbstractJsonReport<Season, VardonCalculator.Entry> {

	private static final String JSON_NAME = "name";
	private static final String JSON_AVERAGE = "average";
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
				map.put(JSON_AVERAGE, convert(entry.average));
				map.put(JSON_ROUNDS, entry.count.toString());
				return map;
			}
		}, objects);
		return objects;
	}

	@Autowired
	@Qualifier("vardonCalculator")
	public void setCalculator(StatsCalculator<VardonCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}
}

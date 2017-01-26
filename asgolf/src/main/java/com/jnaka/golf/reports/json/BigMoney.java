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
import com.jnaka.golf.service.stats.MoneyPlayerCalculator;
import com.jnaka.golf.service.stats.StatsCalculator;
import com.jnaka.golf.service.stats.MoneyPlayerCalculator.Entry;

@Component("jsonBigMoney")
public class BigMoney extends AbstractJsonReport<Season, MoneyPlayerCalculator.Entry> {

	private static final String JSON_PLAYER_ID = "playerId";
	private static final String JSON_PLAYER_ACTIVE = "active";
	private static final String JSON_PLAYER_NAME = "playerName";
	private static final String JSON_TOURNAMENTS = "tournaments";
	private static final String JSON_POINTS = "points";
	private static final String JSON_EARNINGS = "earnings";
	private static final String JSON_FIRST = "first";
	private static final String JSON_SECOND = "second";
	private static final String JSON_THIRD = "third";

	public Object create(Season season) {
		List<Map<String, String>> objects = new ArrayList<Map<String, String>>();
		List<Entry> entries = this.getCalculator().getEntries(season);
		CollectionUtils.collect(entries, new Transformer() {
			@Override
			public Object transform(Object input) {
				Map<String, String> map = new HashMap<String, String>();
				Entry entry = (Entry) input;
				map.put(JSON_PLAYER_ID, entry.getPlayer().getId().toString());
				map.put(JSON_PLAYER_NAME, ReportHelper.getPlayerName(entry.getPlayer()));
				map.put(JSON_PLAYER_ACTIVE, entry.getPlayer().getActive().toString());
				map.put(JSON_TOURNAMENTS, entry.getTournaments().toString());
				map.put(JSON_POINTS, entry.getPoints().toString());
				map.put(JSON_EARNINGS, entry.getEarnings().toString());
				map.put(JSON_FIRST, entry.getFinishes()[0].toString());
				map.put(JSON_SECOND, entry.getFinishes()[1].toString());
				map.put(JSON_THIRD, entry.getFinishes()[2].toString());
				return map;
			}
		}, objects);
		return objects;
	}

	@Autowired
	@Qualifier("moneyPlayerCalculator")
	public void setCalculator(StatsCalculator<MoneyPlayerCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}
}

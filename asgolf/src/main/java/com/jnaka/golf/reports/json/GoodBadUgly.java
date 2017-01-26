package com.jnaka.golf.reports.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.stats.GoodBadUglyCalculator;
import com.jnaka.golf.service.stats.GoodBadUglyCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

/*
 * 
 * {
 * 	lowGross: {
 *   score : 78,
 *   rounds : [
 *   ]
 * },
 * lowNet: {
 *   score : 64,
 *   rounds : [
 *     player:,
 *     course:,
 *     date:
 *   ]
 * },
 * uglyHole: {
 *   score :13,
 *   rounds : [ {
 *     player:,
 *     course:,
 *     date:,
 *     hole:
 *   }]
 * }
 * 
 * []
 * }
 * 
 */
@Component("jsonGoodBadUgly")
public class GoodBadUgly extends AbstractJsonReport<Season, GoodBadUglyCalculator.Entry> {

	private static final String JSON_SCORE = "score";
	private static final String JSON_ROUNDS = "rounds";
	private static final String JSON_PLAYER = "player";
	private static final String JSON_PLAY_DATE = "playDate";
	private static final String JSON_COURSE = "course";

	public Object create(Season season) {
		Map<String, Map<String, Object>> objects = new HashMap<String, Map<String, Object>>();
		for (GoodBadUglyCalculator.GbuType type : GoodBadUglyCalculator.GbuType.values()) {
			HashMap<String, Object> inner = new HashMap<String, Object>();
			inner.put(JSON_SCORE, 0);
			inner.put(JSON_ROUNDS, new ArrayList<Map<String, Object>>());
			objects.put(type.getXmlName(), inner);
		}

		List<Entry> entries = this.getCalculator().getEntries(season);
		for (Entry entry : entries) {
			Map<String, Object> entryMap = objects.get(entry.type.getXmlName());
			entryMap.put(JSON_SCORE, entry.value);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> rounds = (List<Map<String, Object>>) entryMap.get(JSON_ROUNDS);
			rounds.add(this.createRound(entry));
		}
		return objects;
	}

	private Map<String, Object> createRound(Entry entry) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(JSON_PLAYER, ReportHelper.getPlayerName(entry.player));
		map.put(JSON_COURSE, entry.course);
		map.put(JSON_PLAY_DATE, convert(entry.date));
		return map;
	}

	@Autowired
	@Qualifier("goodBadUglyCalculator")
	public void setCalculator(StatsCalculator<GoodBadUglyCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}

}

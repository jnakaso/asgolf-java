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
import com.jnaka.golf.service.stats.StandingsCalculator;
import com.jnaka.golf.service.stats.StandingsCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

@Component("jsonStandings")
public class Standings extends AbstractJsonReport<Season, Entry> {

	private static final String JSON_ID_PLAYER = "playerId";
	private static final String JSON_NAME = "name";
	private static final String JSON_EARNINGS = "earnings";
	private static final String JSON_POINTS = "points";
	private static final String JSON_ATTENDANCE = "attendance";
	private static final String JSON_KPS = "kps";
	private static final String JSON_HDCP = "hdcp";
	private static final String JSON_POINTS_BY_T = "pointsByTour";
	private static final String JSON_KPS_BY_T = "kpsByTour";
	private static final String JSON_EARNINGS_BY_T = "earningsByTour";

	@Override
	public Object create(Season season) {
		List<Map<String, Object>> objects = new ArrayList<Map<String, Object>>();
		List<Entry> entries = this.getCalculator().getEntries(season);
		CollectionUtils.collect(entries, new Transformer() {
			@Override
			public Object transform(Object input) {
				Map<String, Object> map = new HashMap<String, Object>();
				Entry entry = (Entry) input;
				map.put(JSON_NAME, ReportHelper.getPlayerName(entry.player));
				map.put(JSON_ID_PLAYER, entry.player.getId());
				map.put(JSON_EARNINGS, entry.earnings);
				map.put(JSON_POINTS, entry.points);
				map.put(JSON_ATTENDANCE, entry.attendance);
				map.put(JSON_KPS, entry.kps);
				map.put(JSON_HDCP, entry.hdcp);
				map.put(JSON_POINTS_BY_T, entry.pointsByTournament);
				map.put(JSON_KPS_BY_T, entry.kpsByTournament);
				map.put(JSON_EARNINGS_BY_T, entry.earningsByTournament);
				return map;
			}
		}, objects);
		return objects;
	}

	@Autowired
	@Qualifier("standingsCalculator")
	public void setCalculator(StatsCalculator<StandingsCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}
}

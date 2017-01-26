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
import com.jnaka.golf.service.stats.DrJekyllCalculator;
import com.jnaka.golf.service.stats.DrJekyllCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

/**
 * @author nakasones
 * 
 *         <pre>
 * <player player="Bill Tadehara" active="true">
 *  			<kp playDate="Jun 3, 2012" course="Classic" hole="15" score="4"/>
 *  			<kp playDate="Mar 4, 2012" course="North Shore" hole="13" score="4"/>
 *  			<kp playDate="Jun 4, 2011" course="Classic" hole="15" score="4"/>
 *  		</player>
 * </pre>
 */
@Component("jsonDrJekyll")
public class DrJekyll extends AbstractJsonReport<Season, DrJekyllCalculator.Entry> {

	private static final String JSON_ID = "id";
	private static final String JSON_NAME = "name";
	private static final String JSON_PLAY_DATE = "playDate";
	private static final String JSON_COURSE = "course";
	private static final String JSON_FRONT = "front";
	private static final String JSON_BACK = "back";
	private static final String JSON_SPLIT = "split";

	public Object create(Season season) {
		List<Map<String, Object>> objects = new ArrayList<Map<String, Object>>();
		List<Entry> entries = this.getCalculator().getEntries(season);
		CollectionUtils.collect(entries, new Transformer() {
			@Override
			public Object transform(Object input) {
				Map<String, Object> map = new HashMap<String, Object>();
				Entry entry = (Entry) input;
				map.put(JSON_ID, entry.player.getId());
				map.put(JSON_NAME, ReportHelper.getPlayerName(entry.player));
				map.put(JSON_PLAY_DATE, convert(entry.playDate));
				map.put(JSON_COURSE, ReportHelper.getCourseName(entry.round));
				map.put(JSON_FRONT, entry.round.getFront());
				map.put(JSON_BACK, entry.round.getBack());
				map.put(JSON_SPLIT, entry.split);
				return map;
			}
		}, objects);
		return objects;
	}

	@Autowired
	@Qualifier("drJekyllCalculator")
	public void setCalculator(StatsCalculator<DrJekyllCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}

}

package com.jnaka.golf.reports.json;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.stats.AbstractPuttingKpCalculator;
import com.jnaka.golf.service.stats.AbstractPuttingKpCalculator.Entry;

/**
 * @author nakasones
 * 
 *         <pre>
 * <player player="Bill Tadehara" active="true">
 *  			<kp playDate="Jun 3, 2012" course="Classic" hole="15" score=
"4"/>
 *  			<kp playDate="Mar 4, 2012" course="North Shore" hole="13" score=
"4"/>
 *  			<kp playDate="Jun 4, 2011" course="Classic" hole="15" score=
"4"/>
 *  		</player>
 *         </pre>
 */
abstract public class AbstractPuttingReport extends AbstractJsonReport<Season, AbstractPuttingKpCalculator.Entry> {

	private static final String JSON_ID = "id";
	private static final String JSON_NAME = "name";
	private static final String JSON_ACTIVE = "active";
	private static final String JSON_PLAY_DATE = "playDate";
	private static final String JSON_COURSE = "course";
	private static final String JSON_HOLE = "hole";
	private static final String JSON_SCORE = "score";

	public Object create(Season season) {
		return this.getCalculator().getEntries(season) //
				.stream() //
				.map(e -> createEntry(e)) //
				.collect(Collectors.toList());
	}

	protected Map<String, Object> createEntry(Entry entry) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(JSON_ID, entry.player.getId());
		map.put(JSON_NAME, ReportHelper.getPlayerName(entry.player));
		map.put(JSON_ACTIVE, entry.player.getActive());
		map.put(JSON_PLAY_DATE, convert(entry.playDate));
		map.put(JSON_COURSE, ReportHelper.getCourseName(entry.round));
		map.put(JSON_HOLE, entry.kp.getHole());
		map.put(JSON_SCORE, entry.score);
		return map;
	}

}

package com.jnaka.golf.reports.xml;

import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.stats.StatsCalculator;
import com.jnaka.golf.service.stats.AbstractPuttingKpCalculator.Entry;

@Component
public class ThreePutt extends AbstractStatFactory<Entry> {

	private static final String XML_ROOT = "ThreePutt";
	private static final String XML_ELEMENT = "player";
	private static final String XML_PLAYER_ID = "playerId";
	private static final String XML_NAME = "name";
	private static final String XML_PLAY_DATE = "playDate";
	private static final String XML_COURSE = "course";
	private static final String XML_SCORE = "score";
	private static final String XML_ACTIVE = "active";
	private static final String XML_HOLE = "hole";

	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}

	@Override
	protected void updateDocument() {
		final Element root = getDocument().getRootElement();
		Season season = new Season();
		season.setId(this.getSeasonID());
		List<Entry> entries = this.getCalculator().getEntries(season);
		CollectionUtils.forAllDo(entries, new Closure() {
			@Override
			public void execute(Object input) {
				Entry entry = (Entry) input;
				Element element = root.addElement(XML_ELEMENT);
				element.addAttribute(XML_PLAYER_ID, entry.player.getId().toString());
				element.addAttribute(XML_NAME, ReportHelper.getPlayerName(entry.player));
				element.addAttribute(XML_ACTIVE, entry.player.getActive().toString());
				element.addAttribute(XML_PLAY_DATE, convert(entry.playDate));
				element.addAttribute(XML_COURSE, ReportHelper.getCourseName(entry.round));
				element.addAttribute(XML_HOLE, entry.kp.getHole());
				element.addAttribute(XML_SCORE, entry.score.toString());
			}
		});
	}

	@Override
	@Autowired
	@Qualifier("threePuttCalculator")
	public void setCalculator(StatsCalculator<Entry> calculator) {
		super.setCalculator(calculator);
	}

}

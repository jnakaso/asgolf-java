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
import com.jnaka.golf.service.stats.GoodBadUglyCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

/**
 * Good, Bad Ugly Good
 * <ul>
 * <li>low Gross, Player, Date, Course</li>
 * <li>low net, Player, Date, Course</li>
 * </ul>
 * Bad
 * <ul>
 * <li>hi Gross, Player, Date, Course</li>
 * <li>hi net, Player, Date, Course</li>
 * </ul>
 * Ugly
 * <ul>
 * <li>worst hole, player, date, course</li>
 * </ul>
 * 
 */
@Component
public class GoodBadUgly extends AbstractStatFactory<Entry> {
	private static final String XML_ROOT = "GoodBadUgly";
	private static final String XML_ELEMENT = "entry";
	private static final String XML_TYPE = "type";
	private static final String XML_VALUE = "value";
	private static final String XML_PLAYER = "player";
	private static final String XML_PLAY_DATE = "playDate";
	private static final String XML_COURSE = "course";

	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}

	@Override
	protected void updateDocument() {
		final Element root = getDocument().getRootElement();
		Season season = new Season();
		season.setId(this.getSeasonID());
		List<Entry> rows = this.getCalculator().getEntries(season);
		CollectionUtils.forAllDo(rows, new Closure() {
			@Override
			public void execute(Object input) {
				Entry row = (Entry) input;
				Element element = root.addElement(XML_ELEMENT);
				element.addAttribute(XML_TYPE, row.type.getXmlName());
				element.addElement(XML_VALUE).setText(row.value.toString());
				element.addElement(XML_PLAYER).setText(ReportHelper.getPlayerName(row.player));
				element.addElement(XML_PLAY_DATE).setText(convert(row.date));
				element.addElement(XML_COURSE).setText(row.course);
			}
		});
	}

	@Override
	@Autowired
	@Qualifier("goodBadUglyCalculator")
	public void setCalculator(StatsCalculator<Entry> calculator) {
		super.setCalculator(calculator);
	}

}

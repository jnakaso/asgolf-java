package com.jnaka.golf.reports.xml;

import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.stats.HolesInOneCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

@Component
public class HolesInOne extends AbstractStatFactory<Entry> {
	private static final String XML_ROOT = "HolesInOne";
	private static final String XML_ELEMENT = "round";
	private static final String XML_PLAYER = "player";
	private static final String XML_PLAY_DATE = "playDate";
	private static final String XML_COURSE = "course";
	private static final String XML_HOLE = "hole";

	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}

	@Override
	protected void updateDocument() {
		final Element root = getDocument().getRootElement();
		List<Entry> rows = this.getCalculator().getEntries(null);
		CollectionUtils.forAllDo(rows, new Closure() {
			@Override
			public void execute(Object input) {
				Entry entry = (Entry) input;
				Element element = root.addElement(XML_ELEMENT);
				element.addAttribute(XML_PLAYER, ReportHelper.getPlayerName(entry.player));
				element.addAttribute(XML_PLAY_DATE, convert(entry.playDate));
				element.addAttribute(XML_COURSE, entry.course);
				element.addAttribute(XML_HOLE, entry.hole.toString());
			}
		});
	}

	@Override
	@Autowired
	@Qualifier("holesInOneCalculator")
	public void setCalculator(StatsCalculator<Entry> calculator) {
		super.setCalculator(calculator);
	}

}

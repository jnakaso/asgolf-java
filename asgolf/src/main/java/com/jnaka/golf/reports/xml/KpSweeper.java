package com.jnaka.golf.reports.xml;

import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.stats.KpSweeperCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

@Component
public class KpSweeper extends AbstractStatFactory<Entry> {
	private static final String XML_ROOT = "KpSweeper";
	private static final String XML_ELEMENT = "round";
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
		List<Entry> rows = this.getCalculator().getEntries(null);
		CollectionUtils.forAllDo(rows, new Closure() {
			@Override
			public void execute(Object input) {
				Entry row = (Entry) input;
				Element element = root.addElement(XML_ELEMENT);
				element.addAttribute(XML_PLAYER, ReportHelper.getPlayerName(row.player));
				element.addAttribute(XML_PLAY_DATE, convert(row.playDate));
				element.addAttribute(XML_COURSE, row.course);
			}
		});
	}

	@Override
	@Autowired
	@Qualifier("kpSweeperCalculator")
	public void setCalculator(StatsCalculator<Entry> calculator) {
		super.setCalculator(calculator);
	}
}

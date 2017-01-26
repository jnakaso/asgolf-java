package com.jnaka.golf.reports.xml;

import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.stats.SandbaggerCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

@Component
public class Sandbagger extends AbstractStatFactory<Entry> {
	private static final String XML_ROOT = "Sandbagger";
	private static final String XML_ELEMENT = "player";
	private static final String XML_NAME = "name";
	private static final String XML_COUNT = "count";
	private static final String XML_HIGH = "high";
	private static final String XML_LOW = "low";
	private static final String XML_RANGE = "range";

	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}

	public void updateDocument() {
		Season season = new Season();
		season.setId(this.getSeasonID());
		List<Entry> rows = this.getCalculator().getEntries(season);
		CollectionUtils.forAllDo(rows, new RowClosure());
	}

	private final class RowClosure implements Closure {
		@Override
		public void execute(Object arg0) {
			Entry row = (Entry) arg0;
			Element element = DocumentHelper.createElement(XML_ELEMENT);
			element.addElement(XML_NAME).setText(ReportHelper.getPlayerName(row.player));
			element.addElement(XML_COUNT).setText(row.count.toString());
			element.addElement(XML_HIGH).setText(row.high.toString());
			element.addElement(XML_LOW).setText(row.low.toString());
			element.addElement(XML_RANGE).setText(row.range.toString());
			Element root = getDocument().getRootElement();
			root.add(element);
		}
	}

	@Override
	@Autowired
	@Qualifier("sandbaggerCalculator")
	public void setCalculator(StatsCalculator<Entry> calculator) {
		super.setCalculator(calculator);
	}
}

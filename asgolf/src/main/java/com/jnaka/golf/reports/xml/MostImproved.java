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
import com.jnaka.golf.service.stats.MostImprovedCalculator;
import com.jnaka.golf.service.stats.MostImprovedCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

@Component
public class MostImproved extends AbstractStatFactory<Entry> {
	private static final String XML_ROOT = "MostImproved";
	private static final String XML_ELEMENT = "player";
	private static final String XML_ID_PLAYER = "playerID";
	private static final String XML_NAME = "name";
	private static final String XML_COUNT = "count";
	private static final String XML_CHANGE = "change";
	private static final String XML_PERCENT_CHANGE = "percentChange";

	public void updateDocument() {
		Season season = new Season();
		season.setId(this.getSeasonID());
		List<MostImprovedCalculator.Entry> rows = this.getCalculator().getEntries(season);
		CollectionUtils.forAllDo(rows, new RowClosure());
	}

	private final class RowClosure implements Closure {

		@Override
		public void execute(Object arg0) {
			MostImprovedCalculator.Entry row = (MostImprovedCalculator.Entry) arg0;
			Element element = DocumentHelper.createElement(XML_ELEMENT);
			element.addAttribute(XML_ID_PLAYER, row.player.getId().toString());
			element.addElement(XML_NAME).setText(ReportHelper.getPlayerName(row.player));
			element.addElement(XML_COUNT).setText(row.count.toString());
			element.addElement(XML_CHANGE).setText(convert(row.change));
			element.addElement(XML_PERCENT_CHANGE).setText(convert(row.percentChange));

			Element root = getDocument().getRootElement();
			root.add(element);
		}
	}

	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}
	
	@Override
	@Autowired
	@Qualifier("mostImprovedCalculator")
	public void setCalculator(StatsCalculator<Entry> calculator) {
		super.setCalculator(calculator);
	}
}

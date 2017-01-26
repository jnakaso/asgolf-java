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
import com.jnaka.golf.service.stats.StatsCalculator;
import com.jnaka.golf.service.stats.VardonCalculator.Entry;

@Component
public class Vardon extends AbstractStatFactory<Entry> {
	private static final String XML_ROOT = "Vardon";
	private static final String XML_ELEMENT = "player";
	private static final String XML_NAME = "name";
	private static final String XML_AVERAGE = "average";
	private static final String XML_ROUNDS = "rounds";


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
			element.addElement(XML_AVERAGE).setText(convert(row.average));
			element.addElement(XML_ROUNDS).setText(row.count.toString());

			Element root = getDocument().getRootElement();
			root.add(element);
		}
	}

	@Override
	@Autowired
	@Qualifier("vardonCalculator")
	public void setCalculator(StatsCalculator<Entry> calculator) {
		super.setCalculator(calculator);
	}


}

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
import com.jnaka.golf.service.stats.StatsCalculator;
import com.jnaka.golf.service.stats.WhereWePlayedCalculator;
import com.jnaka.golf.service.stats.WhereWePlayedCalculator.Entry;

/**
 * <pre>
 * <Where>
 * <TournamentSummary>
 * <date>Jun 3, 2001</date>
 * <tournamentId>59</tournamentId>
 * <course>Alderbrook</course>
 * <average>80.00</average>
 * </TournamentSummary>
 * </Where>
 * </pre>
 * 
 * 
 * @author jnakaso
 * 
 */
@Component
public class WhereWePlayed extends AbstractStatFactory<WhereWePlayedCalculator.Entry> {

	private static final String XML_ROOT = "Where";
	private static final String XML_ROW_ELEMENT = "TournamentSummary";
	private static final String XML_DATE = "date";
	private static final String XML_COURSE = "course";
	private static final String XML_AVG = "average";
	private static final String XML_ID_TOURNAMENT = "tournamentId";

	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}

	@Override
	protected void updateDocument() {
		Season season = new Season();
		List<WhereWePlayedCalculator.Entry> rows = this.getCalculator().getEntries(season);
		CollectionUtils.forAllDo(rows, new RowClosure());
	}

	private final class RowClosure implements Closure {

		@Override
		public void execute(Object arg0) {
			WhereWePlayedCalculator.Entry entry = (WhereWePlayedCalculator.Entry) arg0;
			Element element = DocumentHelper.createElement(XML_ROW_ELEMENT);
			element.addElement(XML_ID_TOURNAMENT).setText(entry.tourId.toString());
			element.addElement(XML_DATE).setText(convert(entry.date));
			element.addElement(XML_COURSE).setText(entry.course);
			element.addElement(XML_AVG).setText(convert(entry.average));

			Element root = getDocument().getRootElement();
			root.add(element);
		}
	}

	@Override
	@Autowired
	@Qualifier("whereWePlayedCalculator")
	public void setCalculator(StatsCalculator<Entry> calculator) {
		super.setCalculator(calculator);
	}


}

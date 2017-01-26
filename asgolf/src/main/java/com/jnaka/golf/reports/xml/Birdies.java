package com.jnaka.golf.reports.xml;

import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.stats.BirdiesCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

/**
 * 
 * 
 * <pre>
 * <Scoring>
 *  <playerScores playerId="1">
 *   <playerName>Bruce Kaneshiro</playerName>
 *   <count_-2>4</count_-2>
 *   <adjustedCount_-2>42</adjustedCount_-2>
 *   <count_-1>85</count_-1>
 *   <adjustedCount_-1>242</adjustedCount_-1>
 *   <count_0>555</count_0>
 *   <adjustedCount_0>493</adjustedCount_0>
 *   <count_1>386</count_1>
 *   <adjustedCount_1>304</adjustedCount_1>
 *   <count_2>86</count_2>
 *   <adjustedCount_2>36</adjustedCount_2>
 *  </playerScores>
 * </Scoring>
 * </pre>
 * 
 * @author jnakaso
 * 
 */
@Component
public class Birdies extends AbstractStatFactory<Entry> {
	private static final String XML_ROOT = "Scoring";
	private static final String XML_ELEMENT = "playerScores";

	private static final String XML_ID_PLAYER = "playerId";
	private static final String XML_PLAYER_NAME = "playerName";
	private static final String XML_ADJ_COUNT_PREFIX = "adjustedCount_";
	private static final String XML_COUNT_PREFIX = "count_";

	private static final int LOW_BOUNDARY = -2;
	private static final int HI_BOUNDARY = 2;

	
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
			element.addAttribute(XML_ID_PLAYER, row.player.getId().toString());
			element.addElement(XML_PLAYER_NAME).setText(ReportHelper.getPlayerName(row.player));
			for (int i = LOW_BOUNDARY; i <= HI_BOUNDARY; i++) {
				String diff = Integer.toString(i);
				Integer score = row.scores.get(Integer.valueOf(i));
				element.addElement(XML_COUNT_PREFIX + diff).setText(ObjectUtils.toString(score, "0"));
				Integer adjusted = row.adjusted.get(Integer.valueOf(i));
				element.addElement(XML_ADJ_COUNT_PREFIX + diff).setText(ObjectUtils.toString(adjusted, "0"));
			}
			Element root = getDocument().getRootElement();
			root.add(element);
		}
	}
	@Override
	@Autowired
	@Qualifier("birdiesCalculator")
	public void setCalculator(StatsCalculator<Entry> calculator) {
		super.setCalculator(calculator);
	}


}

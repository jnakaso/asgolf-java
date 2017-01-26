package com.jnaka.golf.reports.xml;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.stats.StandingsCalculator;
import com.jnaka.golf.service.stats.StandingsCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

/**
 * 
 * <pre>
 * <PlayerScoring playerId="1">
 * 	<firstName>Bruce</firstName>
 * 	<lastName>Kaneshiro</lastName>
 * 	<points>71.5</points>
 * 	<earnings>160.83</earnings>
 * 	<hdcp>7.18</hdcp>
 * 	<attendance>12</attendance>
 * 	<kps>5</kps>
 * </PlayerScoring>
 * </pre>
 * 
 * @author jnakaso
 * 
 */
@Component
public class PlayerScoring extends AbstractStatFactory<StandingsCalculator.Entry> {

	private static final String XML_ROOT = "PlayerScorings";
	private static final String XML_ROW_ELEMENT = "PlayerScoring";
	private static final String XML_FIRST_NAME = "firstName";
	private static final String XML_LAST_NAME = "lastName";
	private static final String XML_EARNINGS = "earnings";
	private static final String XML_POINTS = "points";
	private static final String XML_ATTENDACE = "attendance";
	private static final String XML_KPS = "kps";
	private static final String XML_ID_PLAYER = "playerId";
	private static final String XML_HDCP = "hdcp";

	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}

	@Override
	protected void updateDocument() {
		Season season = new Season();
		season.setId(this.getSeasonID());
		List<Entry> rows = this.getCalculator().getEntries(season);
		CollectionUtils.forAllDo(rows, new RowClosure());
	}

	private final class RowClosure implements Closure {

		@Override
		public void execute(Object arg0) {
			Entry row = (Entry) arg0;
			Element element = DocumentHelper.createElement(XML_ROW_ELEMENT);
			element.addAttribute(XML_ID_PLAYER, row.player.getId().toString());
			element.addElement(XML_FIRST_NAME).setText(row.player.getFirstName());
			element.addElement(XML_LAST_NAME).setText(row.player.getLastName());
			element.addElement(XML_HDCP).setText(new DecimalFormat("#0.00").format(row.player.getHandicap()));
			element.addElement(XML_POINTS).setText(row.points.toString());
			element.addElement(XML_EARNINGS).setText(new DecimalFormat("#0.00").format(row.earnings));
			element.addElement(XML_ATTENDACE).setText(Integer.toString(row.attendance));
			element.addElement(XML_KPS).setText(Integer.toString(row.kps));

			Element root = getDocument().getRootElement();
			root.add(element);
		}
	}

	@Override
	@Autowired
	@Qualifier("standingsCalculator")
	public void setCalculator(StatsCalculator<StandingsCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}
}

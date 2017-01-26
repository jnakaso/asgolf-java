package com.jnaka.golf.reports.xml;

import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.stats.MoneyPlayerCalculator.Entry;
import com.jnaka.golf.service.stats.StatsCalculator;

/**
 * Big Money Tournament Winners.
 * 
 * <pre>
 * <MoneyPlayers>
 *  <player playerId="1">
 *   <playerName>Bruce Kaneshiro</playerName>
 *   <active>*</active>
 *   <tournaments>10</tournaments>
 *   <points>42</points>
 *   <earnings>85</earnings>
 *   <first>2</first>
 *   <second>3</second>
 *   <third>15</third>
 *  </player>
 * </MoneyPlayers>
 * </pre>
 * 
 * @author nakasones
 * 
 */
@Component
public class BigMoney extends AbstractStatFactory<Entry> {
	private static final String XML_ROOT = "MoneyPlayers";
	private static final String XML_ELEMENT = "player";
	private static final String XML_PLAYER_ID = "playerId";
	private static final String XML_PLAYER_ACTIVE = "active";
	private static final String XML_PLAYER_NAME = "playerName";
	private static final String XML_TOURNAMENTS = "tournaments";
	private static final String XML_POINTS = "points";
	private static final String XML_EARNINGS = "earnings";
	private static final String XML_FIRST = "first";
	private static final String XML_SECOND = "second";
	private static final String XML_THIRD = "third";

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
			public void execute(Object arg0) {
				Entry row = (Entry) arg0;
				Element element = root.addElement(XML_ELEMENT);
				element.addAttribute(XML_PLAYER_ID, row.getPlayer().getId().toString());
				element.addAttribute(XML_PLAYER_ACTIVE, row.getPlayer().getActive().toString());
				element.addAttribute(XML_PLAYER_NAME, ReportHelper.getPlayerName(row.getPlayer()));
				element.addAttribute(XML_TOURNAMENTS, row.getTournaments().toString());
				element.addAttribute(XML_POINTS, row.getPoints().toString());
				element.addAttribute(XML_EARNINGS, row.getEarnings().toString());
				element.addAttribute(XML_FIRST, row.getFinishes()[0].toString());
				element.addAttribute(XML_SECOND, row.getFinishes()[1].toString());
				element.addAttribute(XML_THIRD, row.getFinishes()[2].toString());
			}
		});
	}

	
	@Override
	@Autowired
	@Qualifier("moneyPlayerCalculator")
	public void setCalculator(StatsCalculator<Entry> calculator) {
		super.setCalculator(calculator);
	}

}

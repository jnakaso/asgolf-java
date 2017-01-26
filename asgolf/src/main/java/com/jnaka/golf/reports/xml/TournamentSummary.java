package com.jnaka.golf.reports.xml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.TournamentService;
import com.jnaka.reports.AbstractDocumentFactory;

@Component
public class TournamentSummary extends AbstractDocumentFactory {

	/**
	 * <pre>
	 * <Tournament tournamentID="151" 
	 *   seasonID="2009" 
	 *   course="Echo Falls" 
	 *   date="May 9, 2009" 
	 *   slope="66.70" 
	 *   rating="118.00">
	 *   <winner>
	 *     <player>George Tsukamaki</player>
	 *     <flight>A</flight>
	 *     <points>9.0</points>
	 *     <finish>A1</finish>
	 *     <earnings>$20.00</earnings>
	 *     <net>67</net> 
	 *   </winner>
	 * </Tournament>
	 * </pre>
	 */

	static class Row {
		Player player;
		String flight;
		String finish;
		Float points;
		Float earnings;
		Float net;
	}

	static final String XML_ROOT = "TournamentSummary";
	static final String XML_ELEMENT = "Tournament";
	static final String XML_ID_TOURNAMENT = "tournamentID";
	static final String XML_DATE = "date";
	static final String XML_COURSE = "course";
	static final String XML_RATING = "rating";
	static final String XML_SLOPE = "slope";

	@Autowired
	private TournamentService tournamentService;

	private int seasonID;

	public TournamentService getTournamentService() {
		return tournamentService;
	}

	public void setTournamentService(TournamentService tournamentService) {
		this.tournamentService = tournamentService;
	}

	public int getSeasonID() {
		return seasonID;
	}

	public void setSeasonID(int seasonID) {
		this.seasonID = seasonID;
	}

	/*
	 * course="Echo Falls" date="May 9, 2009" slope="66.70" rating="118.00"
	 * 
	 * @see com.jnaka.reports.AbstractDocumentFactory#updateDocument()
	 */
	@Override
	public void updateDocument() {
		for (Tournament tournament : this.getTournamentService().findBySeason(this.getSeasonID())) {
			Element tElement = DocumentHelper.createElement(XML_ELEMENT);
			tElement.addAttribute(XML_ID_TOURNAMENT, tournament.getId().toString());
			tElement.addAttribute(XML_DATE, convert(tournament.getDate()));
			tElement.addAttribute(XML_SLOPE, convert(tournament.getSlope()));
			tElement.addAttribute(XML_RATING, convert(tournament.getRating()));
			tElement.addAttribute(XML_COURSE, tournament.getCourse().getName());
			CollectionUtils.forAllDo(this.lookupWinnerRows(tournament), new RowClosure(tElement));
			this.getDocument().getRootElement().add(tElement);
		}
	}

	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}

	List<Row> lookupWinnerRows(Tournament tournament) {
		List<Row> rows = new ArrayList<Row>();
		for (Flight flight : Flight.values()) {
			for (Winner winner : tournament.getWinners()) {
				if (flight == winner.getRound().getFlight()) {
					Row row = this.createRow(winner);
					rows.add(row);
				}
			}
		}
		return rows;
	}

	Row createRow(Winner winner) {
		Row row = new Row();
		row.earnings = winner.getEarnings();
		row.finish = winner.getFinish();
		row.player = winner.getRound().getPlayer();
		row.net = winner.getRound().getTotalNet();
		row.points = winner.getPoints();
		row.flight = ObjectUtils.toString(winner.getRound().getFlight());
		return row;
	}

	/**
	 * <pre>
	 *   <winner>
	 *     <player>George Tsukamaki</player>
	 *     <flight>A</flight>
	 *     <points>9.0</points>
	 *     <finish>A1</finish>
	 *     <earnings>$20.00</earnings>
	 *     <net>67</net> 
	 *   </winner>
	 * </pre>
	 */
	final class RowClosure implements Closure {
		static final String XML_WINNER = "winner";
		static final String XML_PLAYER = "player";
		static final String XML_FLIGHT = "flight";
		static final String XML_FINISH = "finish";
		static final String XML_NET = "net";
		static final String XML_EARNINGS = "earnings";
		static final String XML_POINTS = "points";

		private final Element root;

		public RowClosure(Element root) {
			super();
			this.root = root;
		}

		@Override
		public void execute(Object arg0) {
			Row row = (Row) arg0;
			Element element = DocumentHelper.createElement(XML_WINNER);
			element.addElement(XML_PLAYER).setText(ReportHelper.getPlayerName(row.player));
			element.addElement(XML_FLIGHT).setText(row.flight);
			element.addElement(XML_FINISH).setText(row.finish);
			element.addElement(XML_NET).setText(convert(row.net));
			element.addElement(XML_EARNINGS).setText(convertMoney(row.earnings.doubleValue()));
			element.addElement(XML_POINTS).setText(convert(row.points));
			this.root.add(element);
		}
	}

}

package com.jnaka.golf.reports.xml;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.TwoDaySummary;
import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.TournamentService;
import com.jnaka.reports.AbstractDocumentFactory;

/**
 * <Round flight="A" handicap="7" playerId="1" player="Bruce Kaneshiro"
 * overall="159" net="145" rounds="2"/>
 * 
 * 
 * @author jnakaso
 * 
 */
@Component
public class TwoDayTournamentSummary extends AbstractDocumentFactory {

	private static final String XML_ROOT = "TwoDayTournamentSummary";
	private static final String XML_ELEMENT = "Round";
	private static final String XML_PLAYER = "player";
	private static final String XML_FLIGHT = "flight";
	private static final String XML_NET = "net";
	private static final String XML_HANDICAP = "handicap";
	private static final String XML_OVERALL = "overall";
	private static final String XML_NUM_ROUNDS = "rounds";
	private static final String XML_ID_PLAYER = "playerID";

	@Autowired
	private TournamentService tournamentService;
	private int seasonID;

	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}

	@Override
	protected void updateDocument() {
		for (TwoDaySummary summary : this.getSummaries()) {
			Element element = this.getDocument().getRootElement().addElement(XML_ELEMENT);
			element.addAttribute(XML_FLIGHT, ObjectUtils.toString(summary.getFlight()));
			element.addAttribute(XML_HANDICAP, summary.getHandicap().toString());
			element.addAttribute(XML_ID_PLAYER, summary.getPlayer().getId().toString());
			element.addAttribute(XML_PLAYER, ReportHelper.getPlayerName(summary.getPlayer()));
			element.addAttribute(XML_OVERALL, summary.getTotal().toString());
			element.addAttribute(XML_NET, summary.getTotalNet().toString());
			element.addAttribute(XML_NUM_ROUNDS, summary.getNumRound().toString());
		}
	}

	private List<TwoDaySummary> getSummaries() {
		List<TwoDaySummary> summaries = this.getTournamentService().getTwoDayRounds(this.getSeasonID());
		Collections.sort(summaries, new Comparator<TwoDaySummary>() {
			@Override
			public int compare(TwoDaySummary o1, TwoDaySummary o2) {
				int value = o1.getFlight().compareTo(o2.getFlight());
				if (value == 0) {
					value = o1.getTotalNet().compareTo(o2.getTotalNet());
				}
				return value;
			}
		});
		return summaries;
	}

	@Override
	public void setSeasonID(int seasonID) {
		this.seasonID = seasonID;
	}

	public int getSeasonID() {
		return seasonID;
	}

	public TournamentService getTournamentService() {
		return tournamentService;
	}

	public void setTournamentService(TournamentService tournamentService) {
		this.tournamentService = tournamentService;
	}

}

package com.jnaka.golf.reports.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.dao.TournamentDao;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.RoundService;
import com.jnaka.reports.AbstractDocumentFactory;

/**
 * 
 * 
 * <pre>
 * <player playerId="1" firstName="Bruce" lastName="Kaneshiro" hdcp="7.18">
 *  <Round accepted="*" date="Sep 12, 2009" course="Maplewood" rating="67.30" slope="118.00" total="77" adjusted="77"/>
 *  <Round accepted="" date="Aug 30, 2009" course="North Shore" rating="69.10" slope="126.00" total="82" adjusted="82"/>
 *  <Round accepted="" date="Aug 16, 2009" course="McCormick Woods" rating="70.70" slope="124.00" total="91" adjusted="84"/>
 *  <Round accepted="*" date="Aug 2, 2009" course="Auburn" rating="68.60" slope="114.00" total="70" adjusted="70"/>
 *  <Round accepted="*" date="Jul 26, 2009" course="Legion Memorial" rating="68.00" slope="109.00" total="75" adjusted="75"/>
 *  <Round accepted="" date="Jun 28, 2009" course="Mount Si" rating="67.40" slope="117.00" total="79" adjusted="78"/>
 *  <Round accepted="*" date="Jun 20, 2009" course="Lake Spanaway" rating="69.90" slope="118.00" total="81" adjusted="80"/>
 *  <Round accepted="" date="Jun 7, 2009" course="Battle Creek" rating="69.40" slope="117.00" total="92" adjusted="90"/>
 *  <Round accepted="*" date="Apr 18, 2009" course="Hawks Prairie - Woodlands" rating="70.00" slope="127.00" total="81" adjusted="81"/>
 *  <Round accepted="" date="Apr 5, 2009" course="Brookdale" rating="69.00" slope="115.00" total="79" adjusted="79"/>
 * </player>
 * </pre>
 * 
 * @author jnakaso
 * 
 */
@Component
public class PlayerLatestRounds extends AbstractDocumentFactory {

	private static final int NUM_RECENT_ROUNDS = 10;
	private static final String XML_ROOT = "PlayerLatest";
	private static final String XML_ROW_ELEMENT = "player";
	private static final String XML_ACCEPTED = "accepted";
	private static final String XML_TOTAL = "total";
	private static final String XML_ADJUSTED = "adjusted";
	private static final String XML_ID_PLAYER = "playerId";
	private static final String XML_FIRST_NAME = "firstName";
	private static final String XML_LAST_NAME = "lastName";
	private static final String XML_HDCP = "hdcp";
	private static final String XML_ROUND_ELEMENT = "Round";
	private static final String XML_COURSE_SLOPE = "slope";
	private static final String XML_COURSE_RATING = "rating";
	private static final String XML_COURSE = "course";
	private static final String XML_DATE = "date";

	@Autowired
	private PlayerService playerService;
	@Autowired
	private TournamentDao tournamentDao;
	private List<Round> rounds;
	
	@Override
	protected String defaultRootName() {
		return XML_ROOT;
	}

	@Override
	protected void updateDocument() {
		for (Player player : this.getPlayerService().getAll()) {
			Element playerElement = this.getDocument().getRootElement().addElement(XML_ROW_ELEMENT);
			playerElement.addAttribute(XML_ID_PLAYER, player.getId().toString());
			playerElement.addAttribute(XML_FIRST_NAME, player.getFirstName());
			playerElement.addAttribute(XML_LAST_NAME, player.getLastName());
			playerElement.addAttribute(XML_HDCP, convert(player.getHandicap()));
			for (Round round : this.getRounds(player)) {
				Element roundElement = playerElement.addElement(XML_ROUND_ELEMENT);
				roundElement.addAttribute(XML_ACCEPTED, round.getAccepted());
				roundElement.addAttribute(XML_DATE, convert(round.getTournament().getDate()));
				roundElement.addAttribute(XML_COURSE, round.getTournament().getCourse().getName());
				roundElement.addAttribute(XML_COURSE_RATING, convert(round.getTournament().getRating()));
				roundElement.addAttribute(XML_COURSE_SLOPE, convert(round.getTournament().getSlope()));
				roundElement.addAttribute(XML_TOTAL, Integer.toString(round.getTotal()));
				roundElement.addAttribute(XML_ADJUSTED, Integer.toString(round.getAdjusted()));
			}
		}

	}

	/**
	 * Gets the last 10 rounds for a player
	 * 
	 * @param player
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Round> getRounds(final Player player) {
		Predicate predicate = new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return ((Round) object).getPlayer().equals(player);
			}
		};

		List<Round> rounds = new ArrayList<Round>(CollectionUtils.select(this.getRounds(), predicate));
		Collections.sort(rounds, RoundService.DATE_COMPARATOR);
		return rounds.subList(0, Math.min(NUM_RECENT_ROUNDS, rounds.size()));
	}

	private List<Round> lookupRounds() {
		List<Round> rounds = new ArrayList<Round>();
		for (Tournament tour : this.getTournamentDao().getAll()) {
			rounds.addAll(tour.getRounds());
		}
		return rounds;
	}

	public TournamentDao getTournamentDao() {
		return this.tournamentDao;
	}

	public void setTournamentDao(TournamentDao tournamentDao) {
		this.tournamentDao = tournamentDao;
	}

	public PlayerService getPlayerService() {
		return playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public List<Round> getRounds() {
		if (this.rounds == null) {
			this.setRounds(this.lookupRounds());
		}
		return rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}

	@Override
	public void setSeasonID(int seasonID) {
		// Don't need
	}

}

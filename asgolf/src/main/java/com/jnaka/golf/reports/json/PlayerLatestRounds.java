package com.jnaka.golf.reports.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.dao.TournamentDao;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.RoundService;

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
@Component("jsonPlayerLatestRounds")
public class PlayerLatestRounds implements JsonReport<Player> {

	private static final int NUM_RECENT_ROUNDS = 10;
	private static final String ACCEPTED = "accepted";
	private static final String TOTAL = "total";
	private static final String ADJUSTED = "adjusted";
	private static final String ID_PLAYER = "playerId";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String HDCP = "hdcp";
	private static final String ROUNDS = "rounds";
	private static final String COURSE_SLOPE = "slope";
	private static final String COURSE_RATING = "rating";
	private static final String COURSE = "course";
	private static final String DATE = "date";
	
	public static String convert(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		DateFormat form = new SimpleDateFormat("MM-dd-yyyy");
		return form.format(date);
	}
	
	@Autowired
	private PlayerService playerService;
	@Autowired
	private TournamentDao tournamentDao;

	@Override
	public Object create(Player player) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ID_PLAYER, player.getId().toString());
		map.put(FIRST_NAME, player.getFirstName());
		map.put(LAST_NAME, player.getLastName());
		map.put(HDCP, player.getHandicap());
		List<Map<String, Object>> rounds = new ArrayList<Map<String, Object>>();
		map.put(ROUNDS, rounds);
		for (Round round : this.getRounds(player)) {
			Map<String, Object> roundMap = new HashMap<String, Object>();
			roundMap.put(ACCEPTED, round.getAccepted());
			roundMap.put(DATE, convert(round.getTournament().getDate()));
			roundMap.put(COURSE, round.getTournament().getCourse().getName());
			roundMap.put(COURSE_RATING, round.getTournament().getRating());
			roundMap.put(COURSE_SLOPE, round.getTournament().getSlope());
			roundMap.put(TOTAL, Integer.toString(round.getTotal()));
			roundMap.put(ADJUSTED, Integer.toString(round.getAdjusted()));
			rounds.add(roundMap);
		}
		return map;
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

		List<Round> rounds = new ArrayList<Round>(CollectionUtils.select(this.lookupRounds(), predicate));
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

}

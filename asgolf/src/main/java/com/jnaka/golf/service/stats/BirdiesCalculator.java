package com.jnaka.golf.service.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.map.LazyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.RoundService;
import com.jnaka.golf.service.stats.BirdiesCalculator.Entry;

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
public class BirdiesCalculator extends AbstractCalculator<Entry> {
	public class Entry {
		final public Player player;
		final public Integer count;
		final public Map<Integer, Integer> scores;
		final public Map<Integer, Integer> adjusted;

		public Entry(Player player, int count, Map<Integer, Integer> scores, Map<Integer, Integer> adjusted) {
			this.player = player;
			this.count = count;
			this.scores = scores;
			this.adjusted = adjusted;
		}
	}

	private static final int NUMBER_OF_HOLES = 18;
	private static final Factory ZERO = new Factory() {

		@Override
		public Object create() {
			return 0;
		}
	};

	@Autowired
	private PlayerService playerService;

	public List<Entry> getEntries(Season season) {
		List<Entry> entries = new ArrayList<Entry>();
		for (Player player : this.lookupPlayers()) {
			List<Round> rounds = this.lookupRounds(player, season);
			if (!rounds.isEmpty()) {
				Integer counts = rounds.size();
				Map<Integer, Integer> scores = this.calculateCounts(rounds);
				Map<Integer, Integer> adjusted = this.calculateAdjustedCounts(rounds);
				entries.add(new Entry(player, counts, scores, adjusted));
			}
		}
		return entries;
	}

	@SuppressWarnings("unchecked")
	private List<Round> lookupRounds(Player player, Season season) {
		List<Round> rounds = this.getPlayerService().findRounds(player, season.getId());
		rounds = new ArrayList<Round>(CollectionUtils.select(rounds, RoundService.HAS_FLIGHT_PREDICATE));
		Collections.sort(rounds, RoundService.DATE_COMPARATOR);
		return rounds;
	}

	private Map<Integer, Integer> calculateAdjustedCounts(List<Round> rounds) {
		Map<Integer, Integer> counts = this.createCountsMap();
		for (Round round : rounds) {
			Integer[] plusMinus = this.getRoundService().getPlusMinusAdjusted(round);
			this.updateCounts(counts, plusMinus);
		}
		return counts;
	}

	private Map<Integer, Integer> calculateCounts(List<Round> rounds) {
		Map<Integer, Integer> counts = this.createCountsMap();
		for (Round round : rounds) {
			Integer[] plusMinus = this.getRoundService().getPlusMinus(round);
			this.updateCounts(counts, plusMinus);
		}
		return counts;
	}

	private Map<Integer, Integer> createCountsMap() {
		Map<Integer, Integer> counts = new LinkedHashMap<Integer, Integer>();
		return counts;
	}

	private void updateCounts(Map<Integer, Integer> counts, Integer[] plusMinus) {
		@SuppressWarnings("unchecked")
		Map<Integer, Integer> map = LazyMap.decorate(counts, ZERO);
		for (int i = 0; i < NUMBER_OF_HOLES; i++) {
			Integer key = plusMinus[i];
			Integer count = map.get(key);
			counts.put(key, count + 1);
		}
	}

	public PlayerService getPlayerService() {
		return playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

}

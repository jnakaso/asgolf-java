package com.jnaka.golf.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.service.PrizeCalculator;

public class PrizeCalculatorImpl implements PrizeCalculator {

	private final List<PrizeMoney> prizeMoney;
	private final Tournament[] tournaments;

	public PrizeCalculatorImpl(List<PrizeMoney> prizes, Tournament... tournaments) {
		this.prizeMoney = prizes;
		this.tournaments = tournaments;
	}

	protected List<PrizeMoney> getPrizeMoney() {
		return this.prizeMoney;
	}

	protected Tournament[] getTournaments() {
		return this.tournaments;
	}

	protected List<Round> getRounds(final Flight flight) {
		MultiValueMap map = MultiValueMap.decorate(new HashMap<Player, Round>());
		for (Tournament tournament : this.getSortedTournaments()) {
			if (tournament != null) {
				for (Round round : tournament.getRounds()) {
					if (flight == round.getFlight()) {
						map.put(round.getPlayer(), round);
					}
				}
			}
		}
		List<Round> rounds = new ArrayList<Round>();
		for (Object object : map.keySet()) {
			@SuppressWarnings("unchecked")
			Collection<Round> playerRounds = (Collection<Round>) map.get(object);
			if (playerRounds.size() == this.getTournaments().length) {
				rounds.addAll(playerRounds);
			}
		}
		return rounds;
	}

	private List<Tournament> getSortedTournaments() {
		return Arrays.asList(this.getTournaments());
	}

	protected void addWinner(Winner winner) {
		this.getTournaments()[0].addWinner(winner);
	}

	public List<Winner> findWinners() {
		List<Winner> winners = new ArrayList<Winner>();
		for (Flight flight : Flight.values()) {
			for (Winner winner : this.findWinners(flight)) {
				winners.add(winner);
			}
		}
		return winners;
	}

	protected List<Winner> findWinners(final Flight flight) {
		MultiMap map = this.createScores(flight);
		List<Winner> winners = this.allocatePrizes(map);
		return winners;
	}

	protected List<Winner> allocatePrizes(MultiMap map) {
		List<Winner> winners = new ArrayList<Winner>();
		List<PrizeMoney> prizeMoney = new ArrayList<PrizeMoney>(this.getPrizeMoney());
		@SuppressWarnings("unchecked")
		Set<Integer> scores = new TreeSet<Integer>(map.keySet());
		Iterator<Integer> iterator = scores.iterator();
		while (!prizeMoney.isEmpty() && iterator.hasNext()) {
			Integer score = iterator.next();
			@SuppressWarnings("unchecked")
			List<List<Round>> winnerRounds = (List<List<Round>>) map.get(score);
			winners.addAll(this.createWinners(winnerRounds, prizeMoney));
		}
		return winners;
	}

	/**
	 * @param flight
	 * @return Multimap key = netScore, value = List<Round>
	 */
	protected MultiMap createScores(final Flight flight) {
		Collection<Round> rounds = this.getRounds(flight);

		// Collate rounds by player
		MultiValueMap playerRoundsMap = new MultiValueMap();
		for (Round round : rounds) {
			playerRoundsMap.put(round.getPlayer(), round);
		}

		// Collate rounds by net score
		MultiMap map = new MultiValueMap();
		for (Object object : playerRoundsMap.entrySet()) {
			@SuppressWarnings("unchecked")
			Map.Entry<Player, List<Round>> entry = (Map.Entry<Player, List<Round>>) object;
			List<Round> playerRounds = entry.getValue();
			map.put(this.calcNet(playerRounds), playerRounds);
		}

		return map;
	}

	protected Integer calcNet(List<Round> rounds) {
		Integer score = 0;
		for (Round round : rounds) {
			score += round.getTotalNet().intValue();
		}
		return score;
	}

	List<Winner> createWinners(List<List<Round>> winnerRounds, List<PrizeMoney> prizeMoney) {
		int numWinners = winnerRounds.size();
		List<PrizeMoney> placePrizeMoney = new ArrayList<PrizeMoney>();
		int count = Math.min(numWinners, prizeMoney.size());
		for (int i = 0; i < count; i++) {
			placePrizeMoney.add(prizeMoney.remove(0));
		}
		return this.divideWinnings(winnerRounds, placePrizeMoney);
	}

	List<Winner> divideWinnings(List<List<Round>> winnerRounds, List<PrizeMoney> placePrizeMoney) {
		int place = Integer.MAX_VALUE;
		float earnings = 0;
		float points = 0;
		for (PrizeMoney prize : placePrizeMoney) {
			place = Math.min(place, prize.getPlace());
			earnings += prize.getEarnings();
			points += prize.getPoints();
		}

		int count = winnerRounds.size();
		List<Winner> winners = new ArrayList<Winner>();
		for (List<Round> rounds : winnerRounds) {
			Round round = rounds.get(0);
			String finish = round.getFlight() + Integer.toString(place);
			Winner winner = new Winner(round, finish, points / count, earnings / count);
			winners.add(winner);
		}
		return winners;
	}

}

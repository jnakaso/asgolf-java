package com.jnaka.golf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;

public class HoneyPotCalculatorImpl implements HoneyPotCalculator {

	private Float ante = DEFAULT_ANTE;

	public HoneyPotCalculatorImpl() {
		super();
	}

	public HoneyPotCalculatorImpl(Float ante) {
		super();
		this.ante = ante;
	}

	@Override
	public Map<Type, List<Winner>> findWinners(Tournament tournament) {
		Map<Type, List<Winner>> winners = new HashMap<HoneyPotCalculator.Type, List<Winner>>();

		List<Round> rounds = new ArrayList<Round>(tournament.getRounds());
		int nRounds = rounds.size();

		for (Type aType : Type.values()) {
			Float purse = this.getPurse(aType, nRounds);
			List<Winner> sectionWinner = this.findWinner(aType, rounds, purse);
			rounds = this.removeWinners(rounds, sectionWinner);
			winners.put(aType, sectionWinner);
		}
		return winners;
	}

	private List<Winner> findWinner(Type type, List<Round> rounds, Float purse) {

		MultiMap roundsMap = new MultiValueMap();
		for (Round round : rounds) {
			switch (type) {
			case OVERALL:
				roundsMap.put(round.getTotalNet(), round);
				break;
			case FRONT:
				roundsMap.put(round.getFrontNet(), round);
				break;
			case BACK:
				roundsMap.put(round.getBackNet(), round);
				break;
			}
		}

		List<Winner> winners = new ArrayList<Winner>();

		@SuppressWarnings("unchecked")
		TreeSet<Float> totals = new TreeSet<Float>(roundsMap.keySet());
		if (!totals.isEmpty()) {
			Float lowScore = totals.first();

			@SuppressWarnings("unchecked")
			List<Round> winnerRounds = (List<Round>) roundsMap.get(lowScore);
			Float purseCut = winnerRounds.isEmpty() ? 0f : purse / winnerRounds.size();

			for (Round round : winnerRounds) {
				winners.add(new HoneyPotCalculator.Winner(round, purseCut));
			}
		}
		return winners;
	}

	private Float getPurse(Type type, int nRounds) {
		Float total = nRounds * this.getAnte();
		switch (type) {
		case OVERALL:
			return total.floatValue() / 2f;
		case FRONT:
		case BACK:
			return total.floatValue() / 4f;
		}
		return 0f;
	}

	private List<Round> removeWinners(List<Round> rounds, List<Winner> winners) {
		List<Round> reduced = new ArrayList<Round>(rounds);
		for (Winner winner : winners) {
			reduced.remove(winner.getRound());
		}
		return reduced;
	}

	public Float getAnte() {
		return ante;
	}

	public void setAnte(Float ante) {
		this.ante = ante;
	}
}

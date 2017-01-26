package com.jnaka.golf.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.service.HandicapCalculator;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.RoundService;

@Service("FiveOfTen")
public class FiveOfTenHandicapCalculator implements HandicapCalculator {

	private static final int MAX_ROUNDS = 10;

	static Closure ACCEPTED_CLOSURE = new Closure() {
		@Override
		public void execute(Object input) {
			((Round) input).setAccepted("*");
		}
	};

	static Closure CLEAR_CLOSURE = new Closure() {
		@Override
		public void execute(Object input) {
			((Round) input).setAccepted(null);
		}
	};

	Comparator<? super Round> BEST_ROUND_COMPARATOR = new Comparator<Round>() {
		@Override
		public int compare(Round o1, Round o2) {
			return getRoundService().getHandicapIndex(o1).compareTo(getRoundService().getHandicapIndex(o2));
		}
	};

	@Autowired
	private PlayerService playerService;
	@Autowired
	private RoundService roundService;

	public void updateHandicap(Player player) {
		List<Round> rounds = this.getPlayerService().findRounds(player);
		CollectionUtils.forAllDo(rounds, CLEAR_CLOSURE);
		List<Round> accepted = findAcceptedRounds(rounds);
		CollectionUtils.forAllDo(accepted, ACCEPTED_CLOSURE);
		player.setHandicap(0.96f * this.averageIndex(accepted));
	}

	protected List<Round> findAcceptedRounds(List<Round> rounds) {
		Collections.sort(rounds, RoundService.DATE_COMPARATOR);
		List<Round> recentRounds = rounds.subList(0, Math.min(rounds.size(), this.getMaxRounds()) - 1);
		Collections.sort(recentRounds, BEST_ROUND_COMPARATOR);
		return rounds.subList(0, this.getMaxBestRounds(recentRounds));
	}

	protected int getMaxRounds() {
		return MAX_ROUNDS;
	}

	protected float averageIndex(List<Round> accepted) {
		float total = 0;
		for (Round round : accepted) {
			Float index = this.getRoundService().getHandicapIndex(round);
			total += index;
		}
		return total / accepted.size();
	}


	protected int getMaxBestRounds(List<Round> recentRounds) {
		int size = recentRounds.size();
		
		if (size > 8)
			return 5;
		if (size > 6)
			return 4;
		if (size > 4)
			return 3;
		if (size > 1)
			return 2;
		throw new IllegalStateException("should have rounds");
	}

	public PlayerService getPlayerService() {
		return this.playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public RoundService getRoundService() {
		return roundService;
	}

	public void setRoundService(RoundService roundService) {
		this.roundService = roundService;
	}
}

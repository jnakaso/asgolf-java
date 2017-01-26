package com.jnaka.golf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.PrizeCalculator;
import com.jnaka.golf.service.PrizeCalculatorFactory;
import com.jnaka.golf.service.TournamentService;

@Service
public class PrizeCalculatorFactoryImpl implements PrizeCalculatorFactory {

	@Autowired
	private TournamentService tournamentService;

	private TournamentService getTournamentService() {
		return this.tournamentService;
	}

	public void setTournamentService(TournamentService tournamentService) {
		this.tournamentService = tournamentService;
	}

	@Override
	public PrizeCalculator getCalculator(Tournament tournament) {
		switch (tournament.getType()) {
		case DAY_ONE:
			return this.createNoPrizeCalculator();
		case DAY_TWO:
			return this.createTwoDay(tournament);
		default:
			return this.createNormal(tournament);
		}
	}

	private PrizeCalculator createNoPrizeCalculator() {
		return new PrizeCalculatorImpl(new ArrayList<PrizeMoney>());
	}

	private PrizeCalculator createTwoDay(Tournament tournament) {
		Season season = tournament.getSeason();
		List<Tournament> tournaments = this.getTournamentService().findBySeason(season.getId().intValue());
		Tournament dayOne = null;
		Tournament dayTwo = null;
		for (Tournament tour : tournaments) {
			switch (tour.getType()) {
			case DAY_ONE:
				dayOne = tour;
				break;
			case DAY_TWO:
				dayTwo = tour;
				break;
			default:
				break;
			}
		}
		return new PrizeCalculatorImpl(TWO_DAY_PRIZES.get(season.getScoringPolicy()), dayTwo, dayOne);
	}

	PrizeCalculator createNormal(Tournament tournament) {
		Season season = tournament.getSeason();
		return new PrizeCalculatorImpl(NORMAL_PRIZES.get(season.getScoringPolicy()), tournament);
	}

}

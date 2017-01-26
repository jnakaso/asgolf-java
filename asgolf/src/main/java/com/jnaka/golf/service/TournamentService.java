package com.jnaka.golf.service;

import java.io.File;
import java.util.List;

import com.jnaka.domain.EntityObjectService;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.TwoDaySummary;
import com.jnaka.golf.domain.Winner;

public interface TournamentService extends EntityObjectService<Tournament> {

	List<Round> findRoundsBySeason(int season);

	List<Round> findRoundsBySeason(int season, Player player);

	List<Round> findRoundsByPlayer(Player player);

	List<Winner> findWinners(Tournament tournament, final Flight flight);

	List<Kp> findKps(Tournament tournament, final Flight flight);

	List<Tournament> findBySeason(int seasonID);

	List<TwoDaySummary> getTwoDayRounds(int seasonID);

	boolean export(Tournament tournament, File file);

	Tournament importTournament(File file);

	void updateTotals(Tournament tournament);

	boolean addRound(Tournament tournament, Round round);

	boolean addKp(Tournament tournament, Kp kp);

	boolean removeRound(Tournament tournament, Round round);

	boolean removeKp(Tournament tournament, Kp kp);

	List<Round> findKpSweepers(int season);
	
	float averageNet(Tournament tournament);

	boolean hasPlayed(Tournament tournament, Player player);

	Tournament getLastTournament();
	
}

package com.jnaka.golf.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Repository;

import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;

@Repository("TournamentDao")
public class TournamentJaxbDao extends ObjectJaxbDao<Tournament> implements TournamentDao {

	@Override
	public void create(Tournament anObject) {
		anObject.setId(this.getLastId() + 1);
		this.getGolfClub().addTournament(anObject);
	}

	@Override
	public boolean delete(Tournament dTour) {
		return this.getGolfClub().removeTournament(dTour);
	}

	@Override
	public List<Tournament> getAll() {
		return new ArrayList<Tournament>(this.getGolfClub().getTournaments());
	}

	@SuppressWarnings("unchecked")
	public List<Tournament> findBySeason(final int season) {
		return (List<Tournament>) CollectionUtils.select(this.getGolfClub().getTournaments(), new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				Tournament test = (Tournament) arg0;
				return test.getSeason().getId().intValue() == season;
			}
		});
	}

	public boolean addRound(Tournament tournament, Round round) {
		round.setId(this.getLastRoundId() + 1);
		return tournament.addRound(round);
	}

	public boolean addKp(Tournament tournament, Kp kp) {
		return tournament.addKp(kp);
	}

	public boolean removeRound(Tournament tournament, Round round) {
		return tournament.removeRound(round);
	}

	public boolean removeKp(Tournament tournament, Kp kp) {
		return tournament.removeKp(kp);
	}

	private int getLastRoundId() {
		int id = 0;
		for (Tournament tournament : this.getAll()) {
			for (Round round : tournament.getRounds()) {
				id = Math.max(id, round.getId().intValue());
			}
		}
		return id;
	}

}

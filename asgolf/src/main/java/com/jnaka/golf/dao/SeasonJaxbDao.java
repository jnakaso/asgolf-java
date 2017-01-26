package com.jnaka.golf.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Repository;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;

@Repository("SeasonDao")
public class SeasonJaxbDao extends ObjectJaxbDao<Season> implements SeasonDao {

	private static final Comparator<? super Tournament> TOURNAMENT_DATE_COMPARATOR = new Comparator<Tournament>() {
		@Override
		public int compare(Tournament o1, Tournament o2) {
			return o2.getDate().compareTo(o1.getDate());
		}
	};

	@Override
	public void create(Season anObject) {
		this.getGolfClub().addSeason(anObject);
	}

	@Override
	public boolean delete(Season anObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Season> getAll() {
		return new ArrayList<Season>(this.getGolfClub().getSeasons());
	}

	@Override
	public List<Tournament> findTournamentsBySeason(final Season season) {
		List<Tournament> tours = new ArrayList<Tournament>();
		CollectionUtils.select(this.getGolfClub().getTournaments(), this.createTournamentPredicate(season), tours);
		Collections.sort(tours, TOURNAMENT_DATE_COMPARATOR);
		return tours;
	}

	private Predicate createTournamentPredicate(final Season season) {
		return new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				Tournament test = (Tournament) object;
				return test.getSeason().equals(season);
			}
		};
	}

}

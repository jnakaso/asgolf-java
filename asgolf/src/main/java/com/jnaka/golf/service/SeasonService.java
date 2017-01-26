package com.jnaka.golf.service;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.jnaka.domain.EntityObjectService;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.PrizeMoney;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.TwoDaySummary;

public interface SeasonService extends EntityObjectService<Season> {

	public Season currentSeason();

	public List<SeasonSummary> findSummaries(Season season);

	public List<Player> getActivePlayers(Season season);

	public boolean addTournament(Season season, Tournament tournament);

	public List<Tournament> findTournaments(Season season);

	public List<Tournament> findTournaments(Season season, Tournament.Type type);

	public PrizeMoney getKpPrizeMoney(Season season);

	public PrizeMoney getAttendancePrizeMoney(Season season);

	public SeasonSummary sum(Collection<SeasonSummary> summaries);

	public void updateSummaries(Season season, List<SeasonSummary> newSummaries);

	public boolean export(Season item, File selected);

	public List<TwoDaySummary> findTwoDaySummaries(Season season, Flight flight);

	public void updateTotals(Season season);

	public void updateHandicaps(Season season);

}

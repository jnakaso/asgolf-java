package com.jnaka.golf.service;

import java.io.File;

import com.jnaka.golf.domain.GolfClub;
import com.jnaka.golf.domain.Tournament;

public interface GolfClubService {

	public GolfClub load(File file);

	public boolean save();

	public PrizeCalculator getPrizeCalculator(GolfClub club);

	public Tournament uploadTournament(Integer seasonId, File file);

	public boolean exportSeason(Integer season);
}

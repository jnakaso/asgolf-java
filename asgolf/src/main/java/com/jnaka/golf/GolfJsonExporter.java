package com.jnaka.golf;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jnaka.golf.dao.DataStore;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.json.JsonWriter;
import com.jnaka.golf.service.SeasonService;

/**
 * <pre>
 * data
 * 	- #year
 *    tournaments.json
 *    tournament_#id.json
 *    stats.json
 *  - player
 *    player_#id.json
 *  seasons.json
 *  courses.json
 *  players.json
 * </pre>
 * 
 * @author nakasones
 * 
 */
@Service
public class GolfJsonExporter {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String DIR_PLAYERS = "players";

	

	private static final Comparator<Season> NEWEST = new Comparator<Season>() {
		@Override
		public int compare(Season arg0, Season arg1) {
			return arg1.getId().compareTo(arg0.getId());
		}
	};

	@Autowired
	@Qualifier("coursesWriter")
	private JsonWriter<List<Course>> coursesWriter;
	@Autowired
	@Qualifier("playersWriter")
	private JsonWriter<List<Player>> playersWriter;
	@Autowired
	@Qualifier("statsWriter")
	private JsonWriter<Season> statsWriter;
	@Autowired
	@Qualifier("recordsWriter")
	private JsonWriter<Season> recordsWriter;
	@Autowired
	@Qualifier("playerWriter")
	private JsonWriter<Player> playerWriter;
	@Autowired
	@Qualifier("tournamentsWriter")
	private JsonWriter<Season> tournamentsWriter;
	@Autowired
	@Qualifier("twoDayTournamentWriter")
	private JsonWriter<Season> twoDayTournamentWriter;
	@Autowired
	@Qualifier("tournamentWriter")
	private JsonWriter<Tournament> tournamentWriter;

	@Autowired
	@Qualifier("DataStore")
	private DataStore dataStore;
	@Autowired
	@Qualifier("SeasonService")
	private SeasonService seasonService;
	@Value("${data.directory.output}")
	private String targetDirectory;
	
	private String coursesFileName = "courses.js";
	private String playersFileName = "players.js";
	private String recordsFileName = "records.js";
	private String statsFileName = "stats.js";
	private String tournamentsFileName = "tournaments.js";
	private String twoDayTournamentFileName = "twoday.js";

	public void export() {
		Season season = this.getSeason();
		this.getStatsWriter().export(season, this.getSeasonFile());
	}

	public void export(Season season) {
		this.getStatsWriter().export(season, this.getSeasonFile());
	}

	public void export(Integer year) {
		Season season = new Season();
		season.setId(year);
		this.getCoursesWriter().export(this.getCourses(), this.getCoursesFile());
		this.getPlayersWriter().export(this.getPlayers(), this.getPlayersFile());
		List<Player> active = this.getSeasonService().getActivePlayers(season);
		for (Player player : active) {
			this.getPlayerWriter().export(player, this.getPlayerFile(player));
		}

		this.getStatsWriter().export(season, this.getStatsFile(season));
		this.getTournamentsWriter().export(season, this.getTournamentsFile(season));
		this.getTwoDayTournamentWriter().export(season, this.getTwoDayTournamentFile(season));

		List<Tournament> tournaments = this.getSeasonService().findTournaments(season);
		for (Tournament tournament : tournaments) {
			this.getTournamentWriter().export(tournament, this.getTournamentFile(tournament));
		}
		this.getRecordsWriter().export(null, this.getRecordsFile());
	}

	public void exportAll() {
		this.getCoursesWriter().export(this.getCourses(), this.getCoursesFile());
		this.getPlayersWriter().export(this.getPlayers(), this.getPlayersFile());
		for (Player player : this.getPlayers()) {
			this.getPlayerWriter().export(player, this.getPlayerFile(player));
		}
		for (Season season : this.getDataStore().getGolfClub().getSeasons()) {
			this.getStatsWriter().export(season, this.getStatsFile(season));
			this.getTournamentsWriter().export(season, this.getTournamentsFile(season));
			this.getTwoDayTournamentWriter().export(season, this.getTwoDayTournamentFile(season));

			List<Tournament> tournaments = this.getSeasonService().findTournaments(season);
			for (Tournament tournament : tournaments) {
				this.getTournamentWriter().export(tournament, this.getTournamentFile(tournament));
			}
		}
		this.getRecordsWriter().export(null, this.getRecordsFile());
	}

	private Season getSeason() {
		List<Season> seasons = this.getDataStore().getGolfClub().getSeasons();
		if (seasons.isEmpty()) {
			return null;
		}
		seasons = new ArrayList<Season>(seasons);
		Collections.sort(seasons, NEWEST);
		return seasons.get(0);
	}

	private List<Course> getCourses() {
		return this.getDataStore().getGolfClub().getCourses();
	}

	private List<Player> getPlayers() {
		return this.getDataStore().getGolfClub().getPlayers();
	}

	private File getStatsFile(Season season) {
		return new File(this.getTargetDirectory() + FILE_SEPARATOR + season.getId() + FILE_SEPARATOR,
				this.getStatsFileName());
	}

	private File getTournamentsFile(Season season) {
		return new File(this.getTargetDirectory() + FILE_SEPARATOR + season.getId() + FILE_SEPARATOR,
				this.getTournamentsFileName());
	}

	private File getTwoDayTournamentFile(Season season) {
		return new File(this.getTargetDirectory() + FILE_SEPARATOR + season.getId() + FILE_SEPARATOR,
				this.getTwoDayTournamentFileName());
	}

	private File getPlayerFile(Player player) {
		return new File(this.getTargetDirectory() + FILE_SEPARATOR + DIR_PLAYERS + FILE_SEPARATOR, player.getId()
				+ ".js");
	}

	private File getTournamentFile(Tournament tournament) {
		String season = tournament.getSeasonID().toString();
		return new File(this.getTargetDirectory() + FILE_SEPARATOR + season + FILE_SEPARATOR, tournament.getId()
				+ ".js");
	}

	private File getRecordsFile() {
		return new File(this.getTargetDirectory(), this.getRecordsFileName());
	}

	private File getSeasonFile() {
		return new File(this.getTargetDirectory(), this.getStatsFileName());
	}

	private File getPlayersFile() {
		return new File(this.getTargetDirectory(), this.getPlayersFileName());
	}

	private File getCoursesFile() {
		return new File(this.getTargetDirectory(), this.getCoursesFileName());
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	public JsonWriter<List<Course>> getCoursesWriter() {
		return coursesWriter;
	}

	public void setCoursesWriter(JsonWriter<List<Course>> coursesWriter) {
		this.coursesWriter = coursesWriter;
	}

	public JsonWriter<List<Player>> getPlayersWriter() {
		return playersWriter;
	}

	public void setPlayersWriter(JsonWriter<List<Player>> playersWriter) {
		this.playersWriter = playersWriter;
	}

	public JsonWriter<Season> getStatsWriter() {
		return this.statsWriter;
	}

	public void setStatsWriter(JsonWriter<Season> statsWriter) {
		this.statsWriter = statsWriter;
	}

	public JsonWriter<Season> getRecordsWriter() {
		return recordsWriter;
	}

	public void setRecordsWriter(JsonWriter<Season> recordsWriter) {
		this.recordsWriter = recordsWriter;
	}

	public JsonWriter<Player> getPlayerWriter() {
		return playerWriter;
	}

	public void setPlayerWriter(JsonWriter<Player> playerWriter) {
		this.playerWriter = playerWriter;
	}

	public JsonWriter<Season> getTournamentsWriter() {
		return tournamentsWriter;
	}

	public void setTournamentsWriter(JsonWriter<Season> tournamentsWriter) {
		this.tournamentsWriter = tournamentsWriter;
	}

	public JsonWriter<Season> getTwoDayTournamentWriter() {
		return twoDayTournamentWriter;
	}

	public void setTwoDayTournamentWriter(JsonWriter<Season> twoDayTournamentWriter) {
		this.twoDayTournamentWriter = twoDayTournamentWriter;
	}

	public JsonWriter<Tournament> getTournamentWriter() {
		return tournamentWriter;
	}

	public void setTournamentWriter(JsonWriter<Tournament> tournamentWriter) {
		this.tournamentWriter = tournamentWriter;
	}

	public String getTargetDirectory() {
		return targetDirectory;
	}

	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public String getCoursesFileName() {
		return coursesFileName;
	}

	public void setCoursesFileName(String coursesFileName) {
		this.coursesFileName = coursesFileName;
	}

	public String getPlayersFileName() {
		return playersFileName;
	}

	public void setPlayersFileName(String playersFileName) {
		this.playersFileName = playersFileName;
	}

	public String getTwoDayTournamentFileName() {
		return twoDayTournamentFileName;
	}

	public void setTwoDayTournamentsFileName(String twoDayTournamentFileName) {
		this.twoDayTournamentFileName = twoDayTournamentFileName;
	}

	public String getStatsFileName() {
		return this.statsFileName;
	}

	public void setStatsFileName(String statsFileName) {
		this.statsFileName = statsFileName;
	}

	public String getRecordsFileName() {
		return recordsFileName;
	}

	public void setRecordsFileName(String recordsFileName) {
		this.recordsFileName = recordsFileName;
	}

	public String getTournamentsFileName() {
		return tournamentsFileName;
	}

	public void setTournamentsFileName(String tournamentsFileName) {
		this.tournamentsFileName = tournamentsFileName;
	}

	SeasonService getSeasonService() {
		return seasonService;
	}

	void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}
}

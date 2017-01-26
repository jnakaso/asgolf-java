package com.jnaka.golf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jnaka.golf.dao.JaxbDataStore;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.GolfClub;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.json.JsonWriter;
import com.jnaka.golf.domain.xml.GolfClubReader;
import com.jnaka.golf.domain.xml.GolfClubReaderImpl;
import com.jnaka.golf.domain.xml.GolfClubWriterImpl;
import com.jnaka.golf.service.CourseService;
import com.jnaka.golf.service.PlayerService;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.service.TournamentService;

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
public class TournamentProcessor {

	private static final String DATA_STORE_FILE = "asgolf.xml";
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String DIR_PLAYERS = "players";


	private Log log = LogFactory.getLog(this.getClass());
	@Autowired
	private JaxbDataStore dataStore;
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
	@Qualifier("SeasonService")
	private SeasonService seasonService;
	@Autowired
	@Qualifier("CourseService")
	private CourseService courseService;
	@Autowired
	@Qualifier("PlayerService")
	private PlayerService playerService;
	@Autowired
	@Qualifier("TournamentService")
	private TournamentService tournamentService;

	@Value("data.directory")
	private String targetDirectory = "/users/nakasones/GolfData";
	private String coursesFileName = "courses.js";
	private String playersFileName = "players.js";
	private String recordsFileName = "records.js";
	private String statsFileName = "stats.js";
	private String tournamentsFileName = "tournaments.js";
	private String twoDayTournamentFileName = "twoday.js";

	public void process(String tournamentFile) throws Exception {
		this.loadDataStore(new File(targetDirectory, DATA_STORE_FILE));
		this.saveDataStore(new File(targetDirectory, DATA_STORE_FILE + ".bak"));
		
		Season season = this.getSeasonService().currentSeason();
		Tournament tournament = this.importTournament(season, tournamentFile);

		this.saveDataStore(new File(targetDirectory, DATA_STORE_FILE));

		this.exportTournament(season, tournament);
		this.exportTournaments(season);
		this.exportCourses();
		this.exportPlayers(season);
		this.exportRecords();
		this.exportStats(season);
	}

	private void exportTournament( Season season, Tournament tournament) {
		String tournamentFile = tournament.getId() + ".js";

		this.log.info("Exporting " + tournamentFile);
		this.getTournamentWriter().export(tournament,
				new File(targetDirectory, season.getId() + FILE_SEPARATOR + tournamentFile));
	}

	public void loadDataStore(File file) throws FileNotFoundException, JAXBException {
		this.log.info("Loading data store" + DATA_STORE_FILE);
		GolfClubReader clubReader = new GolfClubReaderImpl();
		GolfClub golfClub = clubReader.read(new FileReader(file));
		this.getDataStore().setGolfClub(golfClub);
		this.getDataStore().setFileName(file.getAbsolutePath());
	}

	public void saveDataStore(File file) throws IOException, JAXBException {
		this.log.info("Backing data store up to " + DATA_STORE_FILE + ".bak");

		FileWriter writer = new FileWriter(file);
		GolfClubWriterImpl clubWriter = new GolfClubWriterImpl();
		clubWriter.write(this.getDataStore().getGolfClub(), writer);
	}

	private Tournament importTournament(Season season, String tournamentFile) {
		this.log.info("Importing " + tournamentFile);

		File file = new File(tournamentFile);
		Tournament tournament = this.getTournamentService().importTournament(file);
		this.getSeasonService().addTournament(season, tournament);
		this.getTournamentService().create(tournament);
		
		this.getTournamentService().update(tournament);
		
		return tournament;
	}

	private void exportTournaments(Season season) {
		this.log.info("Exporting tournaments for season " + season);

		File file = this.getFile(season, this.getTournamentsFileName());
		this.getTournamentsWriter().export(season, file);

		File file2 = this.getFile(season, this.getTwoDayTournamentFileName());
		this.getTwoDayTournamentWriter().export(season, file2);
	}

	private void exportRecords() {
		this.log.info("Exporting records.");
		File file = new File(this.getTargetDirectory(), this.getRecordsFileName());
		this.getRecordsWriter().export(null, file);
	}

	private void exportStats(Season season) {
		this.log.info("Exporting stats for season " + season);
		
		File file = this.getFile(season, this.getStatsFileName());
		this.getStatsWriter().export(season, file);
	}

	private void exportCourses() {
		this.log.info("Exporting tournaments for courses.");

		List<Course> courses = this.getCourseService().getAll();
		File file = new File(this.getTargetDirectory(), this.getCoursesFileName());
		this.getCoursesWriter().export(courses, file);
	}

	private void exportPlayers(Season season) {
		this.log.info("Exporting players for season " + season);

		List<Player> players = this.getPlayerService().getAll();
		File file = new File(this.getTargetDirectory(), this.getPlayersFileName());
		this.getPlayersWriter().export(players, file);

		List<Player> active = this.getSeasonService().getActivePlayers(season);
		for (Player player : active) {
			File pFile = new File(this.getTargetDirectory() + FILE_SEPARATOR + DIR_PLAYERS + FILE_SEPARATOR,
					player.getId() + ".js");
			this.getPlayerWriter().export(player, pFile);
		}
	}

	private File getFile(Season season, String fileName) {
		return new File(this.getTargetDirectory() + FILE_SEPARATOR + season.getId() + FILE_SEPARATOR, fileName);
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

	PlayerService getPlayerService() {
		return this.playerService;
	}

	void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	SeasonService getSeasonService() {
		return seasonService;
	}

	void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}

	CourseService getCourseService() {
		return courseService;
	}

	void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}

	TournamentService getTournamentService() {
		return tournamentService;
	}

	void setTournamentService(TournamentService tournamentService) {
		this.tournamentService = tournamentService;
	}

	private JaxbDataStore getDataStore() {
		return this.dataStore;
	}

	void setDataStore(JaxbDataStore dataStore) {
		this.dataStore = dataStore;
	}

}

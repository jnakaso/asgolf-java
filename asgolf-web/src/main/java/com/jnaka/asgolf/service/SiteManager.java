package com.jnaka.asgolf.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.GolfClubService;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.service.TournamentService;

/**
 * @author nakasones
 *
 *
 *         <pre>
 *
 {
	"powerRank": 2015,
	"currentSeason": 2016,
	"currentEvent": 1,
	"nextCourseId": 14,
	"lastTournamentSeason": 2015,
	"lastTournamentId": 238,
	"showTwoDay":false
}
 *         </pre>
 */
@Service
public class SiteManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String DEFAULT_INI = "ini.js";
	private static final String EVENTS_FILE = "events.xml";

	private ObjectMapper objectMapper = new ObjectMapper();

	@Value("${data.directory.output}")
	private String dataDirectory;
	@Autowired
	private SeasonService seasonService;
	@Autowired
	private TournamentService tournamentService;
	@Autowired
	private GolfClubService golfClubService;
	
	public boolean save() {
		return this.getGolfClubService().save();
	}

	public String getCurrentEvents() throws IOException {
		Season currentSeason = this.getSeasonService().currentSeason();
		File file = new File(this.dataDirectory, currentSeason.getId() + FILE_SEPARATOR + EVENTS_FILE);
		return FileUtils.readFileToString(file);
	}

	public SiteDefaults createDefaults() {
		Season currentSeason = this.getSeasonService().currentSeason();

		SiteDefaults siteDefaults = new SiteDefaults();
		siteDefaults.setCurrentSeason(currentSeason.getId());
		siteDefaults.setPowerRank(currentSeason.getId());
		Tournament tour = this.getTournamentService().getLastTournament();
		if (tour != null) {
			siteDefaults.setLastTournamentId(tour.getId());
			siteDefaults.setLastTournamentSeason(tour.getSeasonID());
		}
		return siteDefaults;
	}

	public boolean writeSiteInfo(Integer season) {
		this.getLogger().debug("Writing info for {}", season);
		return this.getGolfClubService().exportSeason(season);
	}

	public boolean writeSiteInfo() {
		return !this.getSeasonService().getAll().stream() //
				.map(s -> this.getGolfClubService().exportSeason(s.getId())) //
				.collect(Collectors.toList()) //
				.contains(false);
	}

	public void updateTotals(Integer id) throws IOException, JAXBException {
		this.getLogger().debug("season-update-totals= {}", id);

		Season season = this.getSeasonService().get(id);
		this.getSeasonService().updateTotals(season);

	}

	public void updateFunnies(String funnyD) throws IOException {
		@SuppressWarnings("unchecked")
		List<String> lines = FileUtils.readLines(new File(funnyD));

		StringBuilder builder = new StringBuilder("{\"funnies\" : [");

		boolean hasOne = false;
		for (String line : lines) {
			if (hasOne) {
				builder.append(",");
			}
			builder.append("\"" + line + "\"");
			hasOne = true;
		}
		builder.append("]}");
		FileUtils.writeStringToFile(new File(funnyD), builder.toString());
	}

	public boolean writeDefaultsToFile(SiteDefaults defaults) {
		try {
			File file = new File(this.dataDirectory, DEFAULT_INI);
			this.getLogger().debug("file= {}, defaults={}", file, defaults);
			String output = this.getObjectMapper().writeValueAsString(defaults);
			FileUtils.writeStringToFile(file, output);
			return true;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}



	ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	GolfClubService getGolfClubService() {
		return golfClubService;
	}

	void setGolfClubService(GolfClubService golfClubService) {
		this.golfClubService = golfClubService;
	}

	TournamentService getTournamentService() {
		return tournamentService;
	}

	void setTournamentService(TournamentService tournamentService) {
		this.tournamentService = tournamentService;
	}

	SeasonService getSeasonService() {
		return seasonService;
	}

	void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}



	Logger getLogger() {
		return logger;
	}

}

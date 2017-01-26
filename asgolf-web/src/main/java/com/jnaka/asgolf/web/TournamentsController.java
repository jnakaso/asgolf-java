package com.jnaka.asgolf.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.GolfClubService;
import com.jnaka.golf.service.TournamentService;

@RestController
public class TournamentsController {

	public interface TournamentMixIn {
		@JsonIgnore
		Tournament getTournament();

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
		Date getDate();
	}

	public interface RoundMixIn {
		@JsonIgnore
		Tournament getTournament();
	}

	public interface CourseMixIn {
		@JsonIgnore
		List<CourseTee> getTees();
	}

	public interface PlayerMixIn {
		@JsonIgnore
		List<SeasonSummary> getSeasonSummaries();
	}

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ObjectMapper mapper;

	@Autowired
	private GolfClubService golfClubService;
	@Autowired
	private TournamentService tournamentService;

	@RequestMapping("/tournaments")
	public List<Tournament> getTournaments() throws Exception {
		return this.getTournamentService().getAll();
	}

	@RequestMapping("/tournament/{id}")
	public JsonNode getTournament(final @PathVariable Integer id) {
		Tournament tour = this.getTournamentService().get(id);
		return this.getMapper().valueToTree(tour);
	}

	@RequestMapping(value = "/tournament-create", method = RequestMethod.POST)
	public JsonNode importTournament(@RequestParam("seasonId") Integer seasonId) {
		Tournament tournament = new Tournament();
		tournament.setSeasonID(seasonId);
		return this.getMapper().valueToTree(tournament);
	}

	@RequestMapping(value = "/tournament-import", method = RequestMethod.POST)
	public JsonNode importTournament(@RequestParam("seasonId") Integer seasonId,
			@RequestParam("tournament") String json) throws IOException {
		File file = new File("temp.json");
		FileUtils.writeByteArrayToFile(file, json.getBytes());
		Tournament tournament = this.getGolfClubService().uploadTournament(seasonId, file);
		return this.getMapper().valueToTree(tournament);
	}

	@RequestMapping(value = "/tournament-update", method = RequestMethod.POST)
	public JsonNode updateTournament(@RequestBody Tournament tournament) {
		Tournament orig = this.getTournamentService().get(tournament.getId());
		this.copy(tournament, orig);
		this.getTournamentService().update(orig);
		return this.getMapper().valueToTree(orig);
	}

	private void copy(Tournament tournament, Tournament orig) {
		orig.setCourse(tournament.getCourse());
		orig.setKps(tournament.getKps());
		
		orig.getRounds().clear();
		for (Round round : tournament.getRounds()) {
			orig.addRound(round);
		}
		
		orig.setDate(tournament.getDate());
		orig.setType(tournament.getType());
		orig.setRating(tournament.getRating());
		orig.setSlope(tournament.getSlope());
	}

	ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			Map<Class<?>, Class<?>> mixin = new HashMap<>();
			mixin.put(Tournament.class, TournamentMixIn.class);
			mixin.put(Course.class, CourseMixIn.class);
			mixin.put(Round.class, RoundMixIn.class);
			mixin.put(Player.class, PlayerMixIn.class);
			mapper.setMixInAnnotations(mixin);
		}
		return mapper;
	}

	void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	TournamentService getTournamentService() {
		return tournamentService;
	}

	void setTournamentService(TournamentService service) {
		this.tournamentService = service;
	}

	GolfClubService getGolfClubService() {
		return golfClubService;
	}

	void setGolfClubService(GolfClubService golfClubService) {
		this.golfClubService = golfClubService;
	}

	Logger getLogger() {
		return logger;
	}

}
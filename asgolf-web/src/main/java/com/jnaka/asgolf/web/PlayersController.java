package com.jnaka.asgolf.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.service.PlayerService;

@RestController
public class PlayersController {

	public interface SeasonSummaryMixIn {
		@JsonIgnore
		List<Player> getPlayer();

		@JsonIgnore
		List<Season> getSeason();
	}

	public interface TournamentMixIn {
		@JsonIgnore
		Tournament getTournament();

		@JsonIgnore
		List<Round> getRounds();

		@JsonIgnore
		List<Winner> getWinners();

		@JsonIgnore
		List<Kp> getKps();
	}

	public interface PlayerMixIn {
		@JsonIgnore
		NavigableSet<SeasonSummary> getSeasonSummaries();
	}

	public interface CourseMixIn {
		@JsonIgnore
		List<CourseTee> getTees();
	}

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ObjectMapper mapper;

	@Autowired
	private PlayerService playerService;

	@RequestMapping("/players")
	public JsonNode getPlayers() throws Exception {
		List<Player> players = this.getPlayerService().getAll();
		JsonNode node = this.getMapper().valueToTree(players);
		return node;
	}

	@RequestMapping("/player/{id}")
	public JsonNode getPlayer(final @PathVariable Integer id) {
		Player player = this.getPlayerService().get(id);
		return this.getMapper().valueToTree(player);
	}

	@RequestMapping("/player-history")
	public JsonNode getPlayerHistory(final @RequestParam("id") Integer id) {
		ObjectMapper aMapper = new ObjectMapper();
		Map<Class<?>, Class<?>> mixin = new HashMap<>();
		mixin.put(Tournament.class, TournamentMixIn.class);
		mixin.put(Player.class, PlayerMixIn.class);
		mixin.put(Course.class, CourseMixIn.class);
		aMapper.setMixInAnnotations(mixin);

		Player player = this.getPlayerService().get(id);
		List<Round> rounds = this.getPlayerService().findRounds(player);
		return aMapper.valueToTree(rounds);
	}

	@RequestMapping("/player-summaries")
	public JsonNode getPlayerSummaries(final @RequestParam("id") Integer id) {
		ObjectMapper aMapper = new ObjectMapper();
		Map<Class<?>, Class<?>> mixin = new HashMap<>();
		mixin.put(SeasonSummary.class, SeasonSummaryMixIn.class);
		aMapper.setMixInAnnotations(mixin);

		NavigableSet<SeasonSummary> summaries = this.getPlayerService().getSeasonSummaries(id);
		return aMapper.valueToTree(summaries);
	}

	@RequestMapping(value = "/player-update", method = RequestMethod.POST)
	public JsonNode updatePlayer(final @RequestBody Player player) {
		this.getLogger().debug("player = {}", player);
		this.getPlayerService().update(player);
		return this.getMapper().valueToTree(player);
	}

	@RequestMapping(value = "/player-create", method = RequestMethod.POST)
	public JsonNode createPlayer() {
		this.getLogger().debug("create player");
		Player player = this.getPlayerService().newInstance();
		return this.getMapper().valueToTree(player);
	}

	ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			Map<Class<?>, Class<?>> mixin = new HashMap<>();
			mixin.put(Player.class, PlayerMixIn.class);
			mapper.setMixInAnnotations(mixin);
		}
		return mapper;
	}

	void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	PlayerService getPlayerService() {
		return playerService;
	}

	void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	Logger getLogger() {
		return logger;
	}

}
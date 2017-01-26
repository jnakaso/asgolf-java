package com.jnaka.asgolf.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnaka.asgolf.service.SiteManager;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.CourseTee;
import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;
import com.jnaka.golf.reports.json.JsonReport;
import com.jnaka.golf.service.SeasonService;

@RestController
public class SeasonsController {

	public interface TournamentMixIn {
		@JsonIgnore
		Season getSeason();

		@JsonIgnore
		List<Round> getRounds();

		@JsonIgnore
		List<Winner> getWinners();

		@JsonIgnore
		List<Kp> getKps();
	}

	public interface CourseMixIn {

		@JsonIgnore
		List<CourseTee> getTees();
	}

	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ObjectMapper mapper;

	@Autowired
	private SiteManager siteManager;

	@Autowired
	private SeasonService seasonService;

	@Autowired
	@Qualifier("jsonStandings")
	private JsonReport<Season> standings;

	@RequestMapping("/seasons")
	public List<Season> getSeasons() throws Exception {
		return this.getSeasonService().getAll();
	}

	@RequestMapping("/season/{id}")
	public Season getSeason(final @PathVariable Integer id) {
		return this.getSeasonService().get(id);
	}

	@RequestMapping("/season-summary/{id}")
	public Object getSeasonSummary(final @PathVariable Integer id) {
		Season season = this.getSeasonService().get(id);
		return this.getStandings().create(season);
	}

	@RequestMapping("/season-tournaments/{id}")
	public JsonNode getTournaments(final @PathVariable Integer id) {
		Season season = this.getSeasonService().get(id);
		List<Tournament> tournaments = this.getSeasonService().findTournaments(season);
		return this.getMapper().valueToTree(tournaments);
	}

	@RequestMapping("/season-update-totals/{id}")
	public Season updateTotals(final @PathVariable Integer id) {
		try {
			this.getLogger().debug("season-update-totals= {}", id);
			this.getSiteManager().updateTotals(id);
			return this.getSeasonService().get(id);
		} catch (IOException | JAXBException e) {
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(value = "/season-update", method = RequestMethod.POST)
	public Season updateSeason(final @RequestBody Season season) {
		this.getLogger().debug("season= {}", season);
		this.getSeasonService().update(season);
		return season;
	}

	@RequestMapping(value = "/season-create", method = RequestMethod.POST)
	public Season createSeason() {
		this.getLogger().debug("create season");
		return this.getSeasonService().newInstance();
	}

	ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
			Map<Class<?>, Class<?>> mixin = new HashMap<>();
			mixin.put(Tournament.class, TournamentMixIn.class);
			mixin.put(Course.class, CourseMixIn.class);
			mapper.setMixInAnnotations(mixin);
		}
		return mapper;
	}

	void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	SeasonService getSeasonService() {
		return seasonService;
	}

	void setSeasonService(SeasonService service) {
		this.seasonService = service;
	}

	SiteManager getSiteManager() {
		return this.siteManager;
	}

	JsonReport<Season> getStandings() {
		return standings;
	}

	void setStandings(JsonReport<Season> standings) {
		this.standings = standings;
	}

	Logger getLogger() {
		return logger;
	}

}
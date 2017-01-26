package com.jnaka.golf.domain.json;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.service.SeasonService;

/**
 * @author nakasones
 * 
 *         <pre>
 *     "courseName": "Auburn", 
 *     "date": "2013-06-23T07:00:00.000Z", 
 *     "id": 2, 
 *     "rating": "68.5", 
 *     "seasonID": 2013, 
 *     "slope": "121", 
 *     "type": "NORMAL"
 * </pre>
 * 
 */
@Component("tournamentsWriter")
public class TournamentsWriterImpl implements JsonWriter<Season> {

	@Autowired
	@Qualifier("SeasonService")
	private SeasonService seasonService;
	@Autowired
	private TournamentTransformer tournamentTransformer;

	@Override
	public boolean export(Season season, File file) {
		try {
			String data = this.writeTournaments(season);
			FileUtils.writeStringToFile(file, data);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	String writeTournaments(Season season) {
		List<Tournament> tournaments = this.getSeasonService().findTournaments(season);
		@SuppressWarnings("unchecked")
		Collection<JSONObject> jsObjects = CollectionUtils.collect(tournaments, this.getTournamentTransformer());
		JSONArray jsonArray = JSONArray.fromObject(jsObjects);
		return jsonArray.toString(1);
	}

	SeasonService getSeasonService() {
		return seasonService;
	}

	void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}

	TournamentTransformer getTournamentTransformer() {
		return tournamentTransformer;
	}

	void setTournamentTransformer(TournamentTransformer tournamentTransformer) {
		this.tournamentTransformer = tournamentTransformer;
	}

}

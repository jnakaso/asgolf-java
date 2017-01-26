package com.jnaka.golf.domain.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Winner;

@Component("tournamentWriter")
public class TournamentWriterImpl implements JsonWriter<Tournament> {

	/**
	 * <pre>
	 *     "flight": "A", 
	 *     "hole": 3, 
	 *     "id": 0, 
	 *     "player": "Allan Fukuyama", 
	 *     "playerID": 69, 
	 *     "tournamentID": 0
	 * </pre>
	 */
	private static final Transformer KP_TRANSFORMER = new Transformer() {

		@Override
		public Object transform(Object input) {
			Kp kp = (Kp) input;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", 0);
			map.put("flight", kp.getFlight() == null ? "" : kp.getFlight().name());
			map.put("hole", kp.getHole());
			map.put("player", kp.getPlayer().getFirstName() + " " + kp.getPlayer().getLastName());
			map.put("playerID", kp.getPlayer().getId());
			map.put("tournamentID", 0);
			return map;
		}
	};
	/**
	 * <pre>
	 *       "back": 46, 
	 *       "backNet": 39, 
	 *       "flight": "A", 
	 *       "front": 41, 
	 *       "frontNet": 34, 
	 *       "hdcp": 14, 
	 *       "holes": [ 3, 4, 4, 7, 5, 3, 5, 5, 5, 5, 6, 5, 4, 4, 6, 4, 5, 7], 
	 *       "id": 0, 
	 *       "player": "Mel Asato", 
	 *       "playerID": 3, 
	 *       "total": 87, 
	 *       "totalNet": 73
	 * </pre>
	 */
	private static final Transformer ROUND_TRANSFORMER = new Transformer() {

		@Override
		public Object transform(Object input) {
			Round round = (Round) input;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", 0);
			map.put("back", round.getBack());
			map.put("backNet", round.getBackNet());
			map.put("front", round.getFront());
			map.put("frontNet", round.getFrontNet());
			map.put("total", round.getTotal());
			map.put("totalNet", round.getTotalNet());
			map.put("flight", round.getFlight() == null ? "" : round.getFlight().name());
			map.put("player", round.getPlayer().getFirstName() + " " + round.getPlayer().getLastName());
			map.put("playerID", round.getPlayer().getId());
			map.put("hdcp", round.getHandicap());
			map.put("holes", round.getScores());
			return map;
		}
	};
	/**
	 * <pre>
	 *  "earnings": 20, 
	 *       "flt": "A", 
	 *       "hdcp": 11, 
	 *       "net": 68, 
	 *       "place": 1, 
	 *       "player": "Allan Fukuyama", 
	 *       "playerID": 69, 
	 *       "points": 9
	 * </pre>
	 */
	private static final Transformer WINNER_TRANSFORMER = new Transformer() {
		@Override
		public Object transform(Object input) {
			Winner winner = (Winner) input;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flt", winner.getRound().getFlight().name());
			map.put("hdcp", winner.getRound().getHandicap());
			map.put("net", winner.getRound().getTotalNet());
			map.put("place", winner.getFinish());
			map.put("player", winner.getRound().getPlayer().getFirstName() + " "
					+ winner.getRound().getPlayer().getLastName());
			map.put("playerID", winner.getRound().getPlayer().getId());
			map.put("points", winner.getPoints());
			map.put("earnings", winner.getEarnings());
			return map;
		}
	};

	@Autowired
	private TournamentTransformer tournamentTransformer;

	public boolean export(Tournament tournament, File file) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("kps", this.getKps(tournament));
			map.put("rounds", this.getRounds(tournament));
			map.put("tournament", this.getTournament(tournament));
			map.put("winners", this.getWinners(tournament));
			FileUtils.writeStringToFile(file, JSONObject.fromObject(map).toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private List<Map<String, Object>> getKps(Tournament tournament) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		CollectionUtils.collect(tournament.getKps(), KP_TRANSFORMER, list);
		return list;
	}

	private Object getRounds(Tournament tournament) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		CollectionUtils.collect(tournament.getRounds(), ROUND_TRANSFORMER, list);
		return list;
	}

	private Object getTournament(Tournament tournament) {
		return this.getTournamentTransformer().transform(tournament);
	}

	private Object getWinners(Tournament tournament) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		CollectionUtils.collect(tournament.getWinners(), WINNER_TRANSFORMER, list);
		return list;
	}

	TournamentTransformer getTournamentTransformer() {
		return tournamentTransformer;
	}

	void setTournamentTransformer(TournamentTransformer tournamentTransformer) {
		this.tournamentTransformer = tournamentTransformer;
	}

}

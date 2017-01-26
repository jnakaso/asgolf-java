package com.jnaka.golf.domain.json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jnaka.golf.dao.CourseDao;
import com.jnaka.golf.dao.PlayerDao;
import com.jnaka.golf.dao.SeasonDao;
import com.jnaka.golf.dao.TournamentReader;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.Flight;
import com.jnaka.golf.domain.Kp;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;

@Service("TournamentJsonReader")
public class TournamentReaderImpl implements TournamentReader {

	@Autowired
	@Qualifier("PlayerDao")
	private PlayerDao playerDao;
	@Autowired
	@Qualifier("CourseDao")
	private CourseDao courseDao;
	@Autowired
	@Qualifier("SeasonDao")
	private SeasonDao seasonDao;

	public Tournament read(File file) {
		try {
			FileReader reader = new FileReader(file);
			String jsonString = IOUtils.toString(reader);
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			Tournament tournament = this.getTournament(jsonObject);
			for (Round round : this.getRounds(jsonObject)) {
				tournament.addRound(round);
			}
			for (Kp kp : this.getKps(jsonObject)) {
				tournament.addKp(kp);
			}
			return tournament;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	Collection<Round> getRounds(JSONObject jsonObject) {
		JSONArray jsonRounds = jsonObject.getJSONArray("rounds");
		Collection<DynaBean> dRounds = JSONArray.toCollection(jsonRounds);
		Collection<Round> rounds = new ArrayList<Round>(dRounds.size());
		int i = 0;
		for (DynaBean dRound : dRounds) {
			Round round = this.createRound(dRound);
			round.setId(i++);
			rounds.add(round);
		}
		return rounds;
	}

	Round createRound(DynaBean dRound) {
		Round round = new Round();

		@SuppressWarnings("unchecked")
		List<Integer> holes = (List<Integer>) dRound.get("holes");
		round.setScores(holes.toArray(new Integer[18]));

		String flight = (String) dRound.get("flight");
		round.setFlight(StringUtils.isEmpty(flight) ? null : Flight.valueOf(flight));

		round.setHandicap((Integer) dRound.get("hdcp"));

		Integer playerId = (Integer) dRound.get("playerID");
		round.setPlayer(this.lookupPlayer(playerId));

		return round;
	}

	Kp createKp(DynaBean dKp) {
		Kp kp = new Kp();

		kp.setHole(dKp.get("hole").toString());

		Integer playerId = (Integer) dKp.get("playerID");
		kp.setPlayer(this.lookupPlayer(playerId));

		String flight = (String) dKp.get("flight");
		kp.setFlight(StringUtils.isEmpty(flight) ? null : Flight.valueOf(flight));

		return kp;
	}

	Player lookupPlayer(Integer playerId) {
		return this.getPlayerDao().load(playerId);
	}

	@SuppressWarnings("unchecked")
	Collection<Kp> getKps(JSONObject jsonObject) {
		JSONArray json = jsonObject.getJSONArray("kps");
		Collection<DynaBean> dKps = JSONArray.toCollection(json);
		Collection<Kp> kps = new ArrayList<Kp>(dKps.size());
		for (DynaBean dKp : dKps) {
			kps.add(this.createKp(dKp));
		}
		return kps;

	}

	Tournament getTournament(JSONObject jsonObject) {
		JSONObject jsTournament = (JSONObject) jsonObject.get("tournament");
		JsonConfig config = new JsonConfig();
		config.setRootClass(Tournament.class);
		config.setJavaPropertyFilter(this.createTournamentPropertyFilter());
		return (Tournament) JSONObject.toBean(jsTournament, config);
	}

	private PropertyFilter createTournamentPropertyFilter() {
		PropertyFilter filter = new PropertyFilter() {

			@Override
			public boolean apply(Object source, String name, Object value) {
				Tournament tour = (Tournament) source;
				if ("seasonID".equals(name)) {
					Season season = getSeasonDao().load((Number) value);
					tour.setSeason(season);
					return true;
				}
				if ("courseName".equals(name)) {
					Course course = getCourseDao().findByName((String) value);
					tour.setCourse(course);
					return true;
				}
				if ("date".equals(name)) {
					try {
						// 2012-03-10T00:30:35.818Z
						String dateString = StringUtils.substringBefore((String) value, "T");
						String[] params = { "yyyy-MM-dd" };
						Date date = DateUtils.parseDate(dateString, params);
						tour.setDate(date);
						return true;
					} catch (ParseException e) {
						e.printStackTrace();
						return false;
					}
				}
				return false;
			}
		};
		return filter;
	}

	public PlayerDao getPlayerDao() {
		return playerDao;
	}

	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	public CourseDao getCourseDao() {
		return courseDao;
	}

	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	public SeasonDao getSeasonDao() {
		return seasonDao;
	}

	public void setSeasonDao(SeasonDao seasonDao) {
		this.seasonDao = seasonDao;
	}

}

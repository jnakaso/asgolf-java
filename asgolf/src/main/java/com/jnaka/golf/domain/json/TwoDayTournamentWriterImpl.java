package com.jnaka.golf.domain.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.Tournament;
import com.jnaka.golf.domain.Tournament.Type;
import com.jnaka.golf.domain.TwoDaySummary;
import com.jnaka.golf.reports.ReportHelper;
import com.jnaka.golf.service.SeasonService;
import com.jnaka.golf.service.TournamentService;

/**
 * <Round flight="A" handicap="7" playerId="1" player="Bruce Kaneshiro"
 * overall="159" net="145" rounds="2"/>
 * 
 * 
 * @author jnakaso
 * 
 */
@Component("twoDayTournamentWriter")
public class TwoDayTournamentWriterImpl implements JsonWriter<Season> {
	private static final String PLAYER = "player";
	private static final String HANDICAP = "handicap";
	private static final String FLIGHT = "flight";
	private static final String DAY_ONE = "dayone";
	private static final String DAY_TWO = "daytwo";
	private static final String TOTAL = "total";
	private static final String TOTAL_NET = "totalNet";

	private static final Transformer TWO_DAY_TRANSFORMER = new Transformer() {

		@Override
		public Object transform(Object input) {
			TwoDaySummary summary = (TwoDaySummary) input;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(PLAYER, ReportHelper.getPlayerName(summary.getPlayer()));
			map.put(HANDICAP, summary.getHandicap());
			map.put(FLIGHT, ObjectUtils.toString(summary.getFlight()));
			map.put(DAY_ONE, summary.getDayOne());
			map.put(DAY_TWO, summary.getDayTwo());
			map.put(TOTAL, summary.getTotal());
			map.put(TOTAL_NET, summary.getTotalNet());
			return map;
		}
	};

	@Autowired
	private TournamentService tournamentService;
	@Autowired
	private SeasonService seasonService;

	@Override
	public boolean export(Season season, File file) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dayoneCourseName", this.getTournamentCourse(season, Type.DAY_ONE));
			map.put("daytwoCourseName", this.getTournamentCourse(season, Type.DAY_TWO));
			map.put("scores", this.getScores(season));
			FileUtils.writeStringToFile(file, JSONObject.fromObject(map).toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private String getTournamentCourse(Season season, Tournament.Type type) {
		List<Tournament> tours = this.getSeasonService().findTournaments(season, type);
		if (tours.isEmpty()) {
			return "";
		}
		return tours.get(0).getCourse().getName();
	}

	private List<Map<String, Object>> getScores(Season season) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		CollectionUtils.collect(this.getSummaries(season), TWO_DAY_TRANSFORMER, list);
		return list;
	}

	private List<TwoDaySummary> getSummaries(Season season) {
		List<TwoDaySummary> summaries = this.getTournamentService().getTwoDayRounds(season.getId());
		Collections.sort(summaries, new Comparator<TwoDaySummary>() {
			@Override
			public int compare(TwoDaySummary o1, TwoDaySummary o2) {
				int value = o1.getFlight().compareTo(o2.getFlight());
				if (value == 0) {
					value = o1.getTotalNet().compareTo(o2.getTotalNet());
				}
				return value;
			}
		});
		return summaries;
	}

	TournamentService getTournamentService() {
		return tournamentService;
	}

	void setTournamentService(TournamentService tournamentService) {
		this.tournamentService = tournamentService;
	}

	SeasonService getSeasonService() {
		return this.seasonService;
	}

	void setSeasonService(SeasonService seasonService) {
		this.seasonService = seasonService;
	}

}

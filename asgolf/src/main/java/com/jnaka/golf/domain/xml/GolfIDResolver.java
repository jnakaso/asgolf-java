package com.jnaka.golf.domain.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.Unmarshaller.Listener;

public class GolfIDResolver extends Listener {

	Map<Integer, Season> seasons = new HashMap<Integer, Season>();
	Map<Number, Player> players = new HashMap<Number, Player>();
	Map<Number, Course> courses = new HashMap<Number, Course>();
	Map<String, Tournament> tournaments = new HashMap<String, Tournament>();
	Map<String, Round> rounds = new HashMap<String, Round>();

	public void afterUnmarshal(Object target, Object parent) {
		if (target instanceof Season) {
			seasons.put(((Season) target).getId(), (Season) target);
		} else if (target instanceof Player) {
			players.put(((Player) target).getId(), (Player) target);
		} else if (target instanceof Course) {
			courses.put(((Course) target).getId(), (Course) target);
		} else if (target instanceof SeasonSummary) {
			SeasonSummary ref = (SeasonSummary) target;
			ref.setSeason(this.seasons.get(ref.getSeasonID()));
		} else if (target instanceof Tournament) {
			Tournament ref = (Tournament) target;
			ref.setCourse(this.courses.get(ref.getCourseID()));
			ref.setSeason(this.seasons.get(ref.getSeasonID()));
		} else if (target instanceof PlayerReference) {
			PlayerReference ref = (PlayerReference) target;
			ref.setPlayer(this.players.get(ref.getPlayerID()));
		}
	}

}

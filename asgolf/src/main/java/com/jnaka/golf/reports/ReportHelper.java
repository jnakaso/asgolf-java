package com.jnaka.golf.reports;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;

public class ReportHelper {

	static public String getPlayerName(Round round) {
		return getPlayerName(round.getPlayer());
	}

	static public String getPlayerName(Player player) {
		return player.getFirstName() + " " + player.getLastName();
	}

	static public String getCourseName(Round round) {
		return round.getTournament().getCourse().getName();
	}

	private ReportHelper() {

	}
}

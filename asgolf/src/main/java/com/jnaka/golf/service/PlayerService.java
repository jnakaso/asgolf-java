package com.jnaka.golf.service;

import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;

import com.jnaka.domain.EntityObjectService;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Round;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.domain.CourseTee;

public interface PlayerService extends EntityObjectService<Player> {

	public static final Comparator<Player> PLAYER_LAST_NAME_COMPARATOR = new Comparator<Player>() {
		@Override
		public int compare(Player o1, Player o2) {
			return o1.getLastName().compareToIgnoreCase(o2.getLastName());
		}
	};

	public List<Round> findRounds(Player player);

	public List<Round> findRounds(final Player player, int season);

	public List<Player> getAll(final boolean active);

	public void updateSummary(SeasonSummary summary);

	public Integer getCourseAdjustedHandicap(Player player, CourseTee courseTee);

	public NavigableSet<SeasonSummary> getSeasonSummaries(Integer id);

}

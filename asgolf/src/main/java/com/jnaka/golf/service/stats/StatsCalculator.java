package com.jnaka.golf.service.stats;

import java.util.List;

import com.jnaka.golf.domain.Season;

public interface StatsCalculator<T> {

	List<T> getEntries(Season season);

}

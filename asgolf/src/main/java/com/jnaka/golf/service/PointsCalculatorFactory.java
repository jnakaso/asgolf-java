package com.jnaka.golf.service;

import com.jnaka.golf.domain.Season;

public interface PointsCalculatorFactory {

	PointsCalculator getCalculator(Season season);

}

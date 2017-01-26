package com.jnaka.golf.service;

import com.jnaka.golf.domain.Season;

public interface HandicapCalculatorFactory {

	HandicapCalculator getCalculator(Season season);

}

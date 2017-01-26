package com.jnaka.golf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.HandicapCalculator;
import com.jnaka.golf.service.HandicapCalculatorFactory;

@Service
public class HandicapCalculatorFactoryImpl implements HandicapCalculatorFactory {

	@Autowired
	@Qualifier("FiveOfTen")
	private HandicapCalculator fiveOfTen;

	@Override
	public HandicapCalculator getCalculator(Season season) {
		switch (season.getHandicapPolicy()) {
		case FIVE_OF_TEN:
			return this.getFiveOfTen();
		default:
			throw new IllegalArgumentException("Season not covered.");
		}
	}

	public HandicapCalculator getFiveOfTen() {
		return fiveOfTen;
	}

	public void setFiveOfTen(HandicapCalculator fiveOfTen) {
		this.fiveOfTen = fiveOfTen;
	}

}

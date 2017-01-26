package com.jnaka.golf.service;

import java.util.List;

import com.jnaka.golf.domain.Winner;

public interface PrizeCalculator {

	public List<Winner> findWinners();

}

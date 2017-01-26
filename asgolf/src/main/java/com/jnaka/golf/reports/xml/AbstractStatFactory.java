package com.jnaka.golf.reports.xml;

import com.jnaka.golf.service.stats.StatsCalculator;
import com.jnaka.reports.AbstractDocumentFactory;

public abstract class AbstractStatFactory<T> extends AbstractDocumentFactory {

	private int seasonID;

	private StatsCalculator<T> calculator;

	public int getSeasonID() {
		return seasonID;
	}

	public void setSeasonID(int seasonID) {
		this.seasonID = seasonID;
	}

	public StatsCalculator<T> getCalculator() {
		return calculator;
	}

	public void setCalculator(StatsCalculator<T> calculator) {
		this.calculator = calculator;
	}

}

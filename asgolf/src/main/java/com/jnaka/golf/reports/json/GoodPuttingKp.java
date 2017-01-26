package com.jnaka.golf.reports.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.service.stats.AbstractPuttingKpCalculator;
import com.jnaka.golf.service.stats.StatsCalculator;

/**
 * @author nakasones

 */
@Component("jsonGoodPuttingKp")
public class GoodPuttingKp extends AbstractPuttingReport {

	@Autowired
	@Qualifier("goodPuttingKpCalculator")
	public void setCalculator(StatsCalculator<AbstractPuttingKpCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}

}

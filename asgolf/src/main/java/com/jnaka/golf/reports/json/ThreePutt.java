package com.jnaka.golf.reports.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.service.stats.StatsCalculator;
import com.jnaka.golf.service.stats.ThreePuttCalculator;

/**
 * @author nakasones

 */
@Component("jsonThreePutt")
public class ThreePutt extends AbstractPuttingReport {

	@Autowired
	@Qualifier("threePuttCalculator")
	public void setCalculator(StatsCalculator<ThreePuttCalculator.Entry> calculator) {
		super.setCalculator(calculator);
	}

}

package com.jnaka.golf.service.stats;

import org.springframework.stereotype.Component;

/**
 * @author nakasones
 * 
 *  <pre>
 *  <player player="Bill Tadehara" active="true">
 * 		<kp playDate="Jun 3, 2012" course="Classic" hole="15" score="4"/>
 * 		<kp playDate="Mar 4, 2012" course="North Shore" hole="13" score="4"/>
 * 		<kp playDate="Jun 4, 2011" course="Classic" hole="15" score="4"/>
 *  </player>
 *  </pre>
 */
@Component
public class GoodPuttingKpCalculator extends AbstractPuttingKpCalculator {

	@Override
	protected boolean scoreTest(Integer score) {
		return score < 3;
	}

}

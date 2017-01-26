package com.jnaka.golf.service.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { PlayerServiceImplTest.class, //
		RoundServiceImplTest.class, //
		TournamentServiceImplTest.class, //
		SeasonServiceImplTest.class, //
		FiveOfTenHandicapCalculatorTest.class, //
		PrizeCalculatorImplTest.class, //
		PointsCalculatorTest.class })
public class GolfServiceSuite {

}

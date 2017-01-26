package com.jnaka.golf.reports.xml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { SandbaggerTest.class, //
		VardonTest.class, //
		MostImprovedTest.class })
public class GolfReportSuite {

}

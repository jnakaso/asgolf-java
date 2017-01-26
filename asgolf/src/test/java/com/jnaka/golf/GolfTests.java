package com.jnaka.golf;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.jnaka.golf.dao.GolfDaoSuite;
import com.jnaka.golf.domain.xml.GolfXmlSuite;
import com.jnaka.golf.reports.xml.GolfReportSuite;
import com.jnaka.golf.service.impl.GolfServiceSuite;

@RunWith(Suite.class)
@SuiteClasses( { GolfDaoSuite.class, //
		GolfReportSuite.class, //
		GolfServiceSuite.class, //
		GolfXmlSuite.class //
})
public class GolfTests {

}

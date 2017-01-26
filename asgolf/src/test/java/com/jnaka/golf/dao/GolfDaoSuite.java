package com.jnaka.golf.dao;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { TournamentReaderTest.class, //
		CourseDaoTest.class, //
		PlayerDaoTest.class, //
		TournamentDaoTest.class, //
		SeasonDaoTest.class })
public class GolfDaoSuite {

}

package com.jnaka.golf;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import com.jnaka.golf.dao.DataStore;
import com.jnaka.golf.domain.Course;
import com.jnaka.golf.domain.GolfClub;
import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.json.JsonWriter;
 
public class GolfJsonExporterTest {

	private Mockery mockery = new Mockery();

	@After
	public void teardown() {
		this.mockery.assertIsSatisfied();
	}

	@Test
	@Ignore
	public void testExport() {
		final GolfJsonExporter exporter = this.createExporter();
		final List<Course> courses = Arrays.asList(new Course());
		final List<Player> players = Arrays.asList(new Player());
		final GolfClub club = this.createClub();
		club.setCourses(courses);
		club.setPlayers(players);

		ExpectationBuilder expectations = new Expectations() {
			{
				this.oneOf(exporter.getDataStore()).getGolfClub();
				this.will(returnValue(club));
				this.oneOf(exporter.getDataStore()).getGolfClub();
				this.will(returnValue(club));
				this.oneOf(exporter.getDataStore()).getGolfClub();
				this.will(returnValue(club));
				this.oneOf(exporter.getCoursesWriter()).export(with(Is.is(courses)), with(any(File.class)));
				this.oneOf(exporter.getPlayersWriter()).export(with(Is.is(players)), with(any(File.class)));
			}
		};
		this.mockery.checking(expectations);
		exporter.exportAll();

	}

	private GolfClub createClub() {
		GolfClub club = new GolfClub();

		return club;
	}

	@SuppressWarnings("unchecked")
	private GolfJsonExporter createExporter() {
		GolfJsonExporter exporter = new GolfJsonExporter();
		@SuppressWarnings("rawtypes")
		JsonWriter writer = this.mockery.mock(JsonWriter.class);
		exporter.setCoursesWriter(writer);
		exporter.setPlayersWriter(writer);
		exporter.setDataStore(this.mockery.mock(DataStore.class));
		return exporter;
	}

}

package com.jnaka.golf.domain.json;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.reports.json.JsonReport;
import com.jnaka.golf.reports.json.StatsWriterImpl;

public class StatsWriterImplTest {
	private Mockery mockery = new Mockery();

	@After
	public void teardown() {
		this.mockery.assertIsSatisfied();
	}

	@Test
	public void test() {
		final StatsWriterImpl writer = this.createWriter();
		final Season season = new Season();
		final File file = new File("tmep");
		final List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("playerName", "Jeff");
		results.add(map);
		ExpectationBuilder expectations = new Expectations() {
			{
				this.oneOf(writer.getSubReports().values().iterator().next()).create(season);
				this.will(returnValue(results));
			}
		};
		this.mockery.checking(expectations);
		boolean actual = writer.export(season, file);
		Assert.assertTrue(actual);
	}

	@SuppressWarnings("unchecked")
	private StatsWriterImpl createWriter() {
		StatsWriterImpl writer = new StatsWriterImpl();
		@SuppressWarnings("rawtypes")
		JsonReport mockReport = this.mockery.mock(JsonReport.class);
		writer.getSubReports().put("vardon", mockReport);
		return writer;
	}

}

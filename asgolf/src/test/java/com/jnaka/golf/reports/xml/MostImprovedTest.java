package com.jnaka.golf.reports.xml;

import java.util.Arrays;

import org.dom4j.Document;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.stats.MostImprovedCalculator;
import com.jnaka.golf.service.stats.StatsCalculator;

public class MostImprovedTest {

	private Mockery mockery = new Mockery();

	@Test
	public void testCreate() {
		final MostImproved reporter = this.createReporter();
		reporter.setSeasonID(2001);

		final Player player1 = new Player();
		player1.setId(1);

		final MostImprovedCalculator.Entry entry = new MostImprovedCalculator.Entry(player1, 1, 10f, 0.1f, 8f, 0.2f);

		Expectations expectations = new Expectations() {
			{
				this.one(reporter.getCalculator()).getEntries(with(any(Season.class)));
				this.will(returnValue(Arrays.asList(entry)));
			}
		};
		this.mockery.checking(expectations);
		Document doc = reporter.create();

		Assert.assertNotNull(doc);
	}

	@SuppressWarnings("unchecked")
	private MostImproved createReporter() {
		MostImproved reporter = new MostImproved();
		reporter.setCalculator(this.mockery.mock(StatsCalculator.class));
		return reporter;
	}
}

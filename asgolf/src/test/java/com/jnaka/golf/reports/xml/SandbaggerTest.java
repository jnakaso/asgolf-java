package com.jnaka.golf.reports.xml;

import java.util.Arrays;
import java.util.List;

import org.dom4j.Document;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.Season;
import com.jnaka.golf.service.stats.SandbaggerCalculator;
import com.jnaka.golf.service.stats.StatsCalculator;

public class SandbaggerTest {

	private Mockery mockery = new Mockery();

	@Test
	public void testCreate() {
		final Sandbagger bagger = this.createReporter();
		bagger.setSeasonID(2001);

		final Player player1 = new Player();
		player1.setId(1);

		final SandbaggerCalculator.Entry entry = new SandbaggerCalculator.Entry(player1, 1, 2, 3, 4);

		final List<SandbaggerCalculator.Entry> entries = Arrays.asList(entry);

		Expectations expectations = new Expectations() {
			{
				this.one(bagger.getCalculator()).getEntries(with(any(Season.class)));
				this.will(returnValue(entries));
			}
		};
		this.mockery.checking(expectations);
		Document doc = bagger.create();

		Assert.assertNotNull(doc);
		Assert.assertEquals(1, doc.getRootElement().selectNodes("*").size());
	}

	@SuppressWarnings("unchecked")
	private Sandbagger createReporter() {
		Sandbagger reporter = new Sandbagger();
		reporter.setCalculator(this.mockery.mock(StatsCalculator.class));
		return reporter;
	}

}

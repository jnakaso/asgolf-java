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
import com.jnaka.golf.service.stats.StatsCalculator;
import com.jnaka.golf.service.stats.VardonCalculator;

public class VardonTest {

	private Mockery mockery = new Mockery();

	@Test
	public void testCreate() {
		final Vardon reporter = this.createReporter();
		reporter.setSeasonID(2001);

		final Player player1 = new Player();
		player1.setId(1);

		final VardonCalculator.Entry entry = new VardonCalculator.Entry (player1, 1, 80.f);

		final List<VardonCalculator.Entry> entries = Arrays.asList(entry);

		Expectations expectations = new Expectations() {
			{
				this.one(reporter.getCalculator()).getEntries(with(any(Season.class)));
				this.will(returnValue(entries));
			}
		};
		this.mockery.checking(expectations);
		Document doc = reporter.create();

		Assert.assertNotNull(doc);
		Assert.assertEquals(1, doc.getRootElement().selectNodes("*").size());
	}

	@SuppressWarnings("unchecked")
	private Vardon createReporter() {
		Vardon reporter = new Vardon();
		reporter.setCalculator(this.mockery.mock(StatsCalculator.class));
		return reporter;
	}
}

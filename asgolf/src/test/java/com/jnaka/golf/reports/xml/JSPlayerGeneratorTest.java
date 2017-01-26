package com.jnaka.golf.reports.xml;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.junit.Test;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.service.PlayerService;

public class JSPlayerGeneratorTest {
	private Mockery mockery = new Mockery();

	@Test
	public void test() throws Exception {
		final JSPlayerGenerator generator = this.createCenerator();
		final Writer writer = new StringWriter();
		Player player = new Player();
		player.setId(10);
		player.setFirstName("firstName");
		player.setLastName("lastName");
		player.setHandicap(10.3f);
		final List<Player> players = Arrays.asList(player);
		ExpectationBuilder expectations = new Expectations() {
			{
				this.one(generator.getPlayerService()).getAll(true);
				this.will(returnValue(players));
			}
		};
		this.mockery.checking(expectations);
		generator.write(writer);
	}

	private JSPlayerGenerator createCenerator() {
		JSPlayerGenerator generator = new JSPlayerGenerator();
		generator.setPlayerService(this.mockery.mock(PlayerService.class));
		return generator;
	}
}

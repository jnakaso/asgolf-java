package com.jnaka.golf.reports.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.service.PlayerService;

public class JSPlayerGenerator {

	static final String OUTPUT = "new JSPlayer(%d, '%s', '%s', %f3.1);\n";
	private PlayerService playerService;

	public void write(Writer writer) throws IOException {
		List<Player> players = this.getPlayerService().getAll(true);
		for (Player player : players) {
			writer.append(String.format(OUTPUT, player.getId(), player.getFirstName(), player.getLastName(),
					player.getHandicap()));
		}
	}

	public PlayerService getPlayerService() {
		return playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

}

package com.jnaka.golf.domain.json;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.domain.SeasonSummary;
import com.jnaka.golf.service.PlayerService;

@Component("playersWriter")
public class PlayersWriterImpl implements JsonWriter<List<Player>>{

	static Transformer PLAYER_TO_JS_PLAYER = new Transformer() {
		@Override
		public Object transform(Object input) {
			JsonConfig config = new JsonConfig();
			config.setIgnoreTransientFields(true);
			config.setJsonPropertyFilter(new PropertyFilter() {

				@Override
				public boolean apply(Object arg0, String arg1, Object arg2) {
					return "seasonSummaries".equals(arg1);
				}
			});

			JsonConfig sConfig = new JsonConfig();
			sConfig.setIgnoreTransientFields(true);
			sConfig.setJsonPropertyFilter(new PropertyFilter() {
				List<String> properties = Arrays.asList("player", "season", "playerName");

				@Override
				public boolean apply(Object arg0, String arg1, Object arg2) {
					return properties.contains(arg1);
				}
			});

			Player player = (Player) input;
			JSONObject bean = JSONObject.fromObject(player, config);
			JSONArray summaries = new JSONArray();
			for (SeasonSummary summary : player.getSeasonSummaries()) {
				JSONObject jSummary = JSONObject.fromObject(summary, sConfig);
				summaries.add(jSummary);
			}
			bean.element("seasonSummaries", summaries);
			return bean;
		}
	};

	@Autowired
	@Qualifier("PlayerService")
	private PlayerService playerService;

	String writePlayers(List<Player> players) {
		@SuppressWarnings("unchecked")
		Collection<JSONObject> jsPlayers = CollectionUtils.collect(players, PLAYER_TO_JS_PLAYER);
		JSONArray jsonArray = JSONArray.fromObject(jsPlayers);
		return jsonArray.toString(1);
	}

	public boolean export(List<Player> players, File file) {
		try {
			String data = this.writePlayers(players);
			FileUtils.writeStringToFile(file, data);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public PlayerService getPlayerService() {
		return playerService;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

}

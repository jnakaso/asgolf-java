package com.jnaka.golf.domain.json;

import java.io.File;
import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Player;
import com.jnaka.golf.reports.json.JsonReport;

@Component("playerWriter")
public class PlayerWriterImpl implements JsonWriter<Player> {

	@Autowired
	@Qualifier("jsonPlayerLatestRounds")
	private JsonReport<Player> report;

	public boolean export(Player player, File file) {
		try {
			Object playerMap = this.getReport().create(player);
			FileUtils.writeStringToFile(file, JSONObject.fromObject(playerMap).toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public JsonReport<Player> getReport() {
		return report;
	}

	public void setReport(JsonReport<Player> report) {
		this.report = report;
	}

}

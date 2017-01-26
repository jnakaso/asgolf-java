package com.jnaka.golf.reports.json;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Season;
import com.jnaka.golf.domain.json.JsonWriter;

@Component("statsWriter")
public class StatsWriterImpl implements JsonWriter<Season> {

	private Map<String, JsonReport<Season>> subReports = new HashMap<String, JsonReport<Season>>();

	@Override
	public boolean export(Season season, File file) {
		try {
			String data = this.writeStats(season);
			FileUtils.writeStringToFile(file, data);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	String writeStats(Season season) {
		JSONObject stats = new JSONObject();
		for (Map.Entry<String, JsonReport<Season>> entry : this.getSubReports().entrySet()) {
			stats.element(entry.getKey(), entry.getValue().create(season));
		}
		return stats.toString(1);
	}

	public Map<String, JsonReport<Season>> getSubReports() {
		return subReports;
	}

	public void setSubReports(Map<String, JsonReport<Season>> subReports) {
		this.subReports = subReports;
	}

}

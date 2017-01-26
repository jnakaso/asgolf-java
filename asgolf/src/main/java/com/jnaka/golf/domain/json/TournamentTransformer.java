package com.jnaka.golf.domain.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Tournament;

@Component
public class TournamentTransformer implements Transformer {
	@Override
	public Object transform(Object input) {
		Tournament tournament = (Tournament) input;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", tournament.getId().toString());
		map.put("seasonID", tournament.getSeasonID());
		map.put("courseName", tournament.getCourse().getName());
		map.put("date", this.convert(tournament.getDate()));
		map.put("rating", tournament.getRating().toString());
		map.put("slope", tournament.getSlope().toString());
		map.put("type", tournament.getType().name());
		return map;
	}

	public String convert(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		DateFormat form = new SimpleDateFormat("MM-dd-yyyy");
		return form.format(date);
	}
}

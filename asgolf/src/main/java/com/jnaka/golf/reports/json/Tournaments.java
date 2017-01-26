package com.jnaka.golf.reports.json;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jnaka.golf.domain.Tournament;

/**
 * 
 * 
 * <pre>
 *   {
 *     "courseName": "Auburn", 
 *     "date": "2013-06-23T07:00:00.000Z", 
 *     "id": 2, 
 *     "rating": "68.5", 
 *     "seasonID": 2013, 
 *     "slope": "121", 
 *     "type": "NORMAL"
 *   }
 * </pre>
 * 
 * @author nakasones
 * 
 */
@Component("jsonTournaments")
public class Tournaments implements JsonReport<Tournament> {

	@Override
	public Object create(Tournament tournament) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", tournament.getId().toString());
		map.put("seasonID", tournament.getSeasonID());
		map.put("courseName", tournament.getCourse().getName());
		map.put("date", tournament.getDate());
		map.put("rating", tournament.getRating().toString());
		map.put("slope", tournament.getSlope().toString());
		map.put("type", tournament.getType().name());
		return map;
	}

}

package com.jnaka.golf;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * <pre>
 * data
 * 	- #year
 *    tournaments.json
 *    tournament_#id.json
 *    stats.json
 *  - player
 *    player_#id.json
 *  seasons.json
 *  courses.json
 *  players.json
 * </pre>
 * 
 * @author nakasones
 * 
 */

public class GolfFunnies {

	public static void main(String args[]) {
		try {
			@SuppressWarnings("unchecked")
			List<String> lines = FileUtils.readLines(new File(args[0]));

			StringBuilder builder = new StringBuilder("{\"funnies\" : [");

			boolean hasOne = false;
			for (String line : lines) {
				if (hasOne) {
					builder.append(",");
				}
				builder.append("\"" + line + "\"");
				hasOne = true;
			}
			builder.append("]}");
			FileUtils.writeStringToFile(new File(args[1]), builder.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

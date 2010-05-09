/*	
 * Copyright 2010 Mex (ellism88@gmail.com)
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package Google;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import AndrewCassidy.PluggableBot.DefaultPlugin;

public class Google extends DefaultPlugin{
	private final String HTTP_REFERER = "http://www.example.com/";

	private String makeQuery(String query) {
		try {
			// Convert spaces to +, etc. to make a valid URL
			query = URLEncoder.encode(query, "UTF-8");

			URL url = new URL(
					"http://ajax.googleapis.com/ajax/services/search/web?start=0&rsz=large&v=1.0&q="
							+ query);
			URLConnection connection = url.openConnection();
			connection.addRequestProperty("Referer", HTTP_REFERER);

			// Get the JSON response
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			String response = builder.toString();
			JSONObject json = new JSONObject(response);

			System.out.println("Total results = "
					+ json.getJSONObject("responseData")
							.getJSONObject("cursor").getString(
									"estimatedResultCount"));

			JSONArray ja = json.getJSONObject("responseData").getJSONArray(
					"results");

			System.out.println("\nResults:");
				StringBuilder s = new StringBuilder();
				JSONObject j = ja.getJSONObject(0);
				s.append(j.getString("titleNoFormatting"));
				s.append(" : ");
				s.append(j.getString("url"));
				return s.toString();
		} catch (Exception e) {
			System.err.println("Something went wrong...");
			e.printStackTrace();
		}
		return null;
	}

		
	@Override
	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (message.startsWith("!find")) {
			String result = makeQuery(message.substring(6));
			if (result == null ) {
				bot.Message(channel, sender, "Search Failed");
			} else {
				bot.Message(channel, sender, result);
			}
		}
	}

	@Override
	public String getHelp() {
		return "Do a google seach with !find <search>";
	}
}

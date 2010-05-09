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

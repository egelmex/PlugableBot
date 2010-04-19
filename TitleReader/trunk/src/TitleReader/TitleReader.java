package TitleReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import AndrewCassidy.PluggableBot.DefaultPlugin;

public class TitleReader extends DefaultPlugin {

	private static final Pattern p = Pattern
			.compile("\\<title\\>(.*)\\<\\/title\\>");

	@Override
	public String getHelp() {
		return "Gets the title of pages from mentioned urls";
	}

	@Override
	public void onMessage(final String channel, final String sender,
			final String login, final String hostname, final String message) {

		Runnable r = new Runnable() {

			@Override
			public void run() {
				// separete input by spaces ( URLs don't have spaces )
				String[] parts = message.split("\\s");

				URLConnection connection = null;
				// Attempt to convert each item into an URL.
				for (String item : parts)
					try {
						URL url = new URL(item);
						System.out.println("'" + url.getProtocol() + "'");

						if (url.getProtocol().equals("http")
								|| url.getProtocol().equals("http")) {
							connection = url.openConnection();

							if (connection.getContentType().startsWith(
									"text/html")) {
								System.out.println(connection.getContentType());

								BufferedReader in = new BufferedReader(
										new InputStreamReader(url.openStream()));

								String inputLine;
								StringBuilder sb = new StringBuilder();

								while ((inputLine = in.readLine()) != null)
									sb.append(inputLine);

								DefaultHttpClient httpclient = new DefaultHttpClient();
								HttpPost post = new HttpPost(
										"http://snipr.com/site/getsnip");

								List<NameValuePair> nvps = new ArrayList<NameValuePair>();
								nvps.add(new BasicNameValuePair("sniplink", url
										.toString()));
								nvps.add(new BasicNameValuePair("snipuser",
										"mex"));
								nvps.add(new BasicNameValuePair("snipapi",
										"b23ff81382e8ff92307f4c56951f2815"));
								nvps
										.add(new BasicNameValuePair("snipnick",
												""));
								nvps
										.add(new BasicNameValuePair(
												"sniptitle", ""));
								nvps
										.add(new BasicNameValuePair(
												"snipowner", ""));
								nvps.add(new BasicNameValuePair("snipformat",
										"simple"));
								nvps.add(new BasicNameValuePair("snippk", ""));

								post.setEntity(new UrlEncodedFormEntity(nvps,
										HTTP.UTF_8));

								HttpResponse response = httpclient
										.execute(post);
								HttpEntity entity = response.getEntity();

								BufferedReader in2 = new BufferedReader(
										new InputStreamReader(entity
												.getContent()));

								String inputLine2;
								StringBuilder sb2 = new StringBuilder();

								while ((inputLine2 = in2.readLine()) != null)
									sb2.append(inputLine2);

								System.out.println(sb2);

								Matcher m = p.matcher(sb);
								if (m.find()) {
									bot.Message(channel, sb2 + " : "
											+ m.group(1));
									System.out
											.println(sb2 + " : " + m.group(1));
								}
							}

						}

					} catch (MalformedURLException e) {
					} catch (IOException e) {
					} finally {
					}

			}
		};

		new Thread(r).start();
	}

	public static void main(String[] args) {
		TitleReader t = new TitleReader();
		t.onMessage(null, null, null, null, "hello http://google.com fish");
		System.out.println();
		t.onMessage(null, null, null, null, "hello google.com fish");

	}

}

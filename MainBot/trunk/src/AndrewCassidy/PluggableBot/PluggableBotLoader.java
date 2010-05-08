/*
 * Copyright	Mex (ellism88@gmail.com)	2010
 * Copyright	Andee 		2007
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

package AndrewCassidy.PluggableBot;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

/**
 * 
 * @author m.ellis (ellism88@gmail.com)
 */
class PluggableBotLoader implements Runnable {
	private String name;
	private URL[] urls;
	private PluggableBot bot;
	
	private static final Logger log = Logger.getLogger(PluggableBotLoader.class
			.getName());

	public PluggableBotLoader(String name, URL[] urls, PluggableBot bot) {
		this.name = name;
		this.urls = urls;
		this.bot = bot;
	}

	@Override
	public void run() {
		URLClassLoader newLoader = new URLClassLoader(urls);
		try {
			Plugin p = (Plugin) newLoader.loadClass(name + "." +name).newInstance();
			bot.addPlugin(name, p);// loadedPlugins.put(name, p);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			log.warning("Could not find class " + name + "." + name + " looked in...");
			for (URL url : urls)
				log.warning("looked in:" + url);
			e.printStackTrace();
			try {
				Plugin p = (Plugin) newLoader.loadClass(name).newInstance();
				bot.addPlugin(name, p);// loadedPlugins.put(name, p);
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}

		}

	}
}
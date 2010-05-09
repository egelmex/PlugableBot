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


package EmailReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import AndrewCassidy.PluggableBot.DefaultPlugin;
import AndrewCassidy.PluggableBot.PluggableBot;


public class EmailReader extends DefaultPlugin {

	private List<Connection> connections = new ArrayList<Connection>();
	
	private String location = "emailreader";

	public EmailReader() {
		
		File file = new File(location);
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				if (f.getName().endsWith(".properties")) {
					try {
						FileInputStream fis = new FileInputStream(f);
						Properties p = new Properties();
						p.load(fis);
						Connection c = new Connection(p, this); 
						connections.add(c);
					} catch (Exception e) {
					}
					
				}
			}
		}
		
		
		for (Connection x : connections) {
			new Thread(x).start();
		}
	}

	@Override
	public void setBot(PluggableBot bot) {
		super.setBot(bot);
	}

	@Override
	public void unload() {
		
		for (Connection con : connections) {
			try {
				con.kill();
				
				Properties p = con.getProps();
				
				File f = new File (location + "/"
						+ p.getProperty("username") + "_" + p.getProperty("server") + ".properties");
				System.out.println(f.toString());
				FileOutputStream os = new FileOutputStream(f);
				p.store(os, "");
				
				os.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}

		
		
	}

	public static void main(String[] args) throws InterruptedException {
		EmailReader m = new EmailReader();
		Thread.sleep(60 * 1000 * 10);
		m.unload();
	}

	@Override
	public String getHelp() {
		return "Reads Subjects of recieved mail in a mailbox.";
	}

}

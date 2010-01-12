import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import AndrewCassidy.PluggableBot.DefaultPlugin;
import AndrewCassidy.PluggableBot.Plugin;

import com.eleventytwo.EmailReader.Connection;

public class EmailReader extends DefaultPlugin {

	private List<Connection> connections = new ArrayList<Connection>();
	
	private String location = "emailreader";

	public EmailReader() {
		
		
		File file = new File(location);
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				if (f.getName().endsWith(".xml")) {
					try {
						FileInputStream fis = new FileInputStream(f);
						XMLDecoder decoder = new XMLDecoder(fis);
						Connection c = (Connection)decoder.readObject();
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
	public void unload() {

		for (Connection con : connections) {
			try {
				System.out.println(con.getUsername());
				
				File f = new File (location + "/"
						+ con.getUsername() + "_" + con.getServer() + ".xml");
				System.out.println(f.toString());
				FileOutputStream os = new FileOutputStream(f);
				XMLEncoder encoder = new XMLEncoder(os);
				encoder.writeObject(con);
				encoder.close();
			} catch (FileNotFoundException e) {
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

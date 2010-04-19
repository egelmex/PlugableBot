package AndrewCassidy.PluggableBot;
/**
 * @author mex
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;

public class IgnoreLib {
	
	String ingoreListName;
	File ignoreListf;
	ArrayList<String> ignoreLista = new ArrayList<String>();

	public IgnoreLib(Object object, String name) {
		this.ingoreListName = object.getClass().getName() + "-" + name;
		openIgnoreListf();
	}
	
	public IgnoreLib(Class<?> clas, String name) {
		this.ingoreListName = clas.getName() + "-" + name;
		openIgnoreListf();
	}
	
	private void openIgnoreListf() {
		FileReader fr = null;
		BufferedReader br = null;
        try {
            File f = new File(ingoreListName);
            if (!f.exists()) {
                System.out.println("Creating ignore list : " + ingoreListName);
                f.createNewFile();
            }
            
                fr = new FileReader(f);
                br = new BufferedReader(fr);
                
                String ignore;
                
                while ((ignore = br.readLine()) != null) {
                    ignoreLista.add(ignore);
                }
            } catch (FileNotFoundException ex) {
                throw new IllegalSelectorException();
            } catch (IOException ex) {
                throw new IllegalSelectorException();
            } finally {
            	if (br != null)
					try { br.close(); } catch (IOException e) {}
            	if (fr != null)
            		try { fr.close(); } catch (IOException e) {}
            }
	}
	
	public boolean ignore(String name) {
		return ignoreLista.contains(name.toLowerCase());
	}
	
	public void addIgnore (String name) {
		if (!ignoreLista.contains(name.toLowerCase())) {
			ignoreLista.add(name.toLowerCase());
			serialiseIgnoreList(ignoreLista);
		}
	}
	
	public void removeIgnore(String name) {
		if (!ignoreLista.contains(name.toLowerCase())) {
			ignoreLista.remove(name.toLowerCase());
			ignoreLista.add(name.toLowerCase());
			serialiseIgnoreList(ignoreLista);
		}
	}
	
	private void serialiseIgnoreList(ArrayList<String> list) {
		BufferedWriter out = null;
		FileWriter fw = null;
		
	    try {
	    	fw = new FileWriter(ignoreListf);
	        out = new BufferedWriter(fw);
	        
	        for (String s : list) {
	        	out.write(s);
	        }
	        
	    } catch (IOException e) {
	    	System.out.println("Failing to serialse ignore list :(");
	    } finally {
        	if (fw != null)
				try { fw.close(); } catch (IOException e) {}
        	if (out != null)
        		try { out.close(); } catch (IOException e) {}
	    }

	}
	
	
}

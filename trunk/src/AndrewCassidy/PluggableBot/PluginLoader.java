/*
 * PluginLoader.java
 *
 * Created on 09 October 2007, 18:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package AndrewCassidy.PluggableBot;

import java.io.*;
/**
 *
 * @author Administrator
 */
public class PluginLoader extends ClassLoader {
    
    /** Creates a new instance of PluginLoader */
    public PluginLoader() {
        
    }
    
    public Plugin loadPlugin(String name) throws Exception
    {
        Class c;/* = findLoadedClass(name);
        if (c == null)
        {*/
            try
            {
                c = findClass(name);
            }
            catch (Exception e)
            {
                throw new InstantiationException("Class file could not be loaded");
            }
        //}
        return (Plugin)c.newInstance();
    }
    
    protected Class findClass(String name) {
        byte[] b = loadClassData(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) {
        try {
            FileInputStream s = new FileInputStream("plugins/" + name.replace('.', '/') + ".class");
        
            byte[] b = new byte[(int)s.getChannel().size()];
            s.read(b);
            return b;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

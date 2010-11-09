package Comic;

/* 
Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

This file is part of ComicBot.

This software is dual-licensed, allowing you to choose between the GNU
General Public License (GPL) and the www.jibble.org Commercial License.
Since the GPL may be too restrictive for use in a proprietary application,
a commercial license is also provided. Full license information can be
found at http://www.jibble.org/licenses/

$Author: pjm2 $
$Id: ComicTest.java,v 1.3 2004/02/01 13:19:54 pjm2 Exp $

*/

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.font.*;
import java.text.*;
import javax.imageio.*;

/**
 * This is a big nasty dirty hack.
 * This code does not come with any warranty or technical support whatsoever.
 *
 * @author Paul Mutton http://www.jibble.org/comicbot/
 */
public class ComicWrite {
    
    public static BufferedImage getTextImage(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB );
        Graphics2D g2d = (Graphics2D)image.getGraphics();

        for (int fontSize = 50; fontSize > 4; fontSize--) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.black);
            //g2d.drawRect(0, 0, width, height);
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Font font = new Font("SansSerif", Font.PLAIN, fontSize);
            g2d.setFont(font);
            
            int borderSize = 3;
            Point pen = new Point(borderSize, borderSize);
            FontRenderContext frc = g2d.getFontRenderContext();
       
            // let styledText be an AttributedCharacterIterator containing at least
            // one character
            AttributedString astring = new AttributedString(text, font.getAttributes());
            AttributedCharacterIterator styledText = astring.getIterator();
       
            LineBreakMeasurer measurer = new LineBreakMeasurer(styledText, frc);
            float wrappingWidth = width - 2*borderSize;
       
            while (measurer.getPosition() < text.length()) {
                TextLayout layout = measurer.nextLayout(wrappingWidth);
                pen.y += layout.getAscent();
                float dx = layout.isLeftToRight() ?
                    0 : (wrappingWidth - layout.getAdvance());
                layout.draw(g2d, pen.x + dx, pen.y);
                pen.y += layout.getDescent() + layout.getLeading();
            }
            
            if (pen.y < height) {
                break;
            }
        }
        return image;
    }
    
    public static void addText(BufferedImage image, String text, int x, int y, int width, int height) {
        BufferedImage textImage = getTextImage(text, width, height);
        Graphics2D g2d = (Graphics2D)image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(textImage, x, y, null);
    }

    // Takes an array of texts (things people say) and an associated
    // array that contains the nick (the person who said the thing).    
    public static boolean createCartoonStrip(File outputDirectory, String channel, String[] texts, String[] nicks) throws IOException {
        // Find all ini files in the data directory.
        File[] filenames = new File("./data").listFiles();
        ArrayList inis = new ArrayList();
        for (int i = 0; i < filenames.length; i++) {
            if (filenames[i].getName().endsWith(".ini")) {
                inis.add(filenames[i]);
            }
        }
        // Pick a random ini file to make the cartoon with.
        File file = (File)inis.get((int)(Math.random()*inis.size()));
        // Get the properties from the file.
        Properties p = new Properties();
        p.load(new FileInputStream(file));
        
        // create the background image for the cartoon strip.
        String backgroundFilename = p.getProperty("background");
        BufferedImage image = ImageIO.read(new File("./data/" + backgroundFilename));
        
        // write on a datestamp.
        String datestampPos = p.getProperty("datestamp");
        String[] datestampSplit = datestampPos.split("[\\s,]+");
        int x = Integer.parseInt(datestampSplit[0]);
        int y = Integer.parseInt(datestampSplit[1]);
        Date date = new Date();
        Graphics2D g2d = (Graphics2D)image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.black);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g2d.drawString(date.toString() + " (" + channel + ")", x, y);
        
        LinkedList positions = new LinkedList();
        
        // Parse up to texts.length things to stick into the cartoon.
        for (int i = 1; i <= texts.length; i++) {
            String bubblePos = p.getProperty("bubble" + i);
            String nickPos = p.getProperty("nick" + i);
            if (bubblePos == null || nickPos == null) {
                break;
            }
            // Work out the positions of both things.
            String[] bubbleSplit = bubblePos.split("[\\s,]+");
            String[] nickSplit = nickPos.split("[\\s,]+");
            int[] bubbleLoc = new int[4];
            int[] nickLoc = new int[4];
            for (int j = 0; j < 4; j++) {
                bubbleLoc[j] = Integer.parseInt(bubbleSplit[j]);
                nickLoc[j] = Integer.parseInt(nickSplit[j]);
            }
            
            positions.add(bubbleLoc);
            positions.add(nickLoc);
            
            System.out.println("adding caption number " + i);
        }
        
        int numBubbles = positions.size() / 2;
        for (int i = 0; i < numBubbles; i++) {
            int maxLength = Integer.parseInt(p.getProperty("maxlength" + (i + 1), "20"));
            int[] b = (int[])positions.removeFirst();
            int[] n = (int[])positions.removeFirst();
            // Add bubble text
            String text = texts[texts.length - numBubbles + i];
            if (text.length() > maxLength) {
                System.out.println(text.length() + " > " + maxLength);
                return false;
            }
            addText(image, text, b[0], b[1], b[2], b[3]);
            System.out.println("adding caption number " + i);
        }
        
        
        //addText(image, "Hello", 100, 100, 100, 100);
        //addText(image, "Oooh, here's a much longer sentence.", 300, 100, 100, 100);
        //addText(image, "Hmm, so you reckon that was a long sentence do ya? I think it's about time you saw how long these sentences really can be, dude!", 500, 100, 100, 100);
        
        ImageIO.write(image, "png", new File(outputDirectory, "cartoon.png"));
        
        // Write an archive image, too.
        ImageIO.write(image, "png", new File(outputDirectory, "cartoon-" + (date.getTime()/1000) + ".png"));
        
        return true;
    }
    
    //public static void main(String[] args) throws IOException {
    //    createCartoonStrip(new String[]{"lo bob", "you have pie?", "How rare!"},
    //                       new String[]{"weebl", "weebl", "bob"});
    //}
    
}
package graphics;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * Created by an6g15 on 11/02/2017.
 */
public class FontLoader{

    static {
        fonts = new Hashtable<>();
    }

    private static Hashtable<String, TrueTypeFont> fonts;

    private FontLoader(){
        /**
         * We shouldn't create new instances of this class - ever...
         * Evvvvvvvvvvvvveeeeeeeeeeerrrrrrrrrrrrr...............
         */
    }
    public static void loadFont(String fontToLoad){
        try{
            InputStream stream = ResourceLoader.getResourceAsStream("res/fonts/" + fontToLoad);
            Font fontToAdd = Font.createFont(Font.TRUETYPE_FONT, stream);
            fontToAdd = fontToAdd.deriveFont(48f);
            fonts.put(fontToLoad, new TrueTypeFont(fontToAdd, false));
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        catch (FontFormatException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

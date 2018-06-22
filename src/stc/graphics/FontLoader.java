package stc.graphics;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by an6g15 on 11/02/2017.
 */
public class FontLoader {

    private static float fontSize;

    public enum Fonts{

        PixelGame("PixelGameFont");

        Fonts(String fontName){
            this.fontName = fontName;
        }

        public String toString(){
            return this.fontName;
        }

        private String fontName;
    }

    static {
        fonts = new Hashtable<>();
        fontsArrayList = new ArrayList<>();
    }

    private static Hashtable<String, TrueTypeFont> fonts;
    private static ArrayList<Fonts> fontsArrayList;

    private FontLoader() {
        /**
         * We shouldn't create new instances of this class - ever...
         * Evvvvvvvvvvvvveeeeeeeeeeerrrrrrrrrrrrr...............
         */
    }
    public static float getFontSize(){
        return fontSize;
    }

    public static void loadFont(GameContainer gc) {

            fontSize = 48f;

        try {

            Font fontToAdd;

            for(Fonts f : Fonts.values()){

                InputStream stream = ResourceLoader.getResourceAsStream("res/fonts/" + f.toString() + ".ttf");
                fontToAdd = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(fontSize);
                fonts.put(f.toString(), new TrueTypeFont(fontToAdd, false));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (FontFormatException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    public static TrueTypeFont getFont(String fontToGet) {
        return fonts.get(fontToGet);
    }
}

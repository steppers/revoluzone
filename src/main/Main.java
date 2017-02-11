package main;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 * A game made for the Soton GameJam by:
 * Anton, Ollie and Alistair
 */

public class Main {

    private static final String GAME_NAME = "Revolosezone";

    public static void main(String[] args) {
        AppGameContainer gc;
        try{
            gc = new AppGameContainer(new StateManager(GAME_NAME));
            gc.setDisplayMode(1600, 900, false);
            gc.setVSync(true);
            gc.setSmoothDeltas(true);
            gc.setMultiSample(4);
            Display.setResizable(true);
            gc.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}

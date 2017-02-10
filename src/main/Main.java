package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 * A game made for the Soton GameJam by:
 * Anton, Ollie and Alistair
 */

public class Main{

    private static final String GAME_NAME = "Revolosezone";

    public static void main(String[] args) {
        AppGameContainer gc;
        try{
            gc = new AppGameContainer(new StateManager(GAME_NAME));
            gc.setDisplayMode(gc.getScreenWidth(), gc.getScreenHeight(), true);
            gc.start();
        }catch (SlickException e){
            System.out.println(e.getMessage());
        }
    }
}

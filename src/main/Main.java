package main;

import filemanage.Parser;

/**
 * A game made for the Soton GameJam by:
 * Anton, Ollie and Alistair
 */

public class Main {

    private static final String GAME_NAME = "Revolosezone";

    public static void main(String[] args) {

        Parser parser = new Parser();
        parser.loadFile("res/config/config.txt");
        parser.getData();


        /**AppGameContainer gc;
        try{
            gc = new AppGameContainer(new StateManager(GAME_NAME));
            gc.setDisplayMode(2400, 1350, false);
            gc.setVSync(true);
            gc.setSmoothDeltas(true);
            gc.setMultiSample(4);
            gc.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }*/
    }
}

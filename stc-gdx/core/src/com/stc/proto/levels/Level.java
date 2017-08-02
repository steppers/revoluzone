package com.stc.proto.levels;

import java.io.File;

/**
 * Created by steppers on 8/2/17.
 */

public class Level {

    private boolean loaded;

    /*
     * Construct a level object from the file provided.
     * This will not lead to a usable object yet. Use load()
     * before the level is required.
     */
    public Level(File file) {
        this.loaded = false;
    }

    /*
     * Scans the file for errors but doesn't load the data in.
     */
    public boolean verify() {
        return true;
    }

    /*
     * Fully loads the file so it's ready to be presented and used
     */
    public boolean load() {
        if(loaded) // Early return
            return true;

        //Fully load the level here

        return loaded;
    }

}

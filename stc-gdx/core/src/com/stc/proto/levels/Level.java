package com.stc.proto.levels;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import jdk.nashorn.internal.parser.JSONParser;

/**
 * Created by steppers on 8/2/17.
 */

public class Level {

    private boolean loaded;
    private String data = "";

    // Level info
    private String fileName;
    private String levelName;
    private String prevLevelFileName;
    private String nextLevelFileName;
    private int size;

    /*
     * Construct a level object from the file provided.
     * This will not lead to a usable object yet. Use load()
     * before the level is required.
     */
    public Level(File file) {
        this.loaded = false;
        this.fileName = file.getName();

        try {
            BufferedReader r = new BufferedReader(new FileReader(file));

            String line;
            while((line = r.readLine()) != null) {
                data += line.trim();
            }

            r.close();
        } catch (java.io.IOException e) {
            System.err.println("[X] Error: Problem reading level file '" + file.getName() + "'. Something went wrong!");
            e.printStackTrace();
            System.exit(1);
        }

        parseData();
    }

    private void parseData() {
        JSONObject obj = new JSONObject(data);
        JSONObject info = obj.getJSONObject("info");
        this.levelName = info.getString("levelName");
        this.nextLevelFileName = info.getString("nextLevelFileName");
        this.prevLevelFileName = info.getString("prevLevelFileName");
        this.size = info.getInt("size");
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

    public String getNextFileName() {
        return nextLevelFileName;
    }

    public String getPrevFileName() {
        return prevLevelFileName;
    }

}

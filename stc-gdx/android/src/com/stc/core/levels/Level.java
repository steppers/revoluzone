package com.stc.core.levels;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import com.badlogic.gdx.files.*;
import org.json.*;

/**
 * Created by steppers on 8/2/17.
 */

public class Level {

    private boolean loaded;
    private String raw = "";

    // Level info
    private String fileName;
    private String levelName;
    private String prevLevelFileName;
    private String nextLevelFileName;
	private int[] levelData;
    private int size;

    /*
     * Construct a level object from the file provided.
     * This will not lead to a usable object yet. Use load()
     * before the level is required.
     */
    public Level(FileHandle file) {
        this.loaded = false;
        this.fileName = file.name();

        raw = file.readString();

        parseData();
    }

	/*
	 * Loads member variables from the raw JSON
	 */
    private void parseData() {
        JSONObject obj = new JSONObject(raw);
		
        JSONObject info = obj.getJSONObject("info");
        this.levelName = info.getString("levelName");
        this.nextLevelFileName = info.getString("nextLevelFileName");
        this.prevLevelFileName = info.getString("prevLevelFileName");
        this.size = info.getInt("size");
		
		levelData = new int[this.size*this.size];
		JSONArray data = obj.getJSONArray("data");
		for(int i = 0; i < data.length(); i++) {
			levelData[i] = data.get(i);
		}
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
	
	public void unload() {
		
	}

    public String getNextFileName() {
        return nextLevelFileName;
    }

    public String getPrevFileName() {
        return prevLevelFileName;
    }

	public String getLevelName() {
		return levelName;
	}
	
}

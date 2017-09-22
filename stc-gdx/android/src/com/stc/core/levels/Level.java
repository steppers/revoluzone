package com.stc.core.levels;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import com.badlogic.gdx.files.*;
import org.json.*;
import com.stc.core.levels.moveables.*;

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
     * Scans the data for errors
     */
    public boolean verify() {
        return true;
    }
	
	public LevelInstance getInstance() {
		LevelInstance instance = new LevelInstance();
		
		Tile[] tiles = new Tile[size*size];
		int x, y;
		for(int i = 0; i < size*size; i++) {
			int id = levelData[i];
			x = i % size;
			y = i / size;
			
			tiles[i] = new Tile(x, y, TileType.EMPTY); // Default
			switch(id) {
				case 1:
					tiles[i] = new Tile(x, y, TileType.WALL);
					break;
				case 2:
					instance.addMoveable(new Ball(x, y));
					break;
				default: break;
			}
		}
		
		instance.setTiles(tiles, size);
		return instance;
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

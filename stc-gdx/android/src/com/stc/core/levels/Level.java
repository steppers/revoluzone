package com.stc.core.levels;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import com.badlogic.gdx.files.*;
import org.json.*;
import com.stc.core.levels.moveables.*;
import com.stc.core.levels.statics.*;
import java.util.*;

/**
 * Created by steppers on 8/2/17.
 */

public class Level {
	
	private class Link {
		public int sx, sy, tx, ty;
	}

    private boolean loaded;
    private String raw = "";

    // Level info
    private String fileName;
    private String levelName;
    private String prevLevelName;
    private String nextLevelName;
	private int[] levelData;
	private ArrayList<Link> links;
    private int size;

    /*
     * Construct a level object from the file provided.
     * This will not lead to a usable object yet. Use load()
     * before the level is required.
     */
    public Level(FileHandle file) {
        this.loaded = false;
        this.fileName = file.name();
		links = new ArrayList<Link>();

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
        this.nextLevelName = info.getString("nextLevelName");
        this.prevLevelName = info.getString("prevLevelName");
        this.size = info.getInt("size");
		
		levelData = new int[this.size*this.size];
		JSONArray data = obj.getJSONArray("data");
		for(int i = 0; i < data.length(); i++) {
			levelData[i] = data.get(i);
		}
		
		if(obj.has("links")) {
		JSONArray linkData = obj.getJSONArray("links");
			for(int i = 0; i < linkData.length(); i++) {
				JSONObject link = linkData.getJSONObject(i);
				Link l = new Link();
				l.sx = link.getInt("sx");
				l.sy = link.getInt("sy");
				l.tx = link.getInt("tx");
				l.ty = link.getInt("ty");
				links.add(l);
			}
		}
    }

    /*
     * Scans the data for errors
     */
    public boolean verify() {
        return true;
    }
	
	public LevelInstance getInstance() {
		LevelInstance instance = new LevelInstance(levelName, nextLevelName, prevLevelName);
		
		Tile[] tiles = new Tile[size*size];
		int x, y, index;
		for(int i = 0; i < size*size; i++) {
			int id = levelData[i];
			x = i % size;
			y = size - 1 - (i / size);
			index = y*size + x;
			
			tiles[index] = new Tile(x, y, TileType.EMPTY); // Default
			switch(id) {
				case 1:
					tiles[index] = new Tile(x, y, TileType.WALL);
					break;
				case 2:
					instance.addMoveable(new Ball(x, y));
					instance.addStatic(new StartPad(x, y));
					break;
				case 3:
					instance.addStatic(new FinishHole(x, y));
					break;
				case 4:
					tiles[index] = new Tile(x, y, TileType.RED);
					break;
				case 5:
					tiles[index] = new Tile(x, y, TileType.BLUE);
					break;
				case 6:
					instance.addStatic(new Switch(x, y));
					break;
				case 7:
					instance.addStatic(new LockedFinishHole(x, y));
					break;
				default: break;
			}
		}
		
		for(Link l : links) {
			instance.getStaticAt(l.sx, l.sy).addLink(instance.getStaticAt(l.tx, l.ty));
		}
		
		instance.setTiles(tiles, size);
		instance.triggerUpdate(0);
		return instance;
	}
	
	public void setNextLevelName(String name) {
		nextLevelName = name;
	}
	
	public void setPrevLevelName(String name) {
		prevLevelName = name;
	}

    public String getNextLevelName() {
        return nextLevelName;
    }

    public String getPrevLevelName() {
        return prevLevelName;
    }

	public String getLevelName() {
		return levelName;
	}
	
}

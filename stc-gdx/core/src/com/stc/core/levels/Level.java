package com.stc.core.levels;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.stc.core.levels.moveables.Ball;
import com.stc.core.levels.moveables.Slider;
import com.stc.core.levels.statics.FinishHole;
import com.stc.core.levels.statics.KillZone;
import com.stc.core.levels.statics.LockedFinishHole;
import com.stc.core.levels.statics.Rail;
import com.stc.core.levels.statics.StartPad;
import com.stc.core.levels.statics.Switch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
	private Rail[][] rails;
	private ArrayList<Vector2> sliders;
    private int size;
	
	// Status
	private boolean valid = false;

    /*
     * Construct a level object from the file provided.
     * This will not lead to a usable object yet. Use load()
     * before the level is required.
     */
    public Level(FileHandle file, boolean oldFormat) {
        this.loaded = false;
        this.fileName = file.name();
		links = new ArrayList<Link>();
		sliders = new ArrayList<>();

        raw = file.readString();

		if(oldFormat)
			parseOld();
		else
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
			levelData[i] = (Integer)(data.get(i));
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
		
		if(obj.has("rails")) {
			rails = new Rail[this.size][this.size];
			JSONArray railData = obj.getJSONArray("rails");
			Link l = new Link();
			for(int i = 0; i < railData.length(); i++) {
				JSONObject rail = railData.getJSONObject(i);
				l.sx = rail.getInt("sx");
				l.sy = rail.getInt("sy");
				l.tx = rail.getInt("tx");
				l.ty = rail.getInt("ty");
				int dx = l.tx - l.sx;
				int dy = l.ty - l.sy;
				if(dx != 0) {
					dx = dx / Math.abs(dx);
					for(int x = l.sx; x != l.tx+dx; x += dx) {
						if(rails[x][l.sy] == null)
							rails[x][l.sy] = new Rail(x, l.sy);
					}
				} else if(dy != 0) {
					dy = dy / Math.abs(dy);
					for(int y = l.sy; y != l.ty+dy; y += dy) {
						if(rails[l.sx][y] == null)
							rails[l.sx][y] = new Rail(l.sx, y);
					}
				}
			}
		}
		
		valid = true;
    }
	
	private void parseOld() {
		String[] lines = raw.split("\n");
		ArrayList<String> tmp = new ArrayList<>();
		
		int i = 0;
		while(lines[i].startsWith("{")) {
			lines[i] = lines[i].substring(1, lines[i].length()-1);
			String[] ids = lines[i].split(",");
			this.size = ids.length + 2;
			for(String s : ids)
				tmp.add(s);
				
			i++;
		}
		
		// Create and clear to walls
		levelData = new int[this.size*this.size];
		for(int j = 0; j < levelData.length; j++) {
			levelData[j] = 1;
		}
		
		// Load the data to the correct place
		int x, y, idc;
		for(int index = 0; index < tmp.size(); index++) {
			x = (index % (this.size-2))+1;
			y = (index / (this.size-2))+1;
			idc = (y*this.size) + x; // adjusted index;
			levelData[idc] = Integer.parseInt(tmp.get(index));
		}
		
		rails = new Rail[this.size][this.size];
		while(i < lines.length) {
			String line = lines[i];
			if(line.startsWith("name")) {
				this.levelName = line.split("=")[1].trim();
			}
			else if(line.startsWith("next")) {
				this.nextLevelName = line.split("=")[1].split(".txt")[0].trim();
			}
			else if(line.startsWith("prev")) {
				this.prevLevelName = line.split("=")[1].split(".txt")[0].trim();
			}
			else if(line.startsWith("rail")) {
				Link l = new Link();
				l.sx = Integer.parseInt(line.split("=")[1].split("->")[0].split(",")[0]);
				l.sy = this.size - 1 - Integer.parseInt(line.split("=")[1].split("->")[0].split(",")[1]);
				l.tx = Integer.parseInt(line.split("=")[1].split("->")[1].split(",")[0]);
				l.ty = this.size - 1 - Integer.parseInt(line.split("=")[1].split("->")[1].split(",")[1]);
				int dx = l.tx - l.sx;
				int dy = l.ty - l.sy;
				if(dx != 0) {
					dx = dx / Math.abs(dx);
					for(x = l.sx; x != l.tx+dx; x += dx) {
						if(rails[x][l.sy] == null)
							rails[x][l.sy] = new Rail(x, l.sy);
					}
				} else if(dy != 0) {
					dy = dy / Math.abs(dy);
					for(y = l.sy; y != l.ty+dy; y += dy) {
						if(rails[l.sx][y] == null)
							rails[l.sx][y] = new Rail(l.sx, y);
					}
				} else {
					rails[l.sx][l.sy] = new Rail(l.sx, l.sy);
				}
			}
			else if(line.startsWith("slider")) {
				x = Integer.parseInt(line.split("=")[1].split(",")[0]);
				y = this.size - 1 - Integer.parseInt(line.split("=")[1].split(",")[1]);
				sliders.add(new Vector2(x, y));
			}
			else if(line.startsWith("link")) {
				Link l = new Link();
				l.sx = Integer.parseInt(line.split("=")[1].split("->")[0].split(",")[0]);
				l.sy = this.size - 1 - Integer.parseInt(line.split("=")[1].split("->")[0].split(",")[1]);
				l.tx = Integer.parseInt(line.split("=")[1].split("->")[1].split(",")[0]);
				l.ty = this.size - 1 - Integer.parseInt(line.split("=")[1].split("->")[1].split(",")[1]);
				links.add(l);
			}
			i++;
		}
		valid = true;
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
			
			tiles[index] = new Tile(x, y, LO_TYPE.EMPTY); // Default
			switch(id) {
				case 1:
					tiles[index] = new Tile(x, y, LO_TYPE.WALL);
					break;
				case 5:
					instance.addMoveable(new Ball(x, y));
					instance.addStatic(new StartPad(x, y));
					break;
				case 6:
					instance.addStatic(new FinishHole(x, y));
					break;
				case 2:
					tiles[index] = new Tile(x, y, LO_TYPE.RED);
					break;
				case 3:
					tiles[index] = new Tile(x, y, LO_TYPE.BLUE);
					break;
				case 7:
					instance.addStatic(new Switch(x, y));
					break;
				case 9:
					instance.addStatic(new LockedFinishHole(x, y));
					break;
				case 4:
					instance.addStatic(new KillZone(x, y));
					break;
				case 10:
					instance.addMoveable(new Slider(x, y));
					break;
				default: break;
			}
		}
		
		for(Link l : links) {
			for(LevelObject lo : instance.getStaticsAt(l.sx, l.sy))
				for(LevelObject lo2 : instance.getStaticsAt(l.tx, l.ty))
					lo.addLink(lo2);
		}
		
		if(rails != null) {
			for(x = 0; x < this.size; x++) {
				for(y = 0; y < this.size; y++) {
					if(rails[x][y] != null) {
						Rail r = rails[x][y];
					
						if(x > 0)
							if(rails[x-1][y] != null)
								r.addLink(rails[x-1][y]);
							
						if(x < this.size-1)
							if(rails[x+1][y] != null)
								r.addLink(rails[x+1][y]);
							
						if(y > 0)
							if(rails[x][y-1] != null)
								r.addLink(rails[x][y-1]);
							
						if(y < this.size-1)
							if(rails[x][y+1] != null)
								r.addLink(rails[x][y+1]);
							
						instance.addStatic(r);
					}
				}
			}
		}
		
		for(Vector2 s : sliders) {
			instance.addMoveable(new Slider((int)s.x, (int)s.y));
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
	
	public boolean isValid() {
		return valid;
	}
	
}

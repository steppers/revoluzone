package com.stc.core.levels;
import com.stc.core.*;

public class LevelInstance
{
	
	private Tile[] tiles;
	private int size;
	
	public LevelInstance() {
		
	}
	
	/*
	 * Evaluates true if this instance is currently usable
	 */
	public boolean isValid() {
		return true;
	}
	
	public void setTiles(Tile[] tiles, int size) {
		this.tiles = tiles;
		this.size = size;
	}
	
	public void render(World world) {
		world.render(tiles, size);
	}
	
}

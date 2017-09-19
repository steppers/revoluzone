package com.stc.core.levels;
import com.stc.core.*;
import java.util.*;

public class LevelInstance
{
	
	private Tile[] tiles;
	private int size;
	
	private boolean updating;
	
	// Ball
	private ArrayList<Moveable> moveables;
	
	public LevelInstance() {
		updating = false;
		moveables = new ArrayList<Moveable>();
		moveables.add(new Ball(3,3));
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
		world.renderMovables(moveables, size);
	}
	
	public void triggerUpdate() {
		updating = true;
	}
	
	public void update(World world, float delta) {
		if(updating) {
			
			updating = false;
		}
	}
	
	public boolean isUpdating() {
		return updating;
	}
	
}

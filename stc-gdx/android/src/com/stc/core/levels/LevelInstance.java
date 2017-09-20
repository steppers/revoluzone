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
		moveables.add(new Ball(1,3));
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
		world.render(tiles, moveables, size);
	}
	
	public void triggerUpdate(int rotation) {
		updating = true;
		
		// Movement direction
		int dx = 0, dy = 0;
		if(rotation == 0)
			dy = -1;
		if(rotation == 90)
			dx = -1;
		if(rotation == 180)
			dy = 1;
		if(rotation == 270)
			dx = 1;
			
		// Calculate movement targets
		boolean moved = true;
		while(moved) {
			moved = false;
			for(Moveable m : moveables) {
				int mx = m.tx;
				int my = m.ty;
				mx += dx;
				my += dy;
				if(mx >= size || mx < 0 || my >= size || my < 0)
					continue;
				if(!tiles[my * size + mx].isSolid()) {
					if(isMoveableSlotAvailable(mx, my) && m.canMoveTo(mx, my, this)) {
						moved = true;
						m.moveTo(mx, my);
					}
				}
			}
		}
	}
	
	public void update(World world, float delta) {
		if(updating) {
			updating = false;
			for(Moveable m : moveables) {
				m.update(delta);
				if(m.isMoving())
					updating = true;
			}
		}
	}
	
	public boolean isUpdating() {
		return updating;
	}
	
	private boolean isMoveableSlotAvailable(int x, int y) {
		for (Moveable m : moveables) {
			if(m.isMovingTo(x, y))
				return false;
		}
		return true;
	}
	
}

package com.stc.core.levels;
import com.stc.core.*;
import java.util.*;
import com.stc.core.levels.moveables.*;

public class LevelInstance
{
	private int size;
	
	private boolean updating;
	
	// Objects
	private ArrayList<LevelObject> objects;
	private ArrayList<Moveable> moveables;
	private ArrayList<LevelObject> statics;
	private ArrayList<Tile> tiles;
	
	public LevelInstance() {
		updating = false;
		
		objects = new ArrayList<LevelObject>();
		moveables = new ArrayList<Moveable>();
		statics = new ArrayList<LevelObject>();
		tiles = new ArrayList<Tile>();
		
		addMoveable(new Ball(2,3));
		addMoveable(new Ball(1,3));
	}
	
	public void addMoveable(Moveable m) {
		moveables.add(m);
		objects.add(m);
	}
	
	public void removeMoveable(Moveable m) {
		moveables.remove(m);
		objects.remove(m);
	}
	
	public void addStatic(LevelObject s) {
		statics.add(s);
		objects.add(s);
	}

	public void removeStatic(LevelObject s) {
		statics.remove(s);
		objects.remove(s);
	}
	
	private void addTile(Tile t) {
		tiles.add(t);
		objects.add(t);
	}
	
	private void removeTile(Tile t) {
		tiles.remove(t);
		objects.remove(t);
	}
	
	/*
	 * Evaluates true if this instance is currently usable
	 */
	public boolean isValid() {
		return true;
	}
	
	public void setTiles(Tile[] tiles, int size) {
		Iterator<Tile> it = this.tiles.iterator();
		while(it.hasNext()) {
			it.next();
			it.remove();
		}
		for(Tile t : tiles) {
			addTile(t);
		}
		this.size = size;
	}
	
	public void render(World world) {
		world.render(this);
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
				if(!tiles.get(my * size + mx).isSolid()) {
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
	
	public int getSize() {
		return size;
	}
	
	public ArrayList<LevelObject> getLevelObjects() {
		return objects;
	}
	
}

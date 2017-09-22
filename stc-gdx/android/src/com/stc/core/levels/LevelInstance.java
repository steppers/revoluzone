package com.stc.core.levels;
import com.stc.core.*;
import java.util.*;
import com.stc.core.levels.moveables.*;

public class LevelInstance
{
	private int size;
	
	private boolean moveablesUpdating = false;
	
	// Objects
	private ArrayList<LevelObject> objects;
	private ArrayList<Moveable> moveables;
	private ArrayList<LevelObject> statics;
	private ArrayList<Tile> tiles;
	
	public LevelInstance() {
		objects = new ArrayList<LevelObject>();
		moveables = new ArrayList<Moveable>();
		statics = new ArrayList<LevelObject>();
		tiles = new ArrayList<Tile>();
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
	
	public void setTiles(Tile[] tiles, int size) {
		// Remove old
		Iterator<Tile> it = this.tiles.iterator();
		while(it.hasNext()) {
			Tile t = it.next();
			it.remove();
			objects.remove(t);
		}
		// Add new
		for(Tile t : tiles) {
			addTile(t);
		}
		this.size = size;
	}
	
	public void render(World world) {
		world.render(this);
	}
	
	public void triggerUpdate(int rotation) {
		moveablesUpdating = true;
		
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
					if(!isSpaceSolid(mx, my) && m.canMoveTo(mx, my, this)) {
						moved = true;
						m.moveTo(mx, my);
					}
				}
			}
		}
	}
	
	public void update(World world, float delta) {
		// Update all
		for(LevelObject o : objects) {
			o.update(delta);
		}
		
		// Refresh updating flag
		if(moveablesUpdating) {
			moveablesUpdating = false;
			for(Moveable m : moveables) {
				if(m.isMoving())
					moveablesUpdating = true;
			}
		}
		
	}
	
	public boolean isUpdating() {
		return moveablesUpdating;
	}
	
	private boolean isSpaceSolid(int x, int y) {
		for (Moveable m : moveables) {
			if(m.isMovingTo(x, y) && m.isSolid())
				return true;
		}
		
		for (LevelObject o : statics) {
			if(o.isOver(x, y) && o.isSolid())
				return true;
		}
		
		for (Tile t : tiles) {
			if(t.isOver(x, y) && t.isSolid())
				return true;
		}
		
		return false;
	}
	
	public int getSize() {
		return size;
	}
	
	public ArrayList<LevelObject> getLevelObjects() {
		return objects;
	}
	
}

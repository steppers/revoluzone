package com.stc.core.levels;
import com.stc.core.*;
import java.util.*;
import com.stc.core.levels.moveables.*;

public class LevelInstance
{
	private int size;
	private String next, prev, name;
	
	private boolean moveablesUpdating = false;
	
	// Objects
	private ArrayList<LevelObject> objects;
	private ArrayList<Moveable> moveables;
	private ArrayList<LevelObject> statics;
	private ArrayList<Tile> tiles;
	
	public LevelInstance(String name, String next, String prev) {
		objects = new ArrayList<LevelObject>();
		moveables = new ArrayList<Moveable>();
		statics = new ArrayList<LevelObject>();
		tiles = new ArrayList<Tile>();
		
		this.name = name;
		this.next = next;
		this.prev = prev;
	}
	
	public void addMoveable(Moveable m) {
		moveables.add(m);
		objects.add(m);
		m.setLevel(this);
	}
	
	public void removeMoveable(Moveable m) {
		moveables.remove(m);
		objects.remove(m);
	}
	
	public void addStatic(LevelObject s) {
		statics.add(s);
		objects.add(s);
		s.setLevel(this);
	}

	public void removeStatic(LevelObject s) {
		statics.remove(s);
		objects.remove(s);
	}
	
	private void addTile(Tile t) {
		tiles.add(t);
		objects.add(t);
		t.setLevel(this);
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
		
		resetRedBlue();
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
			updateActiveStates();
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
	
	private void updateActiveStates() {
		for(LevelObject o : objects) {
			if(o.isActivator()) {
				for(LevelObject u : objects) {
					if(u == o)
						continue;
					else if(o.isOver(u.x, u.y)) {
						u.setActive(true);
					}
				}
			}
		}
	}
	
	public LevelObject getStaticAt(int x, int y) {
		for(LevelObject s : statics) {
			if(s.isOver(x, y))
				return s;
		}
		return null;
	}
	
	public Moveable getMoveableAt(int x, int y) {
		for(Moveable m : moveables) {
			if(m.isOver(x, y))
				return m;
		}
		return null;
	}
	
	public Tile getTileAt(int x, int y) {
		for(Tile t : tiles) {
			if(t.isOver(x, y))
				return t;
		}
		return null;
	}
	
	private void resetRedBlue() {
		TileType type;
		for(Tile t : tiles) {
			type = t.getType();
			if(type == TileType.BLUE || type == TileType.RED) {
				t.setActive(type == TileType.BLUE ? true : false);
			}
		}
	}
	
	public void toggleRedBlue() {
		TileType active = TileType.EMPTY;
		TileType type;
		for(Tile t : tiles) {
			type = t.getType();
			if(type == TileType.RED && active == TileType.EMPTY) {
				active = t.isActive() ? TileType.BLUE : TileType.RED;
			}
			if(type == TileType.BLUE && active == TileType.EMPTY) {
				active = t.isActive() ? TileType.RED : TileType.BLUE;
			}
			if(type == TileType.BLUE || type == TileType.RED) {
				t.setActive(type == active ? true : false);
			}
		}
	}
	
	public ArrayList<LevelObject> getLevelObjects() {
		return objects;
	}
	
	public LevelInstance getNextLevel() {
		return LevelManager.instance().getLevelInstance(next);
	}
	
	public LevelInstance getPrevLevel() {
		return LevelManager.instance().getLevelInstance(prev);
	}
	
	public String getLevelName() {
		return name;
	}
	
	public String getNextLevelName() {
		return next;
	}
	
	public String getPrevLevelName() {
		return prev;
	}
}

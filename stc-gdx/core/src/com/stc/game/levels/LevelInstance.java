package com.stc.game.levels;

import com.stc.game.World;

import java.util.ArrayList;
import java.util.Iterator;

public class LevelInstance
{
	private int size;
	private String next, prev, name;
	private int turnCount;
	private int lastRotation = 0;
	
	private boolean moveablesUpdating = false;
	private boolean complete = false;
	
	// Objects
	private ArrayList<LevelObject> objects;
	private ArrayList<LevelObject> moveables;
	private ArrayList<LevelObject> statics;
	private ArrayList<Tile> tiles;
	
	public LevelInstance(String name, String next, String prev) {
		objects = new ArrayList<LevelObject>();
		moveables = new ArrayList<LevelObject>();
		statics = new ArrayList<LevelObject>();
		tiles = new ArrayList<Tile>();
		
		this.name = name;
		this.next = next;
		this.prev = prev;
		turnCount = -1;
	}
	
	public void addMoveable(LevelObject m) {
		moveables.add(m);
		objects.add(m);
		m.setLevel(this);
	}
	
	public void removeMoveable(LevelObject m) {
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
	
	public void refreshUpdate() {
		triggerUpdate(lastRotation);
		turnCount--;
	}
	
	public void triggerUpdate(int rotation) {
		moveablesUpdating = true;
		turnCount++;
		lastRotation = rotation;
		
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
			for(LevelObject m : moveables) {
				int mx = m.getTx();
				int my = m.getTy();
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
			for(LevelObject m : moveables) {
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
		for (LevelObject m : moveables) {
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
		ArrayList<LevelObject> l = new ArrayList<LevelObject>();
		l.addAll(objects);
		for(LevelObject o : l) {
			if(o.isActivator()) {
				for(LevelObject u : l) {
					if(u == o)
						continue;
					else if(o.isOver(u.x, u.y)) {
						u.setActive(true, o);
					}
				}
			}
		}
	}
	
	public LevelObject getStaticOfTypeAt(int x, int y, LO_TYPE type) {
		for(LevelObject s : statics) {
			if(s.isOver(x, y) && s.getType() == type)
				return s;
		}
		return null;
	}
	
	public ArrayList<LevelObject> getStaticsAt(int x, int y) {
		ArrayList<LevelObject> objs = new ArrayList<>();
		for(LevelObject s : statics) {
			if(s.isOver(x, y))
				objs.add(s);
		}
		return objs;
	}
	
	public LevelObject getMoveableOfTypeAt(int x, int y, LO_TYPE type) {
		for(LevelObject m : moveables) {
			if(m.isOver(x, y) && m.getType() == type)
				return m;
		}
		return null;
	}
	
	public ArrayList<LevelObject> getMoveablesAt(int x, int y) {
		ArrayList<LevelObject> objs = new ArrayList<>();
		for(LevelObject m : moveables) {
			if(m.isOver(x, y))
				objs.add(m);
		}
		return objs;
	}
	
	public Tile getTileAt(int x, int y) {
		for(Tile t : tiles) {
			if(t.isOver(x, y))
				return t;
		}
		return null;
	}
	
	private void resetRedBlue() {
		LO_TYPE type;
		for(Tile t : tiles) {
			type = t.getType();
			if(type == LO_TYPE.BLUE || type == LO_TYPE.RED) {
				t.setActive(type == LO_TYPE.BLUE ? true : false, null);
			}
		}
	}
	
	public void toggleRedBlue() {
		LO_TYPE active = LO_TYPE.EMPTY;
		LO_TYPE type;
		
		ArrayList<Tile> activate = new ArrayList<Tile>();
		ArrayList<Tile> deactivate = new ArrayList<Tile>();
		for(Tile t : tiles) {
			type = t.getType();
			if(type == LO_TYPE.RED && active == LO_TYPE.EMPTY) {
				active = t.isActive() ? LO_TYPE.BLUE : LO_TYPE.RED;
			}
			if(type == LO_TYPE.BLUE && active == LO_TYPE.EMPTY) {
				active = t.isActive() ? LO_TYPE.RED : LO_TYPE.BLUE;
			}
			if(type == LO_TYPE.BLUE || type == LO_TYPE.RED) {
				for(LevelObject m : moveables) {
					if(m.isOver(t.x, t.y))
						return;
				}
				
				if(type == active)
					activate.add(t);
				else
					deactivate.add(t);
			}
		}
		for(Tile t : activate) {
			t.setActive(true, null);
		}
		for(Tile t : deactivate) {
			t.setActive(false, null);
		}
	}
	
	public void triggerNextLevel() {
		complete = true;
	}
	
	public void reset() {
		next = name;
		complete = true;
	}
	
	public boolean isComplete() {
		return complete;
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
	
	public int getTurnCount() {
		return turnCount;
	}
	
	public void resetTurnCount() {
		turnCount = 0;
	}
	
	public int getBallCount() {
		int c = 0;
		for(LevelObject m : moveables) {
			if(m.getType() == LO_TYPE.BALL)
				c++;
		}
		return c;
	}
}

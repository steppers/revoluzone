package com.stc.game.levels;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.game.Globals;

import java.util.ArrayList;

public abstract class LevelObject
{
	protected LevelInstance level;
	
	protected LO_TYPE type;

	// Position & Movement vars
	public float x, y;
	// Movement
	private float vx, vy;
	private int tx, ty;
	private int dx, dy;

	protected boolean solid = false;
	protected boolean activator = false;
	protected boolean mStatic = false;

	protected boolean active = false;
	
	protected ArrayList<LevelObject> links;
	
	public LevelObject(float x, float y, LO_TYPE type) {
		this.x = x;
		this.y = y;
		level = null;
		links = new ArrayList<LevelObject>();

		this.type = type;
		solid = type.isSolid();
		mStatic = type.isStatic();

		activator = !mStatic;
		vx = 0;
		vy = 0;
		ty = (int)y;
		tx = (int)x;
	}
	
	public void update(float delta)
	{
		if(!mStatic) {
			if (tx != x || ty != y) {
				vx += Globals.G * delta * (float) dx;
				vy += Globals.G * delta * (float) dy;

				if (vx > 1) vx = 1;
				if (vx < -1) vx = -1;
				if (vy > 1) vy = 1;
				if (vy < -1) vy = -1;

				x += vx;
				y += vy;

				if (dx == 1 && x > tx) {
					x = tx;
					vx = 0;
					vy = 0;
				}
				if (dx == -1 && x < tx) {
					x = tx;
					vx = 0;
					vy = 0;
				}
				if (dy == 1 && y > ty) {
					y = ty;
					vx = 0;
					vy = 0;
				}
				if (dy == -1 && y < ty) {
					y = ty;
					vx = 0;
					vy = 0;
				}
			}
		}
	}

	public abstract void renderObject(ShapeRenderer g, float opacity);
	public void renderFloor(ShapeRenderer g, float opacity) {}
	public abstract void renderShadow(ShapeRenderer g, float opacity);
	
	public void setActive(boolean active, LevelObject activator) {
		this.active = active;
		
		if(active)
			onActivate(activator);
		else
			onDeactivate(activator);
	}

	protected abstract void onActivate(LevelObject activator);
	protected abstract void onDeactivate(LevelObject activator);
	
	public boolean isSolid() {
		return solid;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean isOver(float x, float y) {
		return x > this.x - 0.5f
			&& x < this.x + 0.5f
			&& y > this.y - 0.5f
			&& y < this.y + 0.5f;
	}
	
	public boolean isActivator() {
		return activator;
	}

	public boolean isStatic() { return mStatic; }
	
	public void setLevel(LevelInstance level) {
		this.level = level;
	}
	
	public void addLink(LevelObject linkTarget) {
		links.add(linkTarget);
	}
	
	public LO_TYPE getType() {
		return type;
	}

	public void moveTo(int x, int y) {
		if(!mStatic) {
			tx = x;
			ty = y;

			dx = 0;
			dy = 0;

			if (tx < this.x)
				dx = -1;
			if (tx > this.x)
				dx = 1;
			if (ty < this.y)
				dy = -1;
			if (ty > this.y)
				dy = 1;
		}
	}

	public boolean isMoving() {
		return tx != x || ty != y;
	}

	public boolean isMovingTo(int x, int y) {
		return x == tx && y == ty;
	}

	public boolean canMoveTo(int x, int y, LevelInstance level) {
		return !mStatic;
	}

	public int getTx() { return tx; }
	public int getTy() { return ty; }
	
}

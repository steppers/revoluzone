package com.stc.core.levels;
import com.badlogic.gdx.graphics.glutils.*;

public abstract class LevelObject
{
	protected LevelInstance level;
	
	protected float x, y;
	protected boolean solid = false;
	protected boolean activator = false;
	
	private boolean active = false;
	
	public LevelObject(float x, float y) {
		this.x = x;
		this.y = y;
		level = null;
	}
	
	public abstract void update(float delta);
	public abstract void renderObject(ShapeRenderer g, float opacity);
	public void renderFloor(ShapeRenderer g, float opacity) {}
	public abstract void renderShadow(ShapeRenderer g, float opacity);
	
	public void setActive(boolean active) {
		this.active = active;
		if(active)
			onActivate();
		else
			onDeactivate();
	}

	protected abstract void onActivate();
	protected abstract void onDeactivate();
	
	public boolean isSolid() {
		return solid;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean isOver(float x, float y) {
		return this.x == x && this.y == y;
	}
	
	public boolean isActivator() {
		return activator;
	}
	
	public void setLevel(LevelInstance level) {
		this.level = level;
	}
	
}

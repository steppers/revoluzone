package com.stc.core.levels;
import com.badlogic.gdx.graphics.glutils.*;
import java.util.*;

public abstract class LevelObject
{
	protected LevelInstance level;
	
	protected LO_TYPE type;
	
	public float x, y;
	protected boolean solid = false;
	protected boolean activator = false;
	
	protected boolean active = false;
	
	protected ArrayList<LevelObject> links;
	
	public LevelObject(float x, float y, LO_TYPE type) {
		this.x = x;
		this.y = y;
		level = null;
		links = new ArrayList<LevelObject>();
		this.type = type;
		solid = type.isSolid();
	}
	
	public abstract void update(float delta);
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
	
	public void setLevel(LevelInstance level) {
		this.level = level;
	}
	
	public void addLink(LevelObject linkTarget) {
		links.add(linkTarget);
	}
	
	public LO_TYPE getType() {
		return type;
	}
	
}

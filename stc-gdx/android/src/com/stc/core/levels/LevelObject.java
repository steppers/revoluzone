package com.stc.core.levels;
import com.badlogic.gdx.graphics.glutils.*;

public abstract class LevelObject
{
	protected float x, y;
	protected boolean solid = false;
	
	private boolean active = true;
	
	public LevelObject(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void update(float delta);
	public abstract void renderObject(ShapeRenderer g, float opacity);
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
	
}

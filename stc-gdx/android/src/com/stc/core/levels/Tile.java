package com.stc.core.levels;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.*;
import com.stc.core.*;

public class Tile extends LevelObject
{
	private TileType type;
	
	public Tile(int x, int y, TileType type) {
		super(x, y);
		this.type = type;
	}
	
	public TileType getType() {
		return type;
	}
	
	public boolean isSolid() {
		if(type == TileType.RED)
			return isActive();
		if(type == TileType.BLUE)
			return isActive();
		return type.isSolid();
	}
	
	public void update(float delta) {
		
	}
	
	public void renderObject(ShapeRenderer g, float opacity) {
		Color c;
		switch(type) {
			case EMPTY:
				break;
			case WALL:
				c = new Color(Globals.COLOR_WALL);
				c.a *= opacity;
				g.setColor(c);
				g.rect(x, y, 1, 1);
				break;
			case RED:
				c = Globals.getColor(Globals.COLOR_RED_ACTIVE, opacity);
				g.setColor(c);
				if(isActive())
					g.rect(x, y, 1, 1);
				break;
			case BLUE:
				c = Globals.getColor(Globals.COLOR_BLUE_ACTIVE, opacity);
				g.setColor(c);
				if(isActive())
					g.rect(x, y, 1, 1);
				break;
			default:
				break;
		}
	}

	@Override
	public void renderFloor(ShapeRenderer g, float opacity)
	{
		Color c;
		switch(type) {
			case RED:
				c = Globals.getColor(Globals.COLOR_RED_INACTIVE, opacity);
				g.setColor(c);
				if(!isActive())
					g.rect(x, y, 1, 1);
				break;
			case BLUE:
				c = Globals.getColor(Globals.COLOR_BLUE_INACTIVE, opacity);
				g.setColor(c);
				if(!isActive())
					g.rect(x, y, 1, 1);
				break;
			default:
				break;
		}
	}
	
	public void renderShadow(ShapeRenderer g, float opacity) {
		Color c = new Color(Globals.COLOR_SHADOW);
		c.a *= opacity;
		g.setColor(c);
		
		switch(type) {
			case EMPTY:
				break;
			case WALL:
				g.rect(x, y, 1, 1);
				break;
			case RED:
				if(isActive())
					g.rect(x, y, 1, 1);
				break;
			case BLUE:
				if(isActive())
					g.rect(x, y, 1, 1);
				break;
			default:
				break;
		}
	}
	
	protected void onActivate() {
		
	}
	
	protected void onDeactivate() {
		
	}
	
	
}

package com.stc.game.levels.moveables;

import com.stc.game.Globals;
import com.stc.game.levels.LO_TYPE;
import com.stc.game.levels.LevelInstance;
import com.stc.game.levels.LevelObject;

public abstract class Moveable extends LevelObject
{
	private float ax, ay;
	public int tx, ty;
	private int dx, dy;
	
	public Moveable(float x, float y, LO_TYPE type) {
		super(x, y, type);
		activator = true;
		ax = 0;
		ay = 0;
		ty = (int)y;
		tx = (int)x;
	}
	
	public void update(float delta) {
		if(tx != x || ty != y) {
			ax += Globals.G * delta * (float)dx;
			ay += Globals.G * delta * (float)dy;
			
			if(ax > 1) ax = 1;
			if(ax < -1) ax = -1;
			if(ay > 1) ay = 1;
			if(ay < -1) ay = -1;
			
			x += ax;
			y += ay;
			
			if(dx == 1 && x > tx) {
				x = tx;
				ax = 0;
				ay = 0;
			}
			if(dx == -1 && x < tx) {
				x = tx;
				ax = 0;
				ay = 0;
			}
			if(dy == 1 && y > ty) {
				y = ty;
				ax = 0;
				ay = 0;
			}
			if(dy == -1 && y < ty) {
				y = ty;
				ax = 0;
				ay = 0;
			}
		}
	}
	
	public void moveTo(int x, int y) {
		tx = x;
		ty = y;
		
		dx = 0;
		dy = 0;
		
		if(tx < this.x)
			dx = -1;
		if(tx > this.x)
			dx = 1;
		if(ty < this.y)
			dy = -1;
		if(ty > this.y)
			dy = 1;
	}
	
	public boolean isMoving() {
		return tx != x || ty != y;
	}
	
	public boolean isMovingTo(int x, int y) {
		return x == tx && y == ty;
	}
	
	public boolean canMoveTo(int x, int y, LevelInstance level) {
		return true;
	}
	
}

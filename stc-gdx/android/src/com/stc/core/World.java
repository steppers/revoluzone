package com.stc.core;

import com.stc.core.levels.Tile;

public class World
{
	
	private float scale = 1.0f;
	private float rotation = 0.0f;
	private float opacity = 1.0f;
	
	public World(float scale) {
		this.scale = scale;
	}
	
	public World(float scale, float rotation) {
		this(scale);
		this.rotation = rotation;
	}
	
	public World(float scale, float rotation, float opacity) {
		this(scale, rotation);
		this.opacity = opacity;
	}
	
	public void renderTiles(float center_x, float center_y, Tile[] tiles, int size) {
		
	}
	
	/*
	 * Renders moving objects such as sliders and the ball.
	 * TODO: Make Moveable class to pass in here.
	 */
	public void renderMovables(float center_x, float center_y) {
		
	}
	
	public void renderStatic(float x, float y, Tile tile) {
		
	}
	
	public void rotate(float degrees) {
		rotation += degrees;
	}
	
	public void modifyScale(float delta) {
		scale += delta;
	}
	
	public void modifyOpacity(float delta) {
		opacity += delta;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}
	
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public float getOpacity() {
		return opacity;
	}
	
}

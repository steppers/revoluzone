package com.stc.core;

import com.stc.core.levels.Tile;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.*;

public class World
{
	
	private float scale = 1.0f;
	private float rotation = 0.0f;
	private float opacity = 1.0f;
	
	private ShapeRenderer renderer;
	
	public World() {
		renderer = Renderer.shapeRenderer();
	}
	
	public World(float scale) {
		this.scale = scale;
		renderer = Renderer.shapeRenderer();
	}
	
	public World(float scale, float rotation) {
		this(scale);
		this.rotation = rotation;
	}
	
	public World(float scale, float rotation, float opacity) {
		this(scale, rotation);
		this.opacity = opacity;
	}
	
	public void renderTiles(Tile[] tiles, int size) {
		
	}
	
	/*
	 * Renders moving objects such as sliders and the ball.
	 * TODO: Make Moveable class to pass in here.
	 */
	public void renderMovables() {
		
	}
	
	public void renderStatic(float x, float y, Tile tile) {
		
	}
	
	public void renderFloor(int size) {
		renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.identity();
		
		Color floorColor = new Color(Globals.COLOR_FLOOR);
		floorColor.a = opacity;
		renderer.setColor(floorColor);
		
		float scaleFactor = Gdx.graphics.getHeight() / (1.414f * size);
		
		renderer.translate(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		renderer.rotate(0,0,1,rotation);
		renderer.scale(scale*scaleFactor, scale*scaleFactor, 1);
		renderer.translate(-(float)size/2.0f, -(float)size/2.0f, 0);
		
		float x = 0, y = 0;
		for(int i = 0; i < size*size; i++) {
			x = i % size;
			y = i / size;
			renderer.rect(x, y, 1, 1);
		}

        renderer.end();
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

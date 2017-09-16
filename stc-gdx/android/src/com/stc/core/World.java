package com.stc.core;

import com.stc.core.levels.Tile;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;

public class World
{
	
	private float scale = 1.0f;
	private float rotation = 0.0f;
	private float opacity = 1.0f;
	
	private ShapeRenderer renderer;
	private SpriteBatch sb;
    private BitmapFont font;
    private GlyphLayout layout;
	
	private Interpolator scaleLerp = new Interpolator(1.0f, 1.0f, 0.0f);
	private Interpolator rotationLerp = new Interpolator(0.0f, 0.0f, 0.0f);
	private Interpolator opacityLerp = new Interpolator(1.0f, 1.0f, 0.0f);
	
	public World() {
		renderer = Renderer.shapeRenderer();
		sb = Renderer.spriteBatch();
        font = Renderer.gameFont();
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
	
	public void update(float delta) {
		scaleLerp.update(delta);
		rotationLerp.update(delta);
		opacityLerp.update(delta);
		
		scale = scaleLerp.lerp();
		rotation = rotationLerp.lerp();
		opacity = opacityLerp.lerp();
	}
	
	public void render(Tile[] tiles, int size) {
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.identity();
		
		float scaleFactor = Gdx.graphics.getHeight() / (1.414f * size);

		renderer.translate(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		renderer.rotate(0,0,1,rotation);
		renderer.scale(scale*scaleFactor, scale*scaleFactor, 1);
		renderer.translate(-(float)size/2.0f, -(float)size/2.0f, 0);
		
		renderFloor(size);
		renderShadows(tiles, size);
		renderTiles(tiles, size);
		
		renderer.end();
	}
	
	public void renderTiles(Tile[] tiles, int size) {
		Color c;
		float x = 0, y = 0;
		for(int i = 0; i < size*size; i++) {
			x = i % size;
			y = i / size;
			switch(tiles[i].getType()) {
				case EMPTY:
					break;
				case WALL:
					c = new Color(Globals.COLOR_WALL);
					c.a *= opacity;
					renderer.setColor(c);
					renderer.rect(x, y, 1, 1);
					break;
				default:
					break;
			}
		}
	}
	
	/*
	 * Renders moving objects such as sliders and the ball.
	 * TODO: Make Moveable class to pass in here.
	 */
	public void renderMovables() {
		
	}
	
	public void renderStatic(float x, float y, Tile tile) {
		
	}
	
	private void renderFloor(int size) {
		Color floorColor = new Color(Globals.COLOR_FLOOR);
		floorColor.a *= opacity;
		renderer.setColor(floorColor);
		
		float x = 0, y = 0;
		for(int i = 0; i < size*size; i++) {
			x = i % size;
			y = i / size;
			renderer.rect(x, y, 1, 1);
		}
	}
	
	public void renderShadows(Tile[] tiles, int size) {
		Color shadowColor = new Color(Globals.COLOR_SHADOW);
		shadowColor.a *= opacity;
		renderer.setColor(shadowColor);
		
		Vector2 t = new Vector2(0.1f, 0.1f);
		t.rotate(-rotation);
		renderer.translate(t.x, t.y, 0.0f);
		
		float x = 0, y = 0;
		for(int i = 0; i < size*size; i++) {
			x = i % size;
			y = i / size;
			switch(tiles[i].getType()) {
				case EMPTY:
					break;
				case WALL:
					renderer.rect(x, y, 1, 1);
					break;
				default:
					break;
			}
		}
		
		renderer.translate(-t.x, -t.y, 0.0f);
	}
	
	public void drawString(float x, float y, String text, float rotation) {
		layout = new GlyphLayout(font, text);
		
		sb.begin();
		Matrix4 m = new Matrix4().idt();
		
		m.translate(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		m.rotate(0, 0, 1, rotation);
		m.scale(scale, scale, 1);
		
		//sb.setTransformMatrix(m);
        font.draw(sb, text, 0, layout.height/2.0f, Integer.MAX_VALUE, Align.center, false);
        sb.end();

        // Fix the blend state back (sb.end() resets it)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void scale(float target, float duration) {
		scaleLerp.begin(scale, target, duration);
	}
	
	public void rotate(float degrees, float duration) {
		rotationLerp.begin(rotation, rotation + degrees, duration);
	}
	
	public void rotate(float degrees) {
		rotation += degrees;
		rotationLerp.clear(rotation);
	}
	
	public void fade(float target, float duration) {
		opacityLerp.begin(opacity, target, duration);
	}
	
	public void setupScaleLerp(float from, float to, float duration) {
		scaleLerp.begin(from, to, duration);
	}
	
	public void setupRotationLerp(float from, float to, float duration) {
		rotationLerp.begin(from, to, duration);
	}
	
	public void setupOpacityLerp(float from, float to, float duration) {
		opacityLerp.begin(from, to, duration);
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
	
	public boolean changing() {
		return scaleLerp.active() || rotationLerp.active() || opacityLerp.active();
	}
	
}

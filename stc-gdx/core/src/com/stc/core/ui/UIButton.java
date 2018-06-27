package com.stc.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.stc.core.Renderer;

public class UIButton
{
	private Rectangle rect;
	private Texture texture;
	private SpriteBatch sb;
	private float rotation = 0.0f;
	
	public UIButton(float x, float y, float width, float height, String texture) {
		this.texture = new Texture(Gdx.files.internal(texture));
		sb = Renderer.spriteBatch();
		rect = new Rectangle(x, y, width, height);
	}
	
	public UIButton(float x, float y, float width, float height, String texture, float rotation) {
		this.texture = new Texture(Gdx.files.internal(texture));
		sb = Renderer.spriteBatch();
		rect = new Rectangle(x, y, width, height);
		this.rotation = rotation;
	}
	
	/*
	 * Draws the button in a static location, using only the scale and opacity from the world
	 */
	public void render(float scale) {
		sb.begin();

		Matrix4 m = new Matrix4().idt();
		m.translate(rect.x, rect.y, 0);
		m.scale(scale, scale, 1);
		m.rotate(0, 0, 1, rotation);
		sb.setTransformMatrix(m);
		
		sb.draw(texture, -rect.width/2, -rect.height/2, rect.width, rect.height);
        sb.end();

        // Fix the blend state back (sb.end() resets it)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public boolean contains(float x, float y) {
		Rectangle actual = new Rectangle(rect);
		actual.x -= actual.width/2;
		actual.y -= actual.height/2;
		return actual.contains(x, y);
	}
	
}

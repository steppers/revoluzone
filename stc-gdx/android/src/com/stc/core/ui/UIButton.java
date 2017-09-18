package com.stc.core.ui;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;
import com.stc.core.*;

public class UIButton
{
	private Rectangle rect;
	private Texture texture;
	private SpriteBatch sb;
	
	public UIButton(float x, float y, float width, float height, String texture) {
		this.texture = new Texture(Gdx.files.internal(texture));
		sb = Renderer.spriteBatch();
		rect = new Rectangle(x, y, width, height);
	}
	
	/*
	 * Draws the button in a static location, using only the scale and opacity from the world
	 */
	public void render(World world) {
		sb.begin();

		Matrix4 m = new Matrix4().idt();
		m.translate(rect.x, rect.y, 0);
		m.scale(world.getScale(), world.getScale(), 1);
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

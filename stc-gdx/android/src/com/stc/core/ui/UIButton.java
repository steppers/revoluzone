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
	private String text;
	
	private ShapeRenderer renderer;
	private SpriteBatch sb;
    private BitmapFont font;
    private GlyphLayout layout;
	
	public UIButton(float x, float y, String text) {
		this.text = text;
		
		renderer = Renderer.shapeRenderer();
		sb = Renderer.spriteBatch();
		font = Renderer.gameFont();
		
		layout = new GlyphLayout(font, text);
		rect = new Rectangle(x, y, layout.width, layout.height);
	}
	
	/*
	 * Draws the button in a static location, using only the scale and opacity from the world
	 */
	public void drawStatic(World world) {
		sb.begin();

		Matrix4 m = new Matrix4().idt();
		m.translate(rect.x, rect.y, 0);
		m.scale(world.getScale(), world.getScale(), 1);
		sb.setTransformMatrix(m);
		
		font.setColor(Globals.COLOR_TEXT);
        font.draw(sb, text, -rect.width/2, rect.height/2, rect.width, Align.center, false);
        sb.end();

        // Fix the blend state back (sb.end() resets it)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		renderer.begin(ShapeRenderer.ShapeType.Line);
		
		renderer.identity();
		renderer.translate(rect.x, rect.y, 0);
		renderer.scale(world.getScale(), world.getScale(), 1.0f);
		
		renderer.setColor(Globals.COLOR_BUTTON_BORDER);
		renderer.rect(-rect.width/2 - 4, -rect.height/2 - 4, rect.width + 8, rect.height + 8);
		
		renderer.end();
	}
	
	public boolean over(float x, float y) {
		
		return false;
	}
	
}

package com.stc.core.levels;
import com.stc.core.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.*;

public class Ball extends Moveable
{
	
	public Ball(int x, int y) {
		super(x, y);
	}
	
	public void render(World world) {
		ShapeRenderer g = Renderer.shapeRenderer();
		
		Color c = new Color(0.4f, 0.4f, 1.0f, 1.0f);
		c.a *= world.getOpacity();
		g.setColor(c);
		g.ellipse(x + 0.1f, y + 0.1f, 0.8f, 0.8f, 32);
	}
	
}

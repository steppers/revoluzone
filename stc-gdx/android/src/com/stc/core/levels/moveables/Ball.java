package com.stc.core.levels.moveables;
import com.stc.core.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.*;
import com.stc.core.levels.*;

public class Ball extends Moveable
{
	
	public Ball(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void renderObject(ShapeRenderer g, float opacity) {
		Color c = new Color(Globals.COLOR_BALL);
		c.a *= opacity;
		g.setColor(c);
		g.ellipse(x + 0.1f, y + 0.1f, 0.8f, 0.8f, 32);
	}
	
	@Override
	public void renderShadow(ShapeRenderer g, float opacity) {
		Color c = new Color(Globals.COLOR_SHADOW);
		c.a *= opacity;
		g.setColor(c);
		g.ellipse(x + 0.1f, y + 0.1f, 0.8f, 0.8f, 32);
	}

	@Override
	protected void onActivate(){}

	@Override
	protected void onDeactivate(){}
	
}

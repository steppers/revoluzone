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
		g.setColor(Globals.getColor(Globals.COLOR_BALL, opacity));
		g.ellipse(x + 0.1f, y + 0.1f, 0.8f, 0.8f, 32);
	}
	
	@Override
	public void renderShadow(ShapeRenderer g, float opacity) {
		g.setColor(Globals.getColor(Globals.COLOR_SHADOW, opacity));
		g.ellipse(x + 0.1f, y + 0.1f, 0.8f, 0.8f, 32);
	}

	@Override
	protected void onActivate(LevelObject activator){}

	@Override
	protected void onDeactivate(LevelObject activator){}
	
}

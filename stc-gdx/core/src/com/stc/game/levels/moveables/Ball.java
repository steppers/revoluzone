package com.stc.game.levels.moveables;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.game.Globals;
import com.stc.game.levels.LO_TYPE;
import com.stc.game.levels.LevelObject;

public class Ball extends LevelObject
{
	
	public Ball(int x, int y) {
		super(x, y, LO_TYPE.BALL);
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

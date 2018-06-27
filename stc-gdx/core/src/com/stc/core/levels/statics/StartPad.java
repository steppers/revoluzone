package com.stc.core.levels.statics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.core.Globals;
import com.stc.core.levels.LO_TYPE;
import com.stc.core.levels.LevelObject;

public class StartPad extends LevelObject
{
	
	public StartPad(int x, int y) {
		super(x, y, LO_TYPE.START);
	}
	
	@Override
	public void update(float delta) {}

	@Override
	public void renderObject(ShapeRenderer g, float opacity){}

	@Override
	public void renderFloor(ShapeRenderer g, float opacity)
	{
		g.setColor(Globals.getColor(Globals.COLOR_START_PAD_OUTER, opacity));
		g.ellipse(x + 0.15f, y + 0.15f, 0.7f, 0.7f, 32);
		
		g.setColor(Globals.getColor(Globals.COLOR_START_PAD_INNER, opacity));
		g.ellipse(x + 0.18f, y + 0.18f, 0.64f, 0.64f, 32);
	}

	@Override
	public void renderShadow(ShapeRenderer g, float opacity){}

	@Override
	protected void onActivate(LevelObject activator){}

	@Override
	protected void onDeactivate(LevelObject activator){}
	
}

package com.stc.game.levels.moveables;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.game.Globals;
import com.stc.game.levels.LO_TYPE;
import com.stc.game.levels.LevelInstance;
import com.stc.game.levels.LevelObject;

public class Slider extends LevelObject
{

	public Slider(int x, int y) {
		super(x, y, LO_TYPE.SLIDER);
	}

	@Override
	public void renderObject(ShapeRenderer g, float opacity) {
		g.setColor(Globals.getColor(Globals.COLOR_SLIDER, opacity));
		g.rect(x, y, 1, 1);
	}

	@Override
	public void renderShadow(ShapeRenderer g, float opacity) {
		g.setColor(Globals.getColor(Globals.COLOR_SHADOW, opacity));
		g.rect(x, y, 1, 1);
	}

	@Override
	protected void onActivate(LevelObject activator){}

	@Override
	protected void onDeactivate(LevelObject activator){}
	
	@Override
	public boolean canMoveTo(int x, int y, LevelInstance level) {
		return level.getStaticOfTypeAt(x, y, LO_TYPE.RAIL) != null;
	}

}

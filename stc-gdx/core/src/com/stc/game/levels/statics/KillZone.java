package com.stc.game.levels.statics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.game.Globals;
import com.stc.game.levels.LO_TYPE;
import com.stc.game.levels.LevelObject;
import com.stc.game.levels.moveables.Ball;

public class KillZone extends LevelObject
{

	public KillZone(int x, int y) {
		super(x, y, LO_TYPE.KILLZONE);
	}

	@Override
	public void update(float delta) {}

	@Override
	public void renderObject(ShapeRenderer g, float opacity){}

	@Override
	public void renderFloor(ShapeRenderer g, float opacity)
	{
		boolean yellow = true;
		for(float s = 0.05f; s < 0.5f; s += 0.1f) {
			if(yellow)
				g.setColor(Globals.getColor(Color.YELLOW, opacity));
			else
				g.setColor(Globals.getColor(Color.BLACK, opacity));
			g.ellipse(x + s, y + s, 1 - 2*s, 1 - 2*s, 32);
			yellow = !yellow;
		}
	}

	@Override
	public void renderShadow(ShapeRenderer g, float opacity){}

	@Override
	protected void onActivate(LevelObject activator){
		if(activator instanceof Ball) {
			level.reset();
		}
	}

	@Override
	protected void onDeactivate(LevelObject activator){}

}

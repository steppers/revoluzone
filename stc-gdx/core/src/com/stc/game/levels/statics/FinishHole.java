package com.stc.game.levels.statics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.game.Globals;
import com.stc.game.levels.LO_TYPE;
import com.stc.game.levels.LevelObject;

public class FinishHole extends LevelObject
{

	public FinishHole(int x, int y) {
		super(x, y, LO_TYPE.FINISH);
	}

	@Override
	public void update(float delta) {}

	@Override
	public void renderObject(ShapeRenderer g, float opacity){}

	@Override
	public void renderFloor(ShapeRenderer g, float opacity)
	{
		g.setColor(Globals.getColor(Globals.COLOR_FINISH_OUTER, opacity));
		g.ellipse(x + 0.05f, y + 0.05f, 0.9f, 0.9f, 32);

		g.setColor(Globals.getColor(Globals.COLOR_FINISH_INNER, opacity));
		g.ellipse(x + 0.1f, y + 0.1f, 0.8f, 0.8f, 32);
	}

	@Override
	public void renderShadow(ShapeRenderer g, float opacity){}

	@Override
	protected void onActivate(LevelObject activator){
		if(activator.getType() == LO_TYPE.BALL) {
			level.removeMoveable(activator);
			level.refreshUpdate();
			if(level.getBallCount() == 0)
				level.triggerNextLevel();
		}
	}

	@Override
	protected void onDeactivate(LevelObject activator){}

}

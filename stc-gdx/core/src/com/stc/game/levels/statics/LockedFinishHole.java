package com.stc.game.levels.statics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.game.Globals;
import com.stc.game.levels.LO_TYPE;
import com.stc.game.levels.LevelObject;
import com.stc.game.levels.moveables.Moveable;

public class LockedFinishHole extends LevelObject
{

	public LockedFinishHole(int x, int y) {
		super(x, y, LO_TYPE.LOCKED_FINISH);
		active = false;
		solid = true;
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

		if(isActive())
			g.setColor(Globals.getColor(Globals.COLOR_FINISH_INNER, opacity));
		else
			g.setColor(Globals.getColor(Globals.COLOR_SWITCH_INNER_INACTIVE, opacity));
		g.ellipse(x + 0.1f, y + 0.1f, 0.8f, 0.8f, 32);
	}

	@Override
	public void renderShadow(ShapeRenderer g, float opacity){}

	@Override
	protected void onActivate(LevelObject activator){
		if(activator.getType() == LO_TYPE.BALL) {
			level.removeMoveable((Moveable)activator);
			level.refreshUpdate();
			if(level.getBallCount() == 0)
				level.triggerNextLevel();
		}
		else
		{
			solid = false;
		}
	}

	@Override
	protected void onDeactivate(LevelObject activator){}

}

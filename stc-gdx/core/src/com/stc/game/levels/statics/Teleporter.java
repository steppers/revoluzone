package com.stc.game.levels.statics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.game.Globals;
import com.stc.game.levels.LO_TYPE;
import com.stc.game.levels.LevelObject;

public class Teleporter extends LevelObject
{

	public Teleporter(int x, int y) {
		super(x, y, LO_TYPE.TELEPORT);
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

		g.setColor(Globals.getColor(Globals.COLOR_TELEPORTER_INNER, opacity));
		g.ellipse(x + 0.1f, y + 0.1f, 0.8f, 0.8f, 32);
	}

	@Override
	public void renderShadow(ShapeRenderer g, float opacity){}

	@Override
	protected void onActivate(LevelObject activator){
		if(links.size() == 0)
			return;
		
		if(activator.getType() == LO_TYPE.BALL || activator.getType() == LO_TYPE.SLIDER) {
			//m.moveTo((int)links.get(0).x, (int)links.get(0).y);
		}
	}

	@Override
	protected void onDeactivate(LevelObject activator){}

}

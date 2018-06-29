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
		g.setColor(Globals.getColor(Globals.COLOR_FINISH_OUTER, opacity));
		g.rect(x,y,1,1);
		g.setColor(Globals.getColor(Globals.COLOR_FINISH_INNER, opacity));
		g.rect(x + 0.08f, y + 0.08f, 0.84f, 0.84f);
		
		g.setColor(Globals.getColor(Color.YELLOW, opacity));
		g.translate(x + 0.5f, y + 0.5f, 0);
		g.rotate(0,0,1,45);
		g.translate(-x - 0.5f, -y - 0.5f, 0);
		
		g.rect(x + 0.2f, y + 0.4f, 0.6f, 0.2f);
		g.rect(x + 0.4f, y + 0.2f, 0.2f, 0.6f);
		
		g.translate(x + 0.5f, y + 0.5f, 0);
		g.rotate(0,0,1,-45);
		g.translate(-x - 0.5f, -y - 0.5f, 0);
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

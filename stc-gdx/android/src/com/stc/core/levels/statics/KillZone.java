package com.stc.core.levels.statics;

import com.stc.core.levels.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.stc.core.*;
import com.stc.core.levels.moveables.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;

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

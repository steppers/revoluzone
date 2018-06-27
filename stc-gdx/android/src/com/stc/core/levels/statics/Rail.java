package com.stc.core.levels.statics;

import com.stc.core.levels.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.stc.core.*;
import com.stc.core.levels.moveables.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;

public class Rail extends LevelObject
{

	public Rail(int x, int y) {
		super(x, y, LO_TYPE.RAIL);
	}

	@Override
	public void update(float delta) {}

	@Override
	public void renderObject(ShapeRenderer g, float opacity){}

	@Override
	public void renderFloor(ShapeRenderer g, float opacity)
	{
		g.setColor(Globals.getColor(Color.BLACK, opacity));
		for(LevelObject r : links) {
			if(r.x < x)
				g.rect(x, y + 0.4f, 0.6f, 0.2f);
			else if(r.x > x)
				g.rect(x + 0.4f, y + 0.4f, 0.6f, 0.2f);
			else if(r.y > y)
				g.rect(x + 0.4f, y + 0.4f, 0.2f, 0.6f);
			else if(r.y < y)
				g.rect(x + 0.4f, y, 0.2f, 0.6f);
		}
	}

	@Override
	public void renderShadow(ShapeRenderer g, float opacity){}

	@Override
	protected void onActivate(LevelObject activator){}

	@Override
	protected void onDeactivate(LevelObject activator){}

}

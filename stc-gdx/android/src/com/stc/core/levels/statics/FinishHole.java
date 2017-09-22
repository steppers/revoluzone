package com.stc.core.levels.statics;
import com.stc.core.levels.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.stc.core.*;

public class FinishHole extends LevelObject
{

	public FinishHole(int x, int y) {
		super(x, y);
	}

	@Override
	public void update(float delta) {}

	@Override
	public void renderObject(ShapeRenderer g, float opacity){}

	@Override
	public void renderFloor(ShapeRenderer g, float opacity)
	{
		if(isActive()) {
			g.setColor(Globals.getColor(Globals.COLOR_START_PAD_INNER, opacity));
		} else {
			g.setColor(Globals.getColor(Globals.COLOR_FINISH_OUTER, opacity));
		}
		g.ellipse(x + 0.05f, y + 0.05f, 0.9f, 0.9f, 32);

		g.setColor(Globals.getColor(Globals.COLOR_FINISH_INNER, opacity));
		g.ellipse(x + 0.1f, y + 0.1f, 0.8f, 0.8f, 32);
	}

	@Override
	public void renderShadow(ShapeRenderer g, float opacity){}

	@Override
	protected void onActivate(){}

	@Override
	protected void onDeactivate(){}

}

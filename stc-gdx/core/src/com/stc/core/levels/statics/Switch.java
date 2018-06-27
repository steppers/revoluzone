package com.stc.core.levels.statics;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.core.Globals;
import com.stc.core.levels.LO_TYPE;
import com.stc.core.levels.LevelObject;
import com.stc.core.levels.moveables.Moveable;

public class Switch extends LevelObject
{
	
	LevelObject trigger;

	public Switch(int x, int y) {
		super(x, y, LO_TYPE.SWITCH);
	}

	@Override
	public void update(float delta) {}

	@Override
	public void renderObject(ShapeRenderer g, float opacity){}

	@Override
	public void renderFloor(ShapeRenderer g, float opacity)
	{
		g.setColor(Globals.getColor(Globals.COLOR_SWITCH_OUTER, opacity));
		g.rect(x + 0.15f, y + 0.15f, 0.7f, 0.7f);

		if(isActive())
			g.setColor(Globals.getColor(Globals.COLOR_SWITCH_INNER_ACTIVE, opacity));
		else
			g.setColor(Globals.getColor(Globals.COLOR_SWITCH_INNER_INACTIVE, opacity));
		g.ellipse(x + 0.25f, y + 0.25f, 0.5f, 0.5f, 32);
	}

	@Override
	public void renderShadow(ShapeRenderer g, float opacity){}

	@Override
	protected void onActivate(LevelObject activator){
		if(!((Moveable)activator).isMoving()) {
			for(LevelObject trigger : links) {
				trigger.setActive(true, this);
			}
		} else {
			active = false;
		}
	}

	@Override
	protected void onDeactivate(LevelObject activator){}
	
	public void addTrigger(LevelObject trigger) {
		this.trigger = trigger;
	}

}

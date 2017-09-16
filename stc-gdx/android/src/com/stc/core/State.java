package com.stc.core;

/**
 * Created by steppers on 6/30/17.
 */

public abstract class State {

    public abstract void update(float delta);
    public abstract void render();
	public abstract void renderText();
	
	protected void transitionTo(State state) {
		StateManager.transitionTo(state);
	}

}

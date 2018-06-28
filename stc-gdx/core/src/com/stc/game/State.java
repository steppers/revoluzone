package com.stc.game;

/**
 * Created by steppers on 6/30/17.
 */

public abstract class State {

    public abstract void update(float delta);
    public abstract void render();
	
	protected void transitionTo(State state) {
		StateManager.transitionTo(state);
	}

}

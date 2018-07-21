package com.stc.game;

import com.stc.game.states.SplashState;

/**
 * Created by steppers on 8/7/17.
 */

public class StateManager {

    private static State currentState = new SplashState();
	
	public static void update(float delta) {
		currentState.update(delta);
	}
	
	public static void render() {
		currentState.render();
	}
	
	public static void transitionTo(State state) {
		currentState = state;
	}

}

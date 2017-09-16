package com.stc.core;
import com.stc.core.states.*;

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
		currentState.renderText();
	}
	
	public static void transitionTo(State state) {
		currentState = state;
	}

}

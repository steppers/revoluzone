package com.stc.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by steppers on 8/2/17.
 */

public class Time {

    private static float delta;

    public static void recalculateDelta() {
        delta = Gdx.graphics.getDeltaTime();
    }

    public static float delta() {
        return delta;
    }
	
	public static long current() {
		return TimeUtils.millis();
	}

}

package com.stc.core;

import com.badlogic.gdx.Gdx;

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

}

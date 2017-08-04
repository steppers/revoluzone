package com.stc.proto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by steppers on 8/2/17.
 */

public class Renderer {

    private static ShapeRenderer sShapeRenderer;

    public static void init() {
        sShapeRenderer = new ShapeRenderer();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static ShapeRenderer shapeRenderer() {
        return sShapeRenderer;
    }

}

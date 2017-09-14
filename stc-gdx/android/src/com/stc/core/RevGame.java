package com.stc.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.core.levels.LevelManager;
import com.stc.core.states.SplashState;

/**
 * Created by steppers on 8/2/17.
 */

public class RevGame extends ApplicationAdapter {

    private Background bg;

    @Override
    public void create () {
        Renderer.init(); // Sets GL Blend modes and creates the shape renderer we use
        bg = new Background();

        LevelManager.instance().getLevel("test");
    }

    private void update() {
        // Update the background
        bg.update(Time.delta());

        // Update the current state
        StateManager.update(Time.delta());

        // Break on screen touch for testing
        if(Gdx.input.isTouched()) {
            System.out.println("Screen touched. Exiting...");
            Gdx.app.exit();
        }
    }

    @Override
    public void render () {
        Time.recalculateDelta(); //Update delta time

        //Trigger update first ------------------------------------------------
        update();

        //Render everything ---------------------------------------------------
        bg.render();
        StateManager.render();
    }

    @Override
    public void resize (int width, int height) {
        Globals.display_width = width;
        Globals.display_height = height;
    }

    @Override
    public void dispose () {
        Renderer.dispose();
    }
}

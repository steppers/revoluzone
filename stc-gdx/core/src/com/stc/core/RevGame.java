package com.stc.core;

import com.badlogic.gdx.ApplicationAdapter;

/**
 * Created by steppers on 8/2/17.
 */

public class RevGame extends ApplicationAdapter {

    private Background bg;

    @Override
    public void create () {
        Renderer.init(); // Sets GL Blend modes and creates the shape renderer we use
        bg = new Background();
    }

    private void update() {
        // Update the background
        bg.update(Time.delta());

        // Update the current state
        StateManager.update(Time.delta());
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

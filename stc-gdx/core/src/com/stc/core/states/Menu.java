package com.stc.states;

import com.badlogic.gdx.Gdx;
import com.stc.Main;
import com.stc.State;

/**
 * Created by steppers on 6/30/17.
 */

public class Menu implements State {

    @Override
    public void update(float delta) {
        Main.model.rotation += 120 * delta;
        Main.model.update(delta);

        if(Gdx.input.isTouched()) {
            System.out.println("Screen touched. Exiting...");
            Gdx.app.exit();
        }
    }

    @Override
    public void render() {
        Main.model.render();
    }

    @Override
    public void dispose() {

    }
}

package com.stc;

/**
 * Created by steppers on 6/30/17.
 */

public interface State {

    void update(float delta);
    void render();
    void dispose();

}

package com.stc.proto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

import static com.stc.proto.Globals.*;

/**
/**
 * Created by steppers on 6/29/17.
 */

public class Background {

    private ArrayList<BackgroundBox> bgBoxes;
    private ShapeRenderer renderer;

    public Background() {
        bgBoxes = new ArrayList<BackgroundBox>();
        for(int i = 0; i < 5; i++) {
            bgBoxes.add(new BackgroundBox());
        }
        renderer = Renderer.shapeRenderer();
    }

    public void update(float delta) {
        for(int j = 0; j < 5; j++) {
            bgBoxes.get(j).update(delta);
        }
    }

    public void render() {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Renderer.shapeRenderer().begin(ShapeRenderer.ShapeType.Filled);

        renderer.identity();

        for(BackgroundBox bb : bgBoxes) {
            float op = (float)(Math.sin(bb.opacity)/2)+0.5f;
            renderer.setColor(1.0f,1.0f,1.0f,op*0.5f);
            renderer.rectLine(bb.x, bb.y, bb.x + bb.side, bb.y, 3);
            renderer.rectLine(bb.x, bb.y+bb.side, bb.x + bb.side, bb.y+bb.side, 3);
            renderer.rectLine(bb.x+bb.side, bb.y, bb.x+bb.side, bb.y+bb.side, 3);
            renderer.rectLine(bb.x, bb.y, bb.x, bb.y+bb.side, 3);
            if ((((Math.sin(bb.opacity-0.1f)/2)+0.5f) > op && op < 0.1))  {
                bb.redefinePosition();
            }
        }

        Renderer.shapeRenderer().end();
    }

    private class BackgroundBox {
        float opacity = 0;
        float x, y, side;

        public BackgroundBox() {
            redefinePosition();
            opacity = (float)Math.random() * 4f;
        }

        void update(float delta) {
            opacity += 3f * delta;
        }

        void redefinePosition() {
            x = (float) Math.random() * display_width;
            y = (float) Math.random() * display_height;
            side = (float) (display_width * (Math.random()+0.5f) * 0.15f);
        }
    }

}

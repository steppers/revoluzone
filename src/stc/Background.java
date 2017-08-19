package stc;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

/**
 * Created by Ollie on 20/05/2017.
 */
public class Background {

    ArrayList<BackgroundBox> bgBoxes;

    public Background(GameContainer gc) {
        bgBoxes = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            bgBoxes.add(new BackgroundBox(gc));
        }
    }

    public void update(float delta) {
        for(int j = 0; j < 5; j++) {
            bgBoxes.get(j).update(delta);
        }
    }

    public void render(GameContainer gc, Graphics graphics) {
        graphics.setBackground(Color.lightGray);
        graphics.clear();
        for(BackgroundBox bb : bgBoxes) {
            float op = (float)(Math.sin(bb.opacity)/2)+0.5f;
            graphics.setLineWidth(5);
            graphics.setColor(new Color(bb.r,bb.g,bb.b,0.35f*op));
            graphics.drawRect(bb.x, bb.y, bb.side, bb.side);
            if ((((Math.sin(bb.opacity-0.1f)/2)+0.5f) > op &&
                    op < 0.1))  {
                bb.redefinePosition(gc);
                bb.r = (float)Math.random();
                bb.g = 0.5f*(float)Math.random();
                bb.b = 1- bb.r;
            }
        }
    }

    private class BackgroundBox {
        float opacity = 0;
        float x, y, side;
        float r = (float)Math.random();
        float g = 0.5f*(float)Math.random();
        float b = 1- r;

        public BackgroundBox(GameContainer gc) {
            redefinePosition(gc);
            opacity = (float)Math.random();
        }

        void update(float delta) {
            opacity += 3f * delta;
        }

        void redefinePosition(GameContainer gc) {
            x = (float) Math.random() * gc.getWidth();
            y = (float) Math.random() * gc.getHeight();
            side = (float) (gc.getWidth() * (Math.random()+0.5f) * 0.15);
        }
    }

}

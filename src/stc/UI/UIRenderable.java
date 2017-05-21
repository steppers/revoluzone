package stc.UI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Ollie on 21/05/2017.
 */
public abstract class UIRenderable {

    //Params
    public Vector2f anchor = new Vector2f(0,0);
    public Vector2f offset = new Vector2f(0,0);
    public float scale = 1f;
    public float rotation = 0;
    public Color color = new Color(Color.white);

    //Additional
    public float rotationOffset = 0f;
    public float offsetScale = 1f;

    //Private vars
    protected Vector2f displayResolution = new Vector2f();
    protected Vector2f tmp = new Vector2f();
    protected GameContainer gc;

    public UIRenderable(GameContainer gc) {
        this.gc = gc;
    }

    public void update() {
        displayResolution.x = gc.getWidth();
        displayResolution.y = gc.getHeight();
    }

    public void render(Graphics g) {
        g.resetTransform();
        tmp.set(anchor);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        g.rotate((int)tmp.x, (int)tmp.y, rotation + rotationOffset);

        tmp.set(offset);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        tmp.scale(offsetScale);
        g.translate((int)tmp.x, (int)tmp.y);

        tmp.set(anchor);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        g.translate((int)tmp.x, (int)tmp.y);
        g.pushTransform();

        drawComponent(g);

        g.popTransform();
    }

    protected abstract void drawComponent(Graphics g);

    public void offsetRotation(float rotationOffset) {
        this.rotationOffset = rotationOffset;
    }

    public void scaleOffset(float scale) {
        this.offsetScale = scale;
    }

}

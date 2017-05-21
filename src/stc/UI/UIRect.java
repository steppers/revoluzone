package stc.UI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Created by Ollie on 21/05/2017.
 */
public class UIRect extends UIRenderable {

    public float width = 0;
    public float height = 0;

    public UIRect(GameContainer gc){
        super(gc);
        update();
    }

    public UIRect(float x, float y, float width, float height, GameContainer gc) {
        super(gc);
        this.anchor.set(x, y);
        this.width = width;
        this.height = height;
        color = new Color(Color.lightGray).darker(0.2f);
        update();
    }

    @Override
    protected void drawComponent(Graphics g) {
        g.setColor(color);
        g.pushTransform();

        float textCenterX = width / 2;
        float textCenterY = height / 2;
        textCenterX *= scale * (displayResolution.x/1920f);
        textCenterY *= scale * (displayResolution.y/1080f);

        tmp.set(-textCenterX, -textCenterY);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        g.translate((int)tmp.x, (int)tmp.y);

        g.scale(scale * (displayResolution.x/1920f), scale * (displayResolution.y/1080f));
        g.fillRect(0, 0, width * displayResolution.x, height * displayResolution.y);
        g.setLineWidth(3);
        g.setColor(color.darker(0.1f));
        g.drawRect(0, 0, width * displayResolution.x, height * displayResolution.y);

        g.popTransform();
    }

    public UIRect clone() {
        UIRect r = new UIRect(gc);
        r.width = width;
        r.height = height;
        r.anchor.set(anchor);
        r.offset.set(offset);
        r.scale = scale;
        r.rotation = rotation;
        r.color = new Color(color);
        r.rotationOffset = rotationOffset;
        r.offsetScale = offsetScale;
        return r;
    }

}

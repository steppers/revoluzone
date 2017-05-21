package stc.UI.proto;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

/**
 * Created by Ollie on 21/05/2017.
 */
public class UIButton extends UIRenderable {

    public interface UIButtonCallback {
        void callback();
    }

    private static final Color boxColor = new Color(Color.lightGray).darker(0.2f);

    private UILabel label;
    private UIRect box;

    private UIButtonCallback onClickCallback;

    public UIButton(String text, GameContainer gc) {
        super(gc);
        label = new UILabel(text, gc);
        label.color = new Color(Color.white);
        box = new UIRect(0, 0, label.getWidth()+0.02f, label.getHeight(), gc);
        box.color = boxColor;
        update();
    }

    public void setText(String text) {
        label = new UILabel(text, gc);
        label.color = new Color(Color.white);
        box = new UIRect(0, 0, label.getWidth()+0.02f, label.getHeight(), gc);
        box.color = boxColor;
        update();
    }

    @Override
    public void update() {
        super.update();

        Transform rotateTrans = Transform.createRotateTransform(rotation + rotationOffset, anchor.x, anchor.y);

        tmp.set(offset);
        tmp.scale(offsetScale);
        Transform offsetTrans = Transform.createTranslateTransform(tmp.x, tmp.y);

        Transform anchorTrans = Transform.createTranslateTransform(anchor.x, anchor.y);
        Transform centerTrans = Transform.createTranslateTransform(-box.width / 2f, -box.height / 2f);
        Transform scaleTrans = Transform.createScaleTransform(scale * (displayResolution.x/1920f), scale * (displayResolution.y/1080f));

        Shape rect = new Rectangle(0, 0, box.width, box.height);
        rect = rect.transform(centerTrans)
                .transform(scaleTrans)
                .transform(anchorTrans)
                .transform(offsetTrans)
                .transform(rotateTrans);

        float mouseX = (float)gc.getInput().getMouseX() / displayResolution.x;
        float mouseY = (float)gc.getInput().getMouseY() / displayResolution.y;

        if(rect.contains(mouseX, mouseY)) {
            if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                box.color = new Color(67, 219, 206);
                if (onClickCallback != null) {
                    onClickCallback.callback();
                }
            }
            if(!gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                box.color = new Color(69, 120, 204);
            }
        } else {
            box.color = boxColor;
        }
    }

    public void setOnClickCallback(UIButtonCallback callback) {
        this.onClickCallback = callback;
    }

    public void setBoxColor(Color color) {
        box.color = color;
    }

    public void setTextColor(Color color) {
        label.color = color;
    }

    @Override
    protected void drawComponent(Graphics g) {
        box.color.a = color.a;
        box.drawComponent(g);
        label.color.a = color.a;
        label.drawComponent(g);
    }

    public float getWidth() {
        return label.getWidth();
    }

    public float getHeight() {
        return label.getHeight();
    }

    public UIButton clone() {
        UIButton r = new UIButton(label.text, gc);
        r.onClickCallback = onClickCallback;
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

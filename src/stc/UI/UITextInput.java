package stc.UI;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import static org.newdawn.slick.Input.*;

/**
 * Created by Ollie on 21/05/2017.
 */
public class UITextInput extends UIRenderable {

    private UILabel label;
    private UIRect box;

    private boolean entryMode = false;
    private float initialWidth = 0;
    private int shiftHeld = 0;

    public UITextInput(String initialText, GameContainer gc) {
        super(gc);
        label = new UILabel(initialText, gc);
        label.color = new Color(Color.white);
        initialWidth = label.getWidth()+0.02f;
        box = new UIRect(0, 0, initialWidth, label.getHeight(), gc);
        box.color = color;
        update();

        gc.getInput().addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(int i, char c) {
                if(entryMode) {
                    switch(i) {
                        case KEY_BACK:
                            if(label.text.length() >= 1)
                                label.text = label.text.substring(0, label.text.length()-1);
                            break;
                        case KEY_LCONTROL:
                        case KEY_RCONTROL:
                        case KEY_LALT:
                        case KEY_RALT:
                        case KEY_ESCAPE:
                        case KEY_TAB:
                        case KEY_ENTER:
                            return;
                        case KEY_LSHIFT:
                        case KEY_RSHIFT:
                            shiftHeld++;
                            break;
                        default:
                            if(shiftHeld > 0) {
                                label.text += String.valueOf(c).toUpperCase();
                            } else {
                                label.text += c;
                            }
                    }
                    if (label.getWidth()+0.02f >= initialWidth)
                        box.width = label.getWidth() + 0.02f;
                    box.height = label.getHeight();
                }
            }

            @Override
            public void keyReleased(int i, char c) {
                if(entryMode) {
                    switch(i) {
                        case KEY_LSHIFT:
                        case KEY_RSHIFT:
                            shiftHeld--;
                            break;
                        default:
                    }
                }
            }
            @Override
            public void setInput(Input input) {}
            @Override
            public boolean isAcceptingInput() {return true;}
            @Override
            public void inputEnded() { }
            @Override
            public void inputStarted() {}
        });
    }

    @Override
    public void update() {
        super.update();

        tmp.set(anchor);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        Transform rotateTrans = Transform.createRotateTransform((float)((rotation + rotationOffset) * Math.PI / 180f), tmp.x, tmp.y);

        tmp.set(offset);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        tmp.scale(offsetScale);
        Transform offsetTrans = Transform.createTranslateTransform(tmp.x, tmp.y);

        tmp.set(anchor);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        Transform anchorTrans = Transform.createTranslateTransform(tmp.x, tmp.y);

        Transform scaleTrans = Transform.createScaleTransform(scale * (displayResolution.x / 1920f), scale * (displayResolution.y / 1080f));
        Transform centerTrans = Transform.createTranslateTransform(-box.width / 2f * displayResolution.x, -box.height / 2f * displayResolution.y);

        Shape rect = new Rectangle(0, 0, box.width * displayResolution.x, box.height * displayResolution.y);
        rect = rect.transform(centerTrans)
                .transform(scaleTrans)
                .transform(anchorTrans)
                .transform(offsetTrans)
                .transform(rotateTrans);

        float mouseX = (float) gc.getInput().getMouseX();
        float mouseY = (float) gc.getInput().getMouseY();

        if (rect.contains(mouseX, mouseY)) {
            if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                box.color = new Color(68, 104, 163);
                entryMode = true;
            }
        } else {
            if(entryMode) {
                if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                    box.color = color;
                    entryMode = false;
                    shiftHeld = 0;
                }
            }
        }
    }

    @Override
    protected void drawComponent(Graphics g) {
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

    public boolean acceptingInput() {
        return entryMode;
    }

    public void setColor(Color color) {
        this.color = color;
        box.color = color;
    }

    public String getText() {
        return label.text;
    }

    public UITextInput clone() {
        UITextInput r = new UITextInput(label.text, gc);
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

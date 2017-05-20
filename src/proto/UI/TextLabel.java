package proto.UI;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by Ollie on 19/05/2017.
 */
public class TextLabel {

    public String text = "";
    public Vector2f position = new Vector2f();
    public Vector2f anchor = new Vector2f(0,0);
    public Vector2f offset = new Vector2f(0,0);
    public float scale = 1f;
    public float rotation = 0;
    public Color color = Color.white;

    //Additional
    public float rotationOffset = 0f;
    public float offsetScale = 1f;

    public TextLabel() {

    }

    public TextLabel(String text) {
        this.text = text;
    }

    public TextLabel(String text, Vector2f position) {
        this.text = text;
        this.position = position;
    }

    public void offsetRotation(float rotationOffset) {
        this.rotationOffset = rotationOffset;
    }

    public void scaleOffset(float scale) {
        this.offsetScale = scale;
    }

    @Override
    public TextLabel clone() {
        TextLabel l = new TextLabel(text);
        l.position.set(position);
        l.anchor.set(anchor);
        l.offset.set(offset);
        l.scale = scale;
        l.rotation = rotation;
        l.color = new Color(color);
        l.rotationOffset = rotationOffset;
        l.offsetScale = offsetScale;
        return l;
    }
}

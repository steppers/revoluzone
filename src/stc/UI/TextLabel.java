package stc.UI;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import stc.Model;

/**
 * Created by Ollie on 19/05/2017.
 */
public class TextLabel {

    public String text = "";
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

    public TextLabel(String text, float posX, float posY, float offsetX, float offsetY) {
        this.text = text;
        this.anchor.set(posX, posY);
        this.offset.set(offsetX, offsetY);
        this.color = Color.green.darker(0.4f);
    }

    public void offsetRotation(float rotationOffset) {
        this.rotationOffset = rotationOffset;
    }

    public void scaleOffset(float scale) {
        this.offsetScale = scale;
    }

    public int getWidth(Font font) {
        String[] lines = text.split("\n");
        int width = 0, tmp;
        for (String l : lines) {
            tmp = font.getWidth(l);
            if(tmp > width)
                width = tmp;
        }
        return width;
    }

    public int getHeight(Font font) {
        String[] lines = text.split("\n");
        int totalHeight = 0;
        for (String l : lines) {
            if(totalHeight != 0)
                totalHeight += 3;
            totalHeight += font.getHeight(l);
        }
        return totalHeight;
    }

    public void render(Graphics g, TextRenderer tr, Model m) {
        scale = m.getScale();
        scaleOffset(m.getScale());
        color.a = (m.getScale()-0.6f)*2f;
        tr.renderText(g, this);
    }

    @Override
    public TextLabel clone() {
        TextLabel l = new TextLabel(text);
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

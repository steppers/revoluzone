package stc.UI.proto;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import stc.graphics.FontLoader;

/**
 * Created by Ollie on 21/05/2017.
 */
public class UILabel extends UIRenderable {

    public String text = "";
    public Font font;

    public UILabel(GameContainer gc){
        super(gc);
        font = FontLoader.getFont(FontLoader.Fonts.PixelGame.toString());
        update();
    }

    public UILabel(String text, GameContainer gc) {
        super(gc);
        this.text = text;
        font = FontLoader.getFont(FontLoader.Fonts.PixelGame.toString());
        update();
    }

    @Override
    protected void drawComponent(Graphics g) {
        g.setColor(color);
        g.pushTransform();

        String[] lines = text.split("\n");
        float totalHeight = getHeight(lines);

        float heightPerLine = totalHeight / lines.length;
        float yOffset = totalHeight / 2f;
        float currentY = yOffset;

        for(int i = 0; i < lines.length; i++) {
            //Get pre-calculated transform and push it back
            g.pushTransform();

            float textCenterX = font.getWidth(lines[i]) / displayResolution.x / 2;
            float textCenterY = currentY / displayResolution.y;
            currentY -= heightPerLine;
            textCenterX *= scale * (displayResolution.x / 1920f);
            textCenterY *= scale * (displayResolution.y / 1080f);

            tmp.set(-textCenterX, -textCenterY);
            tmp.x *= displayResolution.x;
            tmp.y *= displayResolution.y;
            g.translate((int) tmp.x, (int) tmp.y);

            g.scale(scale * (displayResolution.x / 1920f), scale * (displayResolution.y / 1080f));
            g.drawString(lines[i], 0, 0);
            g.popTransform();
        }


        g.popTransform();
    }

    public float getWidth() {
        return (float)getWidth(text.split("\n")) / displayResolution.x;
    }

    public float getHeight() {
        return (float)getHeight(text.split("\n")) / displayResolution.y;
    }

    private int getWidth(String[] lines) {
        int width = 0, tmp;
        for (String l : lines) {
            tmp = font.getWidth(l);
            if(tmp > width)
                width = tmp;
        }
        return width;
    }

    private int getHeight(String[] lines) {
        int totalHeight = 0;
        for (String l : lines) {
            if(totalHeight != 0)
                totalHeight += 3;
            totalHeight += font.getHeight(l);
        }
        return totalHeight;
    }

    public UILabel clone() {
        UILabel r = new UILabel(this.text, gc);
        r.font = font;
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

package proto.UI;

import graphics.FontLoader;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import proto.UI.TextLabel;

/**
 * Created by Ollie on 19/05/2017.
 */
public class TextRenderer {

    private Vector2f displayResolution = new Vector2f();
    private Vector2f tmp = new Vector2f();

    private Font font;

    public TextRenderer(GameContainer gc) {
        updateDisplaySize(gc);
        graphics.FontLoader.loadFont(gc);
        font = FontLoader.getFont(FontLoader.Fonts.PixelGame.toString());
    }

    public void updateDisplaySize(GameContainer gc) {
        displayResolution.x = gc.getWidth();
        displayResolution.y = gc.getHeight();
    }

    public void renderText(Graphics g, TextLabel label) {
        g.setColor(label.color);

        g.resetTransform();
        tmp.set(label.anchor);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        g.rotate((int)tmp.x, (int)tmp.y, label.rotation + label.rotationOffset);

        tmp.set(label.offset);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        tmp.scale(label.offsetScale);
        g.translate((int)tmp.x, (int)tmp.y);

        tmp.set(label.anchor);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        g.translate((int)tmp.x, (int)tmp.y);
        g.pushTransform();

        String[] lines = label.text.split("\n");
        int totalHeight = 0;
        for (String l : lines) {
            if(totalHeight != 0)
                totalHeight += 3;
            totalHeight += font.getHeight(l);
        }

        float heightPerLine = (float)totalHeight / lines.length;
        float yOffset = (float)totalHeight / 2f;
        float currentY = yOffset;

        for(int i = 0; i < lines.length; i++) {
            //Get reset transform
            g.popTransform();
            g.pushTransform();

            float textCenterX = font.getWidth(lines[i]) / displayResolution.x / 2;
            float textCenterY = currentY / displayResolution.y;
            currentY -= heightPerLine;
            textCenterX *= label.scale * (displayResolution.x/1920f);
            textCenterY *= label.scale * (displayResolution.y/1080f);

            tmp.set(-textCenterX, -textCenterY);
            tmp.x *= displayResolution.x;
            tmp.y *= displayResolution.y;
            g.translate((int)tmp.x, (int)tmp.y);

            g.scale(label.scale * (displayResolution.x/1920f), label.scale * (displayResolution.y/1080f));
            g.drawString(lines[i], 0, 0);
        }
        g.popTransform();
    }

}

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

        float textCenterX = font.getWidth(label.text) / displayResolution.x / 2;
        float textCenterY = font.getHeight(label.text) / displayResolution.y / 2;
        textCenterX *= label.scale * (displayResolution.x/1920f);
        textCenterY *= label.scale * (displayResolution.y/1080f);

        tmp.set(label.anchor);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        g.rotate(tmp.x, tmp.y, label.rotation + label.rotationOffset);

        tmp.set(label.offset);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        tmp.scale(label.offsetScale);
        g.translate(tmp.x, tmp.y);

        tmp.set(label.anchor);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        g.translate(tmp.x, tmp.y);

        tmp.set(-textCenterX, -textCenterY);
        tmp.x *= displayResolution.x;
        tmp.y *= displayResolution.y;
        g.translate((int)tmp.x, (int)tmp.y);

        g.scale(label.scale * (displayResolution.x/1920f), label.scale * (displayResolution.y/1080f));
        g.drawString(label.text, 0, 0);
    }

}

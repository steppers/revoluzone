package stc.UI;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import stc.Model;
import stc.graphics.FontLoader;

/**
 * Created by Ollie on 20/05/2017.
 */
public class Button {

    public interface ButtonCallback {
        void callback();
    }

    ButtonCallback onMouseClickCallback;

    ClickBox clickBox;
    TextLabel label;

    public Button(String label, float posX, float posY, float offsetX, float offsetY, GameContainer gc) {
        this.label = new TextLabel(label);
        this.label.anchor.set(posX, posY);
        this.label.offset.set(offsetX, offsetY);
        this.label.color = Color.green.darker(0.4f);
        float width = (float)this.label.getWidth(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString())) / gc.getWidth();
        float height = (float)this.label.getHeight(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString())) / gc.getHeight();
        clickBox = new ClickBox(posX+offsetX, posY+offsetY, width, height);
    }

    public void update(GameContainer gc) {
        if(clickBox.isMouseInside(gc)) {
            if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                onMouseClickCallback.callback();
                label.color = Color.red;
            }
            if(!gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                label.color = Color.yellow;
            }
        } else {
            label.color = Color.green.darker(0.4f);
        }
    }

    public void render(Graphics g, TextRenderer tr, Model m) {
        label.scale = m.getScale();
        label.scaleOffset(m.getScale());
        label.color.a = (m.getScale()-0.6f)*2f;
        tr.renderText(g, label);

//        g.resetTransform();
//        g.drawRect();
    }

    public void setOnMouseClickCallback(ButtonCallback callback) {
        this.onMouseClickCallback = callback;
    }

}

package stc.UI;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

/**
 * Created by Ollie on 20/05/2017.
 */
public class ClickBox {

    private Rectangle rect;

    public ClickBox(float x, float y, float width, float height) {
        rect = new Rectangle(x - width/2, y - height/2, width, height);
    }

    public boolean isMouseInside(GameContainer gc) {
        float mouseX = (float)gc.getInput().getMouseX() / gc.getWidth();
        float mousey = (float)gc.getInput().getMouseY() / gc.getHeight();
        if(rect.contains(mouseX, mousey))
            return true;
        return false;
    }

}

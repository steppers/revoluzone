package proto;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import java.util.ArrayList;

/**
 * Created by steppers on 2/12/17.
 */
public class Tile {

    public enum Type {
        EMPTY("Empty"),
        FIXED("Fixed"),
        RED("Red"),
        BLUE("Blue"),
        KILL("Kill"),
        START("Start"),
        FINISH("Finish"),
        SWITCH("Switch"),
        SLIDE("Slide"),
        TELEPORT("Teleport"),
        LOCKED_FINISH("Locked Finish"),
        BLUE_FINISH("Blue Finish"),
        RED_FINISH("Red Finish"),
        GREEN("Green");

        String name;

        Type(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

    public Type type = Type.FIXED;
    public boolean active = false;
    public ArrayList<Tile> links;

    public Tile(int type) {
        this.type = Type.values()[type];
        links = new ArrayList<>();
    }

    public boolean isSolid() {
        switch(type) {
            case EMPTY:
                return false;
            case FIXED:
                return true;
            case RED:
                return active ? true : false;
            case BLUE:
                return active ? true : false;
            case KILL:
                return false;
            case START:
                return false;
            case FINISH:
                return false;
            case SWITCH:
                return false;
            case SLIDE:
                return false;
            case TELEPORT:
                return false;
            case LOCKED_FINISH:
                return true;
            case BLUE_FINISH:
                return active ? true : false;
            case RED_FINISH:
                return active ? true : false;
            case GREEN:
                return active ? true : false;
            default:
                return true;
        }
    }


    public void render(int x, int y, int size, Graphics g, float opacity) {
        Rectangle rect = new Rectangle(0, 0, size, size);
        Shape stripe = rect.transform(Transform.createScaleTransform(0.333f, 1f));
        switch(type) {
            case EMPTY:
                g.setColor(Color.white.darker(0.2f).multiply(new Color(1,1,1,opacity)));
                g.fill(rect);
                break;
            case FIXED:
                g.setColor(Color.white.darker(0.4f).multiply(new Color(1,1,1,opacity)));
                g.fill(rect);
                break;
            case RED:
                g.setColor(Color.red.multiply(new Color(1,1,1,opacity)));
                g.fill(rect);
                break;
            case BLUE:
                g.setColor(Color.blue.multiply(new Color(1,1,1,opacity)));
                g.fill(rect);
                break;
            case KILL:
                g.setColor(Color.yellow.multiply(new Color(1,1,1,opacity)));
                g.fill(rect);
                g.setColor(Color.black.multiply(new Color(1,1,1,opacity)));
                g.fill(stripe);
                break;
            case START:
                break;
            case FINISH:
                break;
            case SWITCH:
                break;
            case SLIDE:
                break;
            case TELEPORT:
                break;
            case LOCKED_FINISH:
                break;
            case BLUE_FINISH:
                break;
            case RED_FINISH:
                break;
            case GREEN:
                g.setColor(Color.green.multiply(new Color(1,1,1,opacity)));
                g.fill(rect);
                break;
            default:
                break;

        }
    }

}

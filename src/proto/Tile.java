package proto;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
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
        RED_FINISH("Red Finish"),
        TELEPORT("Teleport"),
        LOCKED_FINISH("Locked Finish"),
        BLUE_FINISH("Blue Finish"),
        SLIDER("Slider Start");

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
    public boolean isRail = false;
    public int x,y;

    public Tile(int type) {
        this.type = Type.values()[type];
        links = new ArrayList<>();
    }

    public boolean isSolid(Model m) {
        switch(type) {
            case EMPTY:
                return hasSlider(x,y,m);
            case FIXED:
                return true;
            case RED:
                if(active) {
                    return active;
                }
                else{
                    return hasSlider(x,y,m);
                }
            case BLUE:
                if(active) {
                    return active;
                }
                else{
                    return hasSlider(x,y,m);
                }
            case KILL:
                return hasSlider(x,y,m);
            case START:
                return hasSlider(x,y,m);
            case FINISH:
                return hasSlider(x,y,m);
            case SWITCH:
                return hasSlider(x,y,m);
            case LOCKED_FINISH:
                return true;
            case SLIDER:
                return hasSlider(x,y,m);
            default:
                return true;
        }
    }

    public boolean hasSlider(int x, int y, Model m){
        int counter = 0;
        for(int i = 0; i < m.sliders.size(); i++){
            if(x == m.sliders.get(i).destX && y == m.sliders.get(i).destY){
                    counter++;
                    break;
            }
        }
        if(counter == 0){
            return false;
        }
        else{
            return true;
        }
    }


    public void render(int x, int y, int size, Graphics g, float opacity) {
        Rectangle rect = new Rectangle(x, y, size, size);
        Shape stripe = rect.transform(Transform.createScaleTransform(0.333f, 1f));
        stripe = stripe.transform(Transform.createTranslateTransform(+size/2, 0));

        //Switch rect
        Rectangle swrect = new Rectangle(x+(size*0.2f), y+(size*0.2f), size*0.6f, size*0.6f);
        Circle cicleSwitch = new Circle(x+size/2,y+size/2, size*0.5f*0.4f);

        //Start/finish
        Circle circleLarge = new Circle(x+size/2,y+size/2, size*0.5f*0.8f);
        Circle circleSmall = new Circle(x+size/2,y+size/2, size*0.5f*0.7f);

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
                g.setColor(Color.green.darker().multiply(new Color(1,1,1,opacity)));
                g.fill(circleLarge);
                g.setColor(Color.green.multiply(new Color(1,1,1,opacity)));
                g.fill(circleSmall);
                break;
            case FINISH:
                g.setColor(Color.darkGray.multiply(new Color(1,1,1,opacity)));
                g.fill(circleLarge);
                g.setColor(Color.black.multiply(new Color(1,1,1,opacity)));
                g.fill(circleSmall);
                break;
            case SWITCH:
                g.setColor(Color.darkGray.multiply(new Color(1,1,1,opacity)));
                g.fill(swrect);
                if(active)
                    g.setColor(Color.green.multiply(new Color(1,1,1,opacity)));
                else
                    g.setColor(Color.red.multiply(new Color(1,1,1,opacity)));
                g.fill(cicleSwitch);
                break;
            case TELEPORT:
                break;
            case LOCKED_FINISH:
                g.setColor(Color.darkGray.multiply(new Color(1,1,1,opacity)));
                g.fill(circleLarge);
                g.setColor(Color.red.multiply(new Color(1,1,1,opacity)));
                g.fill(circleSmall);
                break;
            case BLUE_FINISH:
                break;
            case RED_FINISH:
                break;
            case SLIDER:
                break;
            default:
                break;

        }
    }

}

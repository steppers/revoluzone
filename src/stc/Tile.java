package stc;

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
        SLIDER("Slider"),
        RAIL("Rail");

        String name;

        Type(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

    public Type type = Type.FIXED;
    public Type resetType = Type.FIXED;
    public boolean active = false;
    public ArrayList<Tile> links;
    public boolean isRail = false;
    public int x,y;

    public Tile(int type) {
        this.type = Type.values()[type];
        resetType = this.type;
        links = new ArrayList<>();
    }

    public void reset(boolean redEnabled) {
        this.type = resetType;
        active = false;
        switch(type) {
            case RED:
                if(redEnabled)
                    active = true;
                else
                    active = false;
                break;
            case BLUE:
                if(redEnabled)
                    active = false;
                else
                    active = true;
                break;
            default:
                break;
        }
    }

    public void activate(Model m) {
        for (Tile t : links) {
            t.activate(m);
        }
        switch(type) {
            case SWITCH:
                active = true;
                break;
            case LOCKED_FINISH:
                type = Tile.Type.FINISH;
                m.recalcAll();
                break;
            default:
                break;
        }
    }

    public boolean isSolid(Model m) {
        switch(type) {
            case EMPTY:
                return hasSlider(m) != null;
            case FIXED:
                return true;
            case RED:
                if(active) {
                    return active;
                }
                else{
                    return hasSlider(m) != null;
                }
            case BLUE:
                if(active) {
                    return active;
                }
                else{
                    return hasSlider(m) != null;
                }
            case LOCKED_FINISH:
                return true;
            default:
                return hasSlider(m) != null;
        }
    }

    public Slider hasSlider(Model m){
        for(int i = 0; i < m.sliders.size(); i++){
            if(this.x == (int)m.sliders.get(i).destX && this.y == (int)m.sliders.get(i).destY){
                return m.sliders.get(i);
            }
        }
        return null;
    }


    public void render(int x, int y, int size, Graphics g, float opacity) {
        Rectangle rect = new Rectangle(x, y, size, size);
        Shape stripe = rect.transform(Transform.createTranslateTransform(-x, -y)).transform(Transform.createScaleTransform(0.2f, 0.7f)).transform(Transform.createTranslateTransform(x, y));
        Shape cross1, cross2;
        stripe = stripe.transform(Transform.createTranslateTransform((float)size/2f, 0));
        cross1 = stripe.transform(Transform.createTranslateTransform(-x, -y)).transform(Transform.createRotateTransform((float)Math.PI/4)).transform(Transform.createTranslateTransform(x, y));
        cross2 = cross1.transform(Transform.createTranslateTransform(-x, -y)).transform(Transform.createRotateTransform(-(float)Math.PI/2)).transform(Transform.createTranslateTransform(x, y));

        Shape railStripe = rect.transform(Transform.createScaleTransform(0.2f, 1f));
        railStripe = railStripe.transform(Transform.createTranslateTransform(((float)size*0.6f)+1, 0));

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
                g.fill(cross1);
                g.fill(cross2);
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
            case RAIL:
                g.setColor(Color.white.darker(0.2f).multiply(new Color(1,1,1,opacity)));
                g.fill(rect);
                g.setColor(Color.black.multiply(new Color(1,1,1,opacity)));
                g.fill(railStripe);
                break;
            case SLIDER:
                g.setColor(Color.magenta.multiply(new Color(0.9f,0.9f,0.9f,1.0f)));
                g.fill(rect);
                break;
            default:
                break;

        }
    }

}
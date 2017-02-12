package proto;

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

}

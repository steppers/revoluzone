package model;

/**
 * Created by steppers on 2/10/17.
 */
public class Tile {

    public static enum Type {
        EMPTY("Empty"),
        FIXED("Fixed"),
        RED("Red"),
        BLUE("Blue"),
        KILL("Kill"),
        START("Start"),
        FINISH("Finish"),
        SWITCH("Switch"),
        SLIDE("Slide"),
        TELEPORT("Teleport");

        String name;

        Type(String name){
         this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

    private Type type = Type.FIXED;
    private boolean active = false;

    public Tile(int type) {
        this.type = Type.values()[type];
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}

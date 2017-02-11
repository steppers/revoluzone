package model;

/**
 * Created by steppers on 2/10/17.
 */
public class Tile {

    public static enum Type {
        EMPTY,
        FIXED,
        RED,
        BLUE,
        KILL,
        START,
        FINISH,
        SWITCH,
        SLIDE,
        TELEPORT
    }

    private Type type = Type.FIXED;
    private boolean active = false;

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

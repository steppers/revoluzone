package model;

/**
 * Created by steppers on 2/10/17.
 */
public class Tile {

    public static final int EMPTY = 0;
    public static final int FIXED = 1;
    public static final int RED = 2;
    public static final int BLUE = 3;

    private int type = FIXED;
    private boolean active = true;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}

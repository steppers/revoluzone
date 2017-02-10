package model;

/**
 * Created by steppers on 2/10/17.
 */
public class Ball {

    private float xf, yf;
    private int xi, yi;

    private int xTarget, yTarget;

    private float velX, velY;
    private boolean moving = false;

    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private Direction dir;

    public Ball(int x, int y) {
        xf = x;
        yf = y;
    }

    public void update(float delta) {
        if(moving) {

        }
    }

    public void setTarget(int x, int y) {
        moving = true;

    }

}

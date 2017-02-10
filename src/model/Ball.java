package model;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by steppers on 2/10/17.
 */
public class Ball {

    private static final float ACCEL = 0.7f;

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

    public Vector2f getPos() {
        return new Vector2f(xf, yf);
    }

    public void update(float delta) {
        if(moving) {
            switch(dir) {
                case UP:
                    velY += ACCEL * delta;
                    yf += velY * delta;
                    if(yf > yTarget) {
                        moving = false;
                        yf = yTarget;
                        yi = yTarget;
                    }
                    break;

                case DOWN:
                    velY -= ACCEL * delta;
                    yf += velY * delta;
                    if(yf < yTarget) {
                        moving = false;
                        yf = yTarget;
                        yi = yTarget;
                    }
                    break;

                case RIGHT:
                    velX += ACCEL * delta;
                    xf += velX * delta;
                    if(xf > xTarget) {
                        moving = false;
                        xf = xTarget;
                        xi = xTarget;
                    }
                    break;

                case LEFT:
                    velX -= ACCEL * delta;
                    xf += velX * delta;
                    if(xf < xTarget) {
                        moving = false;
                        xf = xTarget;
                        xi = xTarget;
                    }
                    break;
            }
        }
    }

    public void setTarget(int x, int y) {
        moving = true;
        int dx = x - xi;
        int dy = y - yi;

        if(dx == 0) {
            if(dy > 0) {
                dir = Direction.UP;
            } else {
                dir = Direction.DOWN;
            }
        } else {
            if(dx > 0) {
                dir = Direction.RIGHT;
            } else {
                dir = Direction.LEFT;
            }
        }
        velX = 0;
        velY = 0;
    }

}

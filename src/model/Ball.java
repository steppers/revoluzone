package model;

import org.newdawn.slick.geom.Vector2f;

/**
 * Created by steppers on 2/10/17.
 */
public class Ball {

    private static final float ACCEL = 90.0f;

    private float x, y;

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
        this.x = x;
        this.y = y;
    }

    public Vector2f getPos() {
        return new Vector2f(x, y);
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(float delta) {
        if(moving) {
            switch(dir) {
                case UP:
                    velY += ACCEL * delta;
                    y += velY * delta;
                    if(y >= yTarget) {
                        moving = false;
                        y = yTarget;
                    }
                    break;

                case DOWN:
                    velY -= ACCEL * delta;
                    y += velY * delta;
                    if(y <= yTarget) {
                        moving = false;
                        y = yTarget;
                    }
                    break;

                case RIGHT:
                    velX += ACCEL * delta;
                    x += velX * delta;
                    if(x >= xTarget) {
                        moving = false;
                        x = xTarget;
                    }
                    break;

                case LEFT:
                    velX -= ACCEL * delta;
                    x += velX * delta;
                    if(x <= xTarget) {
                        moving = false;
                        x = xTarget;
                    }
                    break;
            }
        }
    }

    public void setTarget(int xt, int yt) {
        xTarget = xt;
        yTarget = yt;
        moving = true;
        int dx = xt - (int)x;
        int dy = yt - (int)y;

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

    public boolean isMoving() {
        return moving;
    }

}

package model;

/**
 * Created by steppers on 2/10/17.
 */
public class WorldModel {

    public static final int GRID_SIZE = 10;
    public static final float ROT_VEL = 360f;

    private Tile[][] grid = new Tile[GRID_SIZE][GRID_SIZE];
    private Ball ball = new Ball(4, 1);

    private float rotation = 0f;
    private int rotDir = 0; //0 = left, 1 = right
    private float targetRotation = 0;
    private boolean rotating = false;

    public WorldModel() {
        for(int x = 0; x < GRID_SIZE; x++) {
            for(int y = 0; y < GRID_SIZE; y++) {
                grid[x][y] = new Tile();
                if(x == 0 || x == GRID_SIZE-1 || y == 0 || y == GRID_SIZE-1) {
                    grid[x][y].setType(Tile.FIXED);
                    continue;
                }
                grid[x][y].setType(Tile.EMPTY);
            }
        }

        grid[3][3].setType(Tile.BLUE);
        grid[8][4].setType(Tile.FIXED);
    }

    public void setGrid(Tile[][] grid) {
        this.grid = grid;
    }

    public Tile[][] getGrid() {
        return grid;
    }

    public Ball getBall() {
        return ball;
    }

    public float getRotation() {
        return rotation;
    }

    public void rotate(float angle) {
        targetRotation += angle;
        rotDir = targetRotation < rotation ? 0 : 1;
        rotating = true;
    }

    public void update(float delta) {
        if(rotating) {
            if(rotDir == 0) {
                rotation -= ROT_VEL * delta;
                if(rotation < targetRotation) {
                    rotation = targetRotation;
                    rotating = false;
                    updateBall();
                }
            } else {
                rotation += ROT_VEL * delta;
                if(rotation > targetRotation) {
                    rotation = targetRotation;
                    rotating = false;
                    updateBall();
                }
            }
        }
    }

    private void updateBall() {
        int r = (int)rotation % 360;
        while(r < 0)
            r += 360;
        int y = (int)(ball.getPos().getY());
        int x = (int)(ball.getPos().getX());
        switch(r) {
            case 0:
                for(y = y+1; y < GRID_SIZE; y++) {
                    if(isSolid(grid[x][y])) {
                        ball.setTarget(x, y-1);
                        return;
                    }
                }
                break;
            case 90:
                for(x = x+1; x < GRID_SIZE; x++) {
                    if(isSolid(grid[x][y])) {
                        ball.setTarget(x-1, y);
                        return;
                    }
                }
                break;
            case 180:
                for(y = y-1; y >= 0; y--) {
                    if(isSolid(grid[x][y])) {
                        ball.setTarget(x, y+1);
                        return;
                    }
                }
                break;
            case 270:
                for(x = x-1; x >= 0; x--) {
                    if(isSolid(grid[x][y])) {
                        ball.setTarget(x+1, y);
                        return;
                    }
                }
                break;
        }
    }

    public boolean isRotating() {
        return rotating;
    }

    private boolean isSolid(Tile t) {
        switch(t.getType()) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                return true;
        }
    }

}

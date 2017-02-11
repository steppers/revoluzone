package model;

/**
 * Created by steppers on 2/10/17.
 */
public class WorldModel {

    public static final int GRID_SIZE = 8;
    public static final float ROT_VEL = 360f;

    private Tile[][] grid = new Tile[GRID_SIZE][GRID_SIZE];
    private Ball ball = new Ball(1, 6);

    private float rotation = 0f;
    private int rotDir = 0; //0 = left, 1 = right
    private float targetRotation = 0;
    private boolean rotating = false;

    public WorldModel() {
        for(int x = 0; x < GRID_SIZE; x++) {
            for(int y = 0; y < GRID_SIZE; y++) {
                grid[x][y] = new Tile();
                if(x == 0 || x == GRID_SIZE-1 || y == 0 || y == GRID_SIZE-1) {
                    grid[x][y].setType(Tile.Type.FIXED);
                    continue;
                }
                grid[x][y].setType(Tile.Type.EMPTY);
            }
        }

        grid[3][1].setType(Tile.Type.RED);
        grid[3][1].setActive(true);
        grid[5][1].setType(Tile.Type.RED);
        grid[5][1].setActive(true);
        grid[2][2].setType(Tile.Type.RED);
        grid[2][2].setActive(true);
        grid[4][3].setType(Tile.Type.KILL);
        grid[6][5].setType(Tile.Type.FIXED);
        grid[6][6].setType(Tile.Type.FIXED);
        grid[5][6].setType(Tile.Type.FIXED);
        grid[2][6].setType(Tile.Type.FIXED);
        grid[3][6].setType(Tile.Type.FINISH);

        recalcBall();
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
        //rotation = rotation > 360 || rotation < - 180 ? rotation - 360 : rotation;
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
                    recalcBall();
                }
            } else {
                rotation += ROT_VEL * delta;
                if(rotation > targetRotation) {
                    rotation = targetRotation;
                    rotating = false;
                    recalcBall();
                }
            }
        }
    }

    public void recalcBall() {
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

    public boolean isSolid(Tile t) {
        switch(t.getType()) {
            case EMPTY:
                return false;
            case FIXED:
                return true;
            case BLUE:
                return t.isActive() ? true : false;
            case RED:
                return t.isActive() ? true : false;
            case KILL:
                return false;
            default:
                return true;
        }
    }

}

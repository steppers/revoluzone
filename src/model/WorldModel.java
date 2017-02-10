package model;

/**
 * Created by steppers on 2/10/17.
 */
public class WorldModel {

    public static final int GRID_SIZE = 10;
    public static final float ROT_VEL = 150f;

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
                }
            } else {
                rotation += ROT_VEL * delta;
                if(rotation > targetRotation) {
                    rotation = targetRotation;
                    rotating = false;
                }
            }
        }
    }

    public boolean isRotating() {
        return rotating;
    }

}

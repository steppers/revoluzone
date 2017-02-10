package model;

/**
 * Created by steppers on 2/10/17.
 */
public class WorldState {

    public static final int GRID_SIZE = 10;
    private Tile[][] grid = new Tile[GRID_SIZE][GRID_SIZE];

    public WorldState() {
        for(int y = 1; y < GRID_SIZE-1; y++) {
            for(int x = 1; x < GRID_SIZE-1; x++) {
                grid[x][y] = new Tile();
                grid[x][y].setType(Tile.EMPTY);
            }
        }
    }

    public void setGrid(Tile[][] grid) {
        this.grid = grid;
    }

    public Tile[][] getGrid() {
        return grid;
    }

}

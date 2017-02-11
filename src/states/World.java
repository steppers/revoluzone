package states;

import model.Tile;
import model.WorldModel;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import Listeners.ScoreListener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by an6g15 on 10/02/2017.
 */
public class World extends BasicGameState {

    private Integer stateId;
    private Integer levelId;
    private Integer highScore;
    private boolean isFinished;
    private static ScoreListener listener;
    private static final int SCALE = 35;

    private WorldModel state = new WorldModel();

    public World(Integer stateId){
        this.stateId = stateId;
    }

    public static void setListener(ScoreListener listenerPassed){
        listener = listenerPassed;
    }

    @Override
    public int getID() {
        return this.stateId;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        Tile[][] grid = state.getGrid();
        Tile t;
        float offset = - (WorldModel.GRID_SIZE / 2) + 0.5f;

        Rectangle rect = new Rectangle(-SCALE/2, -SCALE/2, SCALE, SCALE);
        Shape tile = rect.transform(Transform.createRotateTransform((float)(state.getRotation()*Math.PI)/180));

        Vector2f screenOffset = new Vector2f(gameContainer.getWidth()/2, gameContainer.getHeight()/2);

        for(int x = 0; x < WorldModel.GRID_SIZE; x++) {
            for(int y = 0; y < WorldModel.GRID_SIZE; y++) {
                t = grid[x][y];
                Vector2f pos = new Vector2f(offset + x, offset + y);
                pos.sub(-state.getRotation());
                pos.scale(SCALE);
                pos.add(screenOffset);
                tile.setLocation(pos.x, pos.y);

                switch (t.getType()) {
                    case 0:
                        graphics.setColor(Color.white);
                        graphics.fill(tile);
                        break;
                    case 1:
                        graphics.setColor(Color.darkGray);
                        graphics.fill(tile);
                        break;
                    case 2:
                        graphics.setColor(Color.red);
                        graphics.fill(tile);
                        break;
                    case 3:
                        graphics.setColor(Color.blue);
                        graphics.fill(tile);
                        break;
                }
            }
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        if(isFinished){
            listener.levelComplete(levelId, highScore);
        }
        float delta = (float) i / 1000;
        if(!state.isRotating()) {
            if (gameContainer.getInput().isKeyDown(Input.KEY_RIGHT)) {
                state.rotate(90);
            } else {
                if (gameContainer.getInput().isKeyDown(Input.KEY_LEFT)) {
                    state.rotate(-90);
                }
            }
        }
        state.update(delta);
    }
}

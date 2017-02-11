package states;

import model.Tile;
import model.WorldModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
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
    private static final int SCALE = 35;

    private WorldModel state = new WorldModel();

    public World(Integer stateId){
        this.stateId = stateId;
    }


    @Override
    public int getID() {
        return this.stateId;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
    }

    @Override
    public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        float SCALE = Math.min(gc.getHeight(), gc.getWidth()) / 15;

        Tile[][] grid = state.getGrid();
        Tile t;
        float offset = - (WorldModel.GRID_SIZE / 2) + 0.5f;

        Rectangle rect = new Rectangle(-SCALE/2, -SCALE/2, SCALE, SCALE);
        Shape tile = rect.transform(Transform.createRotateTransform((float)(state.getRotation()*Math.PI)/180));

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);

        //First render pass (Floor)
        graphics.setColor(Color.white.darker(0.2f)); //Floor color
        for(int x = 0; x < WorldModel.GRID_SIZE; x++) {
            for (int y = 0; y < WorldModel.GRID_SIZE; y++) {
                Vector2f pos = new Vector2f(offset + x, offset + y);
                pos.sub(-state.getRotation());
                pos.scale(SCALE);
                pos.add(screenOffset);
                tile.setLocation(pos.x, pos.y);
                graphics.fill(tile);
            }
        }

        //Second render pass (Shadows)
        {
            float shOffset = offset + 0.06f;
            graphics.setColor(Color.white.darker(0.8f)); //Shadow color
            for (int x = 0; x < WorldModel.GRID_SIZE; x++) {
                for (int y = 0; y < WorldModel.GRID_SIZE; y++) {
                    if (state.isSolid(grid[x][y])) {
                        Vector2f pos = new Vector2f(shOffset + x, shOffset + y);
                        pos.sub(-state.getRotation());
                        pos.scale(SCALE);
                        pos.add(screenOffset);
                        tile.setLocation(pos.x, pos.y);
                        graphics.fill(tile);
                    }
                }
            }

            //Calc ball pos
            shOffset = offset + 0.06f;
            Vector2f pos = state.getBall().getPos().add(new Vector2f(shOffset, shOffset));
            pos.sub(-state.getRotation());
            pos.scale(SCALE);
            pos.add(screenOffset);

            Circle c = new Circle(0, 0, SCALE / 2);
            Shape circ = c.transform(Transform.createRotateTransform((float) (state.getRotation() * Math.PI) / 180));
            circ.setLocation(pos.x, pos.y);
            graphics.fill(circ);
        }

        //Third render pass (blocks)
        for(int x = 0; x < WorldModel.GRID_SIZE; x++) {
            for(int y = 0; y < WorldModel.GRID_SIZE; y++) {
                t = grid[x][y];
                Vector2f pos = new Vector2f(offset + x, offset + y);
                pos.sub(-state.getRotation());
                pos.scale(SCALE);
                pos.add(screenOffset);
                tile.setLocation(pos.x, pos.y);

                switch (t.getType()) {
                    case EMPTY:
                        break;
                    case FIXED:
                        graphics.setColor(Color.white.darker(0.4f));
                        graphics.fill(tile);
                        break;
                    case RED:
                        graphics.setColor(Color.red);
                        if(!t.isActive())
                            graphics.setColor(graphics.getColor().multiply(new Color(1, 1, 1, 0.3f)));
                        graphics.fill(tile);
                        break;
                    case BLUE:
                        graphics.setColor(Color.blue);
                        if(!t.isActive())
                            graphics.setColor(graphics.getColor().multiply(new Color(1, 1, 1, 0.3f)));
                        graphics.fill(tile);
                        break;
                }
            }
        }

        Vector2f pos = state.getBall().getPos().add(new Vector2f(offset, offset));
        pos.sub(-state.getRotation());
        pos.scale(SCALE);
        pos.add(screenOffset);

        Circle c = new Circle(0, 0, SCALE/2);
        Shape circ = c.transform(Transform.createRotateTransform((float)(state.getRotation()*Math.PI)/180));
        circ.setLocation(pos.x, pos.y);
        graphics.setColor(Color.cyan);
        graphics.fill(circ);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame stateBasedGame, int i) throws SlickException {
        float delta = (float) i / 1000;
        if(!state.isRotating()) {
            if(!state.getBall().isMoving()) {
                if(gc.getInput().isMousePressed(0)) {
                    Tile t = getTileFromMousePos(gc);
                    if(t != null) {
                        t.setActive(!t.isActive());
                        state.recalcBall();
                    }
                } else if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
                    state.rotate(90);
                } else {
                    if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
                        state.rotate(-90);
                    }
                }
            } else {
                state.getBall().update(delta);
            }
        }
        state.update(delta);

        if((Display.getWidth() != gc.getWidth() || Display.getHeight() != gc.getHeight()) && !gc.getInput().isMouseButtonDown(0)) {
            GL11.glViewport(0,0,Display.getWidth(), Display.getHeight());
        }
    }

    private Tile getTileFromMousePos(GameContainer gc) {
        float SCALE = Math.min(gc.getHeight(), gc.getWidth()) / 15;

        Tile[][] grid = state.getGrid();
        float offset = - (WorldModel.GRID_SIZE / 2);

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);

        Vector2f mouse = new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY());
        mouse = mouse.sub(screenOffset);
        mouse = mouse.scale(1/SCALE);
        mouse = mouse.sub(state.getRotation());
        mouse = mouse.sub(new Vector2f(offset, offset));

        int x, y;
        x = (int)mouse.x;
        y = (int)mouse.y;

        if(x >= 0 && x < WorldModel.GRID_SIZE && y >= 0 && y < WorldModel.GRID_SIZE) {
            return grid[x][y];
        }
        return null;
    }
}

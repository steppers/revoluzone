package states;

import graphics.FontLoader;
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

    enum States{
        MENU,
        LEVEL_SELECT,
        SETTINGS,
        CREDITS,
        PLAY
    }

    private Integer stateId;
    private Integer levelId;
    private Integer highScore;

    private States current;
    private Input currentInput;

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
        state.recalcBall();
        current = States.MENU;
        this.currentInput = gameContainer.getInput();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        renderWorld(gc, graphics, state);

        ascertainCurrentState();
        graphics.setFont(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString()));
        switch (current){
            case MENU:
                graphics.rotate(gc.getWidth() / 2, gc.getHeight() / 2, state.getRotation());
                graphics.drawString("Level Select", ( gc.getWidth() / 2) - 150, 150);
                graphics.rotate(gc.getWidth() / 2, gc.getHeight() / 2, 90);
                graphics.drawString("Settings", ( gc.getWidth() / 2) - 120, 150);
                graphics.rotate(gc.getWidth() / 2, gc.getHeight() / 2, 180);
                graphics.drawString("Quit", ( gc.getWidth() / 2) - 60, 150);
                break;
            case LEVEL_SELECT:

                break;
        }


    }

    @Override
    public void update(GameContainer gc, StateBasedGame stateBasedGame, int i) throws SlickException {
        float delta = (float) i / 1000;
        if(!state.isRotating()) {
            if(!state.getBall().isMoving()) {
//                if(gc.getInput().isMousePressed(0)) {
//                    Tile t = getTileFromMousePos(gc);
//                    if(t != null) {
//                        t.setActive(!t.isActive());
//                        state.recalcBall();
//                    }
//                }
                Tile[][] grid = state.getGrid();
                if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) { //TOGGLE ALL BLOCKS FOR NOW!!!
                    for(int x = 0; x < WorldModel.GRID_SIZE; x++) {
                        for (int y = 0; y < WorldModel.GRID_SIZE; y++) {
                            grid[x][y].setActive(!grid[x][y].isActive());
                        }
                    }
                    state.recalcBall();
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

        if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            System.exit(0);
        }
    }

//    private Tile getTileFromMousePos(GameContainer gc) {
//        float SCALE = (Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / state.GRID_SIZE;
//
//        Tile[][] grid = state.getGrid();
//        float offset = - (WorldModel.GRID_SIZE / 2);
//
//        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);
//
//        Vector2f mouse = new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY());
//        mouse = mouse.sub(screenOffset);
//        mouse = mouse.scale(1/SCALE);
//        mouse = mouse.sub(state.getRotation());
//        mouse = mouse.sub(new Vector2f(offset, offset));
//
//        int x, y;
//        x = (int)mouse.x;
//        y = (int)mouse.y;
//
//        if(x >= 0 && x < WorldModel.GRID_SIZE && y >= 0 && y < WorldModel.GRID_SIZE) {
//            return grid[x][y];
//        }
//        return null;
//    }

     void renderWorld(GameContainer gc, Graphics g, WorldModel state) {
        float SCALE = (Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / state.GRID_SIZE;

        Tile[][] grid = state.getGrid();
        Tile t;
        float offset = - (WorldModel.GRID_SIZE / 2) + 0.5f;

        Rectangle rect = new Rectangle(-SCALE/2, -SCALE/2, SCALE, SCALE);
        Shape tile = rect.transform(Transform.createRotateTransform((float)(state.getRotation()*Math.PI)/180));

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);

        //First render pass (Floor)
        g.setColor(Color.white.darker(0.2f)); //Floor color
        for(int x = 0; x < WorldModel.GRID_SIZE; x++) {
            for (int y = 0; y < WorldModel.GRID_SIZE; y++) {
                Vector2f pos = new Vector2f(offset + x, offset + y);
                pos.sub(-state.getRotation());
                pos.scale(SCALE);
                pos.add(screenOffset);
                tile.setLocation(pos.x, pos.y);
                g.fill(tile);
            }
        }

        //Second render pass (Shadows)
        {
            Vector2f shadow = new Vector2f(0.07f, 0.07f).sub(state.getRotation() + 25).add(new Vector2f(offset, offset));
            g.setColor(Color.white.darker(0.8f)); //Shadow color
            for (int x = 0; x < WorldModel.GRID_SIZE; x++) {
                for (int y = 0; y < WorldModel.GRID_SIZE; y++) {
                    if (state.isSolid(grid[x][y])) {
                        Vector2f pos = new Vector2f(shadow.x + x, shadow.y + y);
                        pos.sub(-state.getRotation());
                        pos.scale(SCALE);
                        pos.add(screenOffset);
                        tile.setLocation(pos.x, pos.y);
                        g.fill(tile);
                    }
                }
            }

            //Calc ball pos
            Vector2f pos = state.getBall().getPos().add(shadow);
            pos.sub(-state.getRotation());
            pos.scale(SCALE);
            pos.add(screenOffset);

            Circle c = new Circle(0, 0, SCALE / 2);
            Shape circ = c.transform(Transform.createRotateTransform((float) (state.getRotation() * Math.PI) / 180));
            circ.setLocation(pos.x, pos.y);
            g.fill(circ);
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
                        g.setColor(Color.white.darker(0.4f));
                        g.fill(tile);
                        break;
                    case RED:
                        g.setColor(Color.red);
                        if(!t.isActive())
                            g.setColor(g.getColor().multiply(new Color(1, 1, 1, 0.3f)));
                        g.fill(tile);
                        break;
                    case BLUE:
                        g.setColor(Color.blue);
                        if(!t.isActive())
                            g.setColor(g.getColor().multiply(new Color(1, 1, 1, 0.3f)));
                        g.fill(tile);
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
        g.setColor(Color.cyan);
        g.fill(circ);
    }

    private void ascertainCurrentState(){
//        States[] states = {
//          States.CREDITS, States.LEVEL_SELECT, States.
//        };
        switch ((int) state.getRotation() % 360){
            case 0:
                if(currentInput.isKeyPressed(Input.KEY_ENTER)){
                    current = States.LEVEL_SELECT;
                }
                break;
            case 90:
                if(currentInput.isKeyPressed(Input.KEY_ENTER)){
                    System.exit(0);
                }
                break;
            case 180:
                if(currentInput.isKeyPressed(Input.KEY_ENTER)){
                    System.exit(0);
                }
                break;
            case 270:
                if(currentInput.isKeyPressed(Input.KEY_ENTER)){
                    current = States.SETTINGS;
                }
                break;
        }
    }
}

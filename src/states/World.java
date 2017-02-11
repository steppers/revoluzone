package states;

import graphics.FontLoader;
import model.Tile;
import model.WorldModel;
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
        PLAY,
        TRANSITION_IN,
        TRANSITION_OUT
    }

    private Integer stateId;
    private Integer levelId;
    private Integer highScore;

    private States currentState;
    private States nextState;
    private Input currentInput;

    private float scale = 0.5f;
    private float transitionRate = 1f;
    private float transitionScaleTarget = 1f;
    private float textOpacity = 1f;

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
        currentState = States.MENU;
        this.currentInput = gameContainer.getInput();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        ascertainCurrentState();
        graphics.setFont(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString()));
        renderWorld(gc, graphics, state, scale);

        graphics.setColor(new Color(1,1,1,textOpacity));
        switch (currentState){
            case MENU:
                renderMenuText(gc, graphics);
                break;
            case TRANSITION_IN:
            case TRANSITION_OUT:
                renderMenuText(gc, graphics);
                break;
            default:
                break;
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame stateBasedGame, int i) throws SlickException {
        float delta = (float) i / 1000;

        switch (currentState){
            case TRANSITION_IN:
                scale += transitionRate * delta;
                textOpacity -= transitionRate * delta;
                if(scale >= transitionScaleTarget) {
                    currentState = nextState;
                    textOpacity = 1;
                    scale = transitionScaleTarget;
                }
                break;
            case TRANSITION_OUT:
                scale -= transitionRate * delta;
                textOpacity += transitionRate * delta;
                if(scale < transitionScaleTarget) {
                    currentState = nextState;
                    textOpacity = 1;
                    scale = transitionScaleTarget;
                }
                break;
            default:
                if(!state.isRotating()) {
                    if(!state.getBall().isMoving()) {
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
                break;
        }
    }

    void renderMenuText(GameContainer gc, Graphics graphics) {
        graphics.rotate(gc.getWidth() / 2, gc.getHeight() / 2, state.getRotation());
        graphics.drawString("Level Select", ( gc.getWidth() / 2) - 135, 150*(1/scale));
        graphics.rotate(gc.getWidth() / 2, gc.getHeight() / 2, 90);
        graphics.drawString("Settings", ( gc.getWidth() / 2) - 100, 150*(1/scale));
        graphics.rotate(gc.getWidth() / 2, gc.getHeight() / 2, 180);
        graphics.drawString("Quit", ( gc.getWidth() / 2) - 45, 150*(1/scale));
    }

    void renderWorld(GameContainer gc, Graphics g, WorldModel state, float scale) {
        float SCALE = ((Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / state.GRID_SIZE) * scale;

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
                    case KILL:
                        g.setColor(Color.yellow);
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

    private void ascertainCurrentState() {
//        States[] states = {
//          States.CREDITS, States.LEVEL_SELECT, States.
//        };
        if (currentInput.isKeyPressed(Input.KEY_ENTER)) {
            switch (currentState) {
                case MENU:
                    switch ((int) state.getRotation() % 360) {
                        case 0:
                            currentState = States.TRANSITION_IN;
                            nextState = States.LEVEL_SELECT;
                            transitionScaleTarget = 1;
                            break;
                        case 90:
                            System.exit(0);
                            break;
                        case 180:
                            //currentState = States.EDITOR;
                            break;
                        case 270:
//                            currentState = States.SETTINGS;
                            break;
                    }
                    break;
                default:
                    break;
            }
        }

        if(currentInput.isKeyPressed(Input.KEY_ESCAPE)) {
            switch (currentState) {
                case LEVEL_SELECT:
                    currentState = States.TRANSITION_OUT;
                    nextState = States.MENU;
                    transitionScaleTarget = 0.5f;
                    break;
                default:
                    break;
            }
        }
    }
}

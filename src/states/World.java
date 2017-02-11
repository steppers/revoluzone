package states;

import graphics.FontLoader;
import graphics.StateRenderer;
import model.Tile;
import model.WorldModel;
import org.newdawn.slick.*;
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

    private StateRenderer renderer;

    private Integer stateId;
    private Integer levelId;
    private Integer highScore;

    private States currentState;
    private States nextState;
    private Input currentInput;

    private WorldModel state = new WorldModel();

    public World(Integer stateId){
        this.stateId = stateId;
        renderer = new StateRenderer(state);
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
        graphics.setFont(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString()));
        renderer.render(gc, graphics);

        switch (currentState){
            case MENU:
                renderMenuText(gc, graphics, 1, 0.5f);
                break;
            case TRANSITION_IN:
            case TRANSITION_OUT:
                renderMenuText(gc, graphics, 1-renderer.getScale(), renderer.getScale());
                break;
            default:
                break;
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame stateBasedGame, int i) throws SlickException {
        float delta = (float) i / 1000;
        renderer.update(delta);

        switch (currentState){
            case TRANSITION_IN:
            case TRANSITION_OUT:
                if(!renderer.isTransitioning())
                    currentState = nextState;
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
        updateCurrentState();
    }

    private void updateCurrentState() {
        if (currentInput.isKeyPressed(Input.KEY_ENTER)) {
            if (!renderer.isTransitioning()) {
                switch (currentState) {
                    case MENU:
                        switch ((int) state.getRotation() % 360) {
                            case 0:
                                renderer.transition(StateRenderer.TransitionType.GROW, state, 1f, 1f);
                                currentState = States.TRANSITION_IN;
                                nextState = States.LEVEL_SELECT;
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
        }

        if (currentInput.isKeyPressed(Input.KEY_ESCAPE)) {
            if (!renderer.isTransitioning()) {
                switch (currentState) {
                    case LEVEL_SELECT:
                        renderer.transition(StateRenderer.TransitionType.SHRINK, state, 0.5f, 1f);
                        currentState = States.TRANSITION_OUT;
                        nextState = States.MENU;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    void renderMenuText(GameContainer gc, Graphics graphics, float opacity, float scale) {
        graphics.setColor(new Color(1,1,1,opacity));
        graphics.rotate(gc.getWidth() / 2, gc.getHeight() / 2, state.getRotation());
        graphics.drawString("Level Select", ( gc.getWidth() / 2) - 135, 150*(1/scale));
        graphics.rotate(gc.getWidth() / 2, gc.getHeight() / 2, 90);
        graphics.drawString("Settings", ( gc.getWidth() / 2) - 100, 150*(1/scale));
        graphics.rotate(gc.getWidth() / 2, gc.getHeight() / 2, 180);
        graphics.drawString("Quit", ( gc.getWidth() / 2) - 45, 150*(1/scale));
    }
}

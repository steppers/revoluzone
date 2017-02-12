package states;

import editor.Editor;
import filemanage.Parser;
import graphics.FontLoader;
import graphics.StateRenderer;
import model.Tile;
import model.WorldModel;
import org.newdawn.slick.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import static states.World.States.LEVEL_SELECT;

/**
 * Created by an6g15 on 10/02/2017.
 */
public class World extends BasicGameState {

    enum States{
        MENU,
        LEVEL_SELECT,
        SETTINGS,
        CREDITS,
        LEVEL,
        TRANSITION,
        EDITOR
    }

    private StateRenderer renderer;

    private Integer stateId;
    private Integer levelId;
    private Integer highScore;

    private States currentState;
    private States nextState;
    private Input currentInput;
    private Parser parser;
    private Editor editor;

    private WorldModel state = new WorldModel();
    private WorldModel menuState = new WorldModel();

    public World(Integer stateId) {
        this.stateId = stateId;
        renderer = new StateRenderer(state);
        parser = new Parser();
        editor = new Editor(renderer);
    }


    @Override
    public int getID() {
        return this.stateId;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        currentState = States.MENU;
        this.currentInput = gameContainer.getInput();
        redefinePosition(gameContainer);
        menuState = parser.getWorldFromFile("res/config/0.txt");
        state = menuState;
        renderer.setState(state);
        state.recalcBall();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        renderBackground(gc, graphics);
        graphics.setFont(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString()));
        renderer.render(gc, graphics);

        switch (currentState){
            case MENU:
                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "Level Select", 0, -135, 200);
                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "Settings", 90, -100, 200);
                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "Quit", -90, -45, 200);
                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "Editor", 180, -60, 200);

                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "Use arrow", 0, -110, 70);
                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "keys to turn", 0, -135, 50);
                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "Use space to", 0, -145, 10);
                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "toggle", 0, -70, -10);
                break;
            case EDITOR:
                editor.render(gc, stateBasedGame, graphics);
                break;
            case TRANSITION:
//                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "Level Select", 0, -135, 200);
//                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "Settings", 90, -100, 200);
//                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "Quit", -90, -45, 200);
//                renderText(gc, graphics, renderer.getOpacity(), renderer.getScale(), "Editor", 180, -60, 200);
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
            case TRANSITION:
                if(!renderer.isTransitioning()) {
                    currentState = nextState;
                    state = renderer.getNextState();
                }
                break;
            case EDITOR:
                state = editor.getState();
                editor.update(gc, delta);
            default:
                if(!state.isRotating()) {
                    if(!state.getBall().isMoving()) {
                        Tile[][] grid = state.getGrid();
                        Vector2f p = state.getBall().getPos();
                        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) { //TOGGLE ALL BLOCKS FOR NOW!!!
                            Tile.Type type = grid[(int)p.x][(int)p.y].getType();
                            if(type == Tile.Type.RED || type == Tile.Type.BLUE) {
                            } else {
                                for (int x = 0; x < state.getGridSize(); x++) {
                                    for (int y = 0; y < state.getGridSize(); y++) {
                                        grid[x][y].setActive(!grid[x][y].isActive());
                                    }
                                }
                            }
                            state.recalcBall();
                        } else if (gc.getInput().isKeyDown(Input.KEY_RIGHT) && (currentState != LEVEL_SELECT)) {
                            state.rotate(90);
                        } else {
                            if (gc.getInput().isKeyDown(Input.KEY_LEFT)&& (currentState != LEVEL_SELECT)) {
                                state.rotate(-90);
                            }
                        }

                        if (grid[(int)(p.x)][(int)(p.y)].getType() == Tile.Type.SWITCH){
                            grid[(int)(p.x)][(int)(p.y)].setActive(true);
                        }
                    } else {
                        state.getBall().update(delta);
                    }
                }
                state.update(delta);
                break;
        }
        updateCurrentState();

        backgroundOpacity += 3f * delta;
    }

    private void updateCurrentState() {
        if (currentInput.isKeyPressed(Input.KEY_ENTER)) {
            if (!renderer.isTransitioning()) {
                switch (currentState) {
                    case MENU:
                        int r = (int)state.getRotation();
                        while(r < 0)
                            r += 360;
                        switch (r % 360) {
                            case 0:
                                renderer.transitionFade(parser.getWorldFromFile("res/config/Level 2.txt"), 1f);
                                currentState = States.TRANSITION;
                                nextState = LEVEL_SELECT;
                                break;
                            case 90:
                                System.exit(0);
                                break;
                            case 180:
                                renderer.transitionGrow(editor.getState(), 1f);
                                currentState = States.TRANSITION;
                                nextState = States.EDITOR;
                                break;
                            case 270:
//                            currentState = States.SETTINGS;
                                break;
                        }
                        break;
                    case LEVEL_SELECT:
                        renderer.transitionGrow(state, 1f);
                        currentState = States.TRANSITION;
                        nextState = States.LEVEL;
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
                        renderer.transitionFade(menuState, 1f);
                        currentState = States.TRANSITION;
                        nextState = States.MENU;
                        break;
                    case EDITOR:
                        renderer.transitionShrink(menuState, 1f);
                        currentState = States.TRANSITION;
                        nextState = States.MENU;
                        break;
                    case LEVEL:
                        renderer.transitionShrink(state, 1f);
                        currentState = States.TRANSITION;
                        nextState = LEVEL_SELECT;
                        break;
                    default:
                        break;
                }
            }
        }

        if (currentInput.isKeyPressed(Input.KEY_RIGHT)) {
            if (!renderer.isTransitioning()) {
                switch (currentState) {
                    case LEVEL_SELECT:
                        renderer.transitionFade(parser.getWorldFromFile("res/config/"+parser.nextLevel), 1f);
                        currentState = States.TRANSITION;
                        nextState = States.LEVEL_SELECT;
                        break;
                    default:
                        break;
                }
            }
        }
        if (currentInput.isKeyPressed(Input.KEY_LEFT)) {
            if (!renderer.isTransitioning()) {
                switch (currentState) {
                    case LEVEL_SELECT:
                        renderer.transitionFade(parser.getWorldFromFile("res/config/"+parser.prevLevel), 1f);
                        currentState = States.TRANSITION;
                        nextState = States.LEVEL_SELECT;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    void renderText(GameContainer gc, Graphics g, float opacity, float scale, String text, float rotation, float xOffset, float yOffset) {
        g.setColor(new Color(1,1,1,opacity));
        g.resetTransform();
        g.rotate(gc.getWidth() / 2, gc.getHeight() / 2, state.getRotation());
        g.rotate(gc.getWidth() / 2, gc.getHeight() / 2, rotation);
        g.drawString(text, ( gc.getWidth() / 2) + xOffset, (gc.getHeight()/2) - yOffset*(1/scale));
    }


    float backgroundOpacity = 0;

    float x, y, side;

    void redefinePosition(GameContainer gc) {
        x = (float) Math.random() * gc.getWidth();
        y = (float) Math.random() * gc.getHeight();
        side = (float) (gc.getWidth() * (Math.random()+0.5f) * 0.15);
    }

    private void renderBackground(GameContainer gc, Graphics graphics){
        graphics.setBackground(Color.lightGray);
        graphics.clear();
        float op = (float)(Math.sin(backgroundOpacity)/2)+0.5f;
        graphics.setLineWidth(3);
        graphics.setColor(new Color(1,1,1,0.35f*op));
        graphics.drawRect(x, y, side, side);
        if ((((Math.sin(backgroundOpacity-0.1f)/2)+0.5f) > op &&
                op < 0.1))  {
            redefinePosition(gc);
        }
    }
}

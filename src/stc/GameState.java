package stc;

import stc.graphics.FontLoader;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import stc.UI.TextRenderer;
import stc.states.*;

/**
 * Created by steppers on 2/12/17.
 */
public class GameState extends BasicGameState {

    public enum State {
        MENU,
        LEVEL_SELECT,
        CREDITS,
        LEVEL,
        TRANSITION,
        EDITOR,
        QUIT
    }
    public Model m;
    public TransitionManager tm;
    public State currentState = State.MENU;
    public State previousState = State.MENU;
    public TextRenderer textRenderer;
    private Background background;

    //State stuff
    private Editor editor;
    private Menu menu;
    private Credits credits;
    private LevelSelect levelSelect;
    private PlayLevel playLevel;

    public GameState() {
        m = new Model("user_levels/test_save.txt", 0.6f);
        tm = new TransitionManager(this);
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        background = new Background(gc);
        textRenderer = new TextRenderer(gc);

        editor = new Editor(this, tm, gc);
        menu = new Menu(this, tm, gc);
        credits = new Credits(this, tm, gc);
        levelSelect = new LevelSelect(this, tm, gc);
        playLevel = new PlayLevel(this, tm, gc);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.setFont(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString()));

        background.render(gc, g);
        switch(currentState) {
            case MENU:
                menu.render(gc, g);
                break;
            case LEVEL_SELECT:
                levelSelect.render(gc, g);
                break;
            case LEVEL:
                playLevel.render(gc, g);
                break;
            case EDITOR:
                editor.render(gc, g);
                break;
            case TRANSITION:
                tm.render(gc, g);
                if(previousState == State.EDITOR || tm.getNewState() == State.EDITOR) {
                    editor.renderTransition(gc, g);
                }
                renderStateText(gc, g, previousState, m);
                renderStateText(gc, g, tm.getNewState(), tm.getNewModel());
                if(tm.getNewState() == State.QUIT) {
                    Color fill = Color.black;
                    fill.a =  1-(m.getScale()*1.6666f);
                    g.setColor(fill);
                    g.resetTransform();
                    g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
                }
                break;
            case CREDITS:
                credits.render(gc, g);
                break;
            case QUIT:
                gc.exit();
                break;
            default:
                break;
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        float delta = (float)i / 1000;
        m.update(delta);
        Tile t = m.getTileUnderBall();
        if(t.type == Tile.Type.KILL) {
            m.reset();
            m.setRotation(0);
        }
        if(t.type == Tile.Type.FINISH && previousState == State.LEVEL) {
            m.ball.halt();
            if(m.score < Integer.parseInt(m.getProperty("score"))) {
                m.setProperty("score", String.valueOf(m.score)); //Could be saved to file too
            }
            tm.transitionFadeRotate(m, new Model(m.getProperty("next"), 1.0f, 0f), State.LEVEL, 90, 0.3f);
        } else {
            if (!m.isWaiting()) {
                switch (currentState) {
                    case MENU:
                        menu.update(gc);
                        break;
                    case LEVEL_SELECT:
                        levelSelect.update(gc);
                        break;
                    case LEVEL:
                        playLevel.update(gc);
                        break;
                    case EDITOR:
                        editor.update(gc);
                        break;
                    case TRANSITION:
                        if (!tm.isTransitioning()) {
                            currentState = tm.getNewState();
                            m = tm.getNewModel();
                        } else {
                            tm.update(delta);
                        }
                        break;
                    case CREDITS:
                        credits.update(gc, delta);
                        break;
                    case QUIT:
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            } else {
                if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {}
                if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {}
                if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {}
                if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {}
            }
        }
        background.update(delta);
    }

    private void renderStateText(GameContainer gc, Graphics g, State state, Model m) {
        switch(state) {
            case MENU:
                menu.renderText(g, m);
                break;
            case LEVEL_SELECT:
                levelSelect.renderText(g, m);
                break;
            case LEVEL:
                playLevel.renderText(g, m);
                break;
            case EDITOR:
                editor.renderText(g, m);
                break;
            case TRANSITION:
                break;
            case CREDITS:
                credits.renderText(g, m);
                break;
            default:
                break;
        }
    }

    public Vector2f getMouseTilePos(GameContainer gc) {
        float SCALE = ((Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / m.gridSize) * m.getScale();

        float offset = - ((float)m.gridSize / 2);

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);

        Vector2f mouse = new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY());
        mouse = mouse.sub(screenOffset);
        mouse = mouse.scale(1/SCALE);
        mouse = mouse.sub(m.getRotation());
        mouse = mouse.sub(new Vector2f(offset, offset));

        int x, y;
        x = (int)mouse.x;
        y = (int)mouse.y;

        if(x >= 0 && x < m.gridSize && y >= 0 && y < m.gridSize) {
            return new Vector2f(x, y);
        }
        return null;
    }
}

package proto;

import graphics.FontLoader;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by steppers on 2/12/17.
 */
public class GameState extends BasicGameState {

    public enum State {
        MENU,
        LEVEL_SELECT,
        SETTINGS,
        CREDITS,
        LEVEL,
        TRANSITION,
        EDITOR
    }

    Model m;
    Model m2;
    TransitionManager tm;
    State currentState = State.MENU;
    State previousState = State.MENU;

    public GameState() {
        m = new Model("0.txt", 0.5f);
        tm = new TransitionManager(this);
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        redefinePosition(gc);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        g.setFont(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString()));

        renderBackground(gc, g);
        switch(currentState) {
            case MENU:
                m.render(gc, g);
                renderStateText(gc, g, currentState, m);
                break;
            case LEVEL_SELECT:
                m.render(gc, g);
                renderStateText(gc, g, State.LEVEL_SELECT, m);
                break;
            case LEVEL:
                m.render(gc, g);
                renderStateText(gc, g, State.LEVEL, m);
                break;
            case EDITOR:
                m.render(gc, g);
                renderStateText(gc, g, State.EDITOR, m);
                break;
            case TRANSITION:
                tm.render(gc, g);
                renderStateText(gc, g, previousState, m);
                renderStateText(gc, g, tm.getNewState(), tm.getNewModel());
                break;
            default:
                break;
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        float delta = (float)i / 1000;
        m.update(delta);
        if(!m.isWaitingForBall()) {
            switch (currentState) {
                case MENU:
                    updateMenu(gc);
                    break;
                case LEVEL_SELECT:
                    updateLevelSelect(gc);
                    break;
                case LEVEL:
                    updateLevel(gc);
                    break;
                case EDITOR:
                    updateEditor(gc);
                    break;
                case TRANSITION:
                    if (!tm.isTransitioning()) {
                        currentState = tm.getNewState();
                        m = tm.getNewModel();
                    } else {
                        tm.update(delta);
                    }
                    break;
                default:
                    break;
            }
        }

        backgroundOpacity += 3f * delta;
    }

    private void updateEditor(GameContainer gc) {
        if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            tm.transitionShrink(m, State.MENU, 0.5f, 0.3f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
            tm.transitionRotate(m, currentState, 90, 0.2f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_LEFT)) {
            tm.transitionRotate(m, currentState, -90, 0.2f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            m.toggleRedBlue();
        }
    }

    private void updateLevel(GameContainer gc) {
        if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            tm.transitionShrink(m, State.LEVEL_SELECT, 0.5f, 0.3f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
            m.score += 1;
            tm.transitionRotate(m, currentState, 90, 0.2f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_LEFT)) {
            m.score += 1;
            tm.transitionRotate(m, currentState, -90, 0.2f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            if(m.getTileUnderBall().type != Tile.Type.BLUE && m.getTileUnderBall().type != Tile.Type.RED)
                m.toggleRedBlue();
        }
        if(m.hasCompleted()) {
            if(m.score < Integer.parseInt(m.getProperty("score"))) {
                m.setProperty("score", String.valueOf(m.score)); //Could be saved to file too
            }
            tm.transitionFadeRotate(m, new Model(m.getProperty("next"), 1.0f, 0f), State.LEVEL, 90, 0.3f);
        }
        Tile t = m.getTileUnderBall();
        switch(t.type) {
            case SWITCH:
                for(Tile link : t.links) {
                    link.active = true;
                    if(link.type == Tile.Type.LOCKED_FINISH) {
                        link.type = Tile.Type.FINISH;
                    }
                }
                t.active = true;
                break;
            default:
                break;
        }
    }

    private void updateMenu(GameContainer gc) {
        if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
            int r = (int)m.getRotation();
            while(r < 0)
                r += 360;
            switch(r % 360) {
                case 0:
                    tm.transitionFade(m, new Model("Level 1.txt", 0.5f, 0f), State.LEVEL_SELECT, 0.6f);
                    break;
                case 90:
                    System.exit(-1);
                    break;
                case 180:
                    tm.transitionGrow(m, State.EDITOR, 1.0f, 0.3f);
                    break;
                case 270:
                    break;
            }
        }
        if(gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
            tm.transitionRotate(m, currentState, 90, 0.2f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_LEFT)) {
            tm.transitionRotate(m, currentState, -90, 0.2f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            m.toggleRedBlue();
        }
    }

    private void updateLevelSelect(GameContainer gc) {
        if(gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
            tm.transitionGrow(m, State.LEVEL, 1.0f, 0.3f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            tm.transitionFade(m, new Model("0.txt", 0.5f, 0f), State.MENU, 0.4f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
            tm.transitionFadeRotate(m, new Model(m.getProperty("next"), 0.5f, 0f), currentState, 90, 0.3f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_LEFT)) {
            tm.transitionFadeRotate(m, new Model(m.getProperty("prev"), 0.5f, 0f), currentState, -90, 0.3f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            m.toggleRedBlue();
        }
    }

    private void renderStateText(GameContainer gc, Graphics g, State state, Model m) {
        switch(state) {
            case MENU:
                renderText(gc, g, m.getOpacity(), m.getScale(), "Level Select", 0, -135, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Settings", 90, -100, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Quit", -90, -45, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Editor", 180, -60, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Use arrow", 0, -110, 0.16f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "keys to turn", 0, -135, 0.10f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Use space to", 0, -145, 0.0f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "toggle", 0, -70, -0.06f, m);
                break;
            case LEVEL_SELECT:
                renderText(gc, g, m.getOpacity(), m.getScale(), m.getProperty("name"), 0, -90, -0.55f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), m.getProperty("prev").split("\\.")[0], 90, -90, -0.55f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), m.getProperty("next").split("\\.")[0], -90, -90, -0.55f, m);
                break;
            case LEVEL:
                renderText(gc, g, m.getOpacity(), m.getScale(), "Best move count: " + m.getProperty("score"), 0, -225, 0.45f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Your move count: " + m.score, 0, -225, -0.40f, m);
            case TRANSITION:

                break;
            default:
                break;
        }
    }

    private void renderText(GameContainer gc, Graphics g, float opacity, float scale, String text, float rotation, float xOffset, float yOffset, Model m) {
        float SCALE = Math.min(gc.getHeight(), gc.getWidth());
        g.setColor(new Color(1,1,1,opacity));
        g.resetTransform();
        g.rotate(gc.getWidth() / 2, gc.getHeight() / 2, m.getRotation());
        g.rotate(gc.getWidth() / 2, gc.getHeight() / 2, rotation);
        g.drawString(text, ( gc.getWidth() / 2) + xOffset, (gc.getHeight()/2) - (yOffset*scale*SCALE));
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

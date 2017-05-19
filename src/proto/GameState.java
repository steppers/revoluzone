package proto;

import graphics.FontLoader;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

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
        EDITOR
    }
    Model m;
    Model m2;
    TransitionManager tm;
    State currentState = State.MENU;
    State previousState = State.MENU;

    ArrayList<BackgroundBox> bgBoxes;
    Editor editor;

    public GameState() {
        m = new Model("0.txt", 0.5f);
        tm = new TransitionManager(this);
        bgBoxes = new ArrayList<>();
        editor = new Editor(this, tm);
        editor.setModel(m);
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        for(int i = 0; i < 5; i++) {
            bgBoxes.add(new BackgroundBox(gc));
        }

        editor.init(gc);
        graphics.FontLoader.loadFont(gc);
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
                editor.render(gc, g);
                break;
            case TRANSITION:
                tm.render(gc, g);
                renderStateText(gc, g, previousState, m);
                renderStateText(gc, g, tm.getNewState(), tm.getNewModel());
                if(previousState == State.EDITOR && tm.getNewState() != State.MENU) {
                    editor.renderTransition(gc, g);
                }
                break;
            case CREDITS:
                m.render(gc, g);
                renderStateText(gc, g, State.CREDITS, m);
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
            Model n = new Model(m.getProperty("name") + ".txt", m.getScale(), m.getOpacity());
            m = n;
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
                        updateMenu(gc);
                        break;
                    case LEVEL_SELECT:
                        updateLevelSelect(gc);
                        break;
                    case LEVEL:
                        updateLevel(gc);
                        break;
                    case EDITOR:
                        editor.setModel(m);
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
                        updateCREDITS(gc);
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

        for(int j = 0; j < 5; j++) {
            bgBoxes.get(j).update(delta);
        }
    }

    private void updateLevel(GameContainer gc) {
        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            tm.transitionShrink(m, State.LEVEL_SELECT, 0.5f, 0.3f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            m.score += 1;
            tm.transitionRotate(m, currentState, 90, 0.2f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
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
        t.activate();
    }

    private void updateMenu(GameContainer gc) {
        if(gc.getInput().isKeyDown(Input.KEY_ENTER)) {
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
                    tm.transitionFade(m, new Model(m.getProperty("name")+".txt", 0.5f, 1f), State.CREDITS, 1f);
                    break;
            }
        }
        if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            tm.transitionRotate(m, currentState, 90, 0.2f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            tm.transitionRotate(m, currentState, -90, 0.2f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            m.toggleRedBlue();
        }
    }

    private void updateLevelSelect(GameContainer gc) {
        if(gc.getInput().isKeyDown(Input.KEY_ENTER)) {
            tm.transitionGrow(m, State.LEVEL, 1.0f, 0.3f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            tm.transitionFade(m, new Model("0.txt", 0.5f, 0f), State.MENU, 0.4f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            tm.transitionFadeRotate(m, new Model(m.getProperty("next"), 0.5f, 0f), currentState, 90, 0.3f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            tm.transitionFadeRotate(m, new Model(m.getProperty("prev"), 0.5f, 0f), currentState, -90, 0.3f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            m.toggleRedBlue();
        }
    }
    private void updateCREDITS(GameContainer gc){
        if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            tm.transitionRotate(m, currentState, 90, 0.2f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            tm.transitionRotate(m, currentState, -90, 0.2f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            m.toggleRedBlue();
        }
        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            tm.transitionFade(m, new Model("0.txt", 0.5f, 0f), State.MENU, 0.4f);
        }
    }

    private void renderStateText(GameContainer gc, Graphics g, State state, Model m) {
        switch(state) {
            case MENU:
//                renderText(gc, g, m.getOpacity(), m.getScale(), "Squaring the Circle", 0, -135, 0.8f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Level Select", 0, -graphics.FontLoader.getFontSize()*3, 0.6f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Credits", 90, -graphics.FontLoader.getFontSize()*2, 0.6f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Quit", -90, -graphics.FontLoader.getFontSize(), 0.6f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Editor", 180, -graphics.FontLoader.getFontSize()*(float)(5/4), 0.6f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Use arrow", 0, -graphics.FontLoader.getFontSize()*2.5f, 0.16f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "keys to turn", 0, -graphics.FontLoader.getFontSize()*3, 0.10f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Use space to", 0, -graphics.FontLoader.getFontSize()*3, 0.0f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "toggle", 0, -graphics.FontLoader.getFontSize()*2.5f, -0.06f*((float)gc.getHeight()/1440), m);
                break;
            case LEVEL_SELECT:
                renderText(gc, g, m.getOpacity(), m.getScale(), m.getProperty("name"), 0, -graphics.FontLoader.getFontSize()*2, 0.6f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), m.getProperty("prev").split("\\.")[0], 90, -graphics.FontLoader.getFontSize()*2, 0.6f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), m.getProperty("next").split("\\.")[0], -90, -graphics.FontLoader.getFontSize()*2, 0.6f*((float)gc.getHeight()/1440), m);
                break;
            case LEVEL:
                renderText(gc, g, m.getOpacity(), m.getScale(), "Best move count: " + m.getProperty("score"), 0, -graphics.FontLoader.getFontSize()*4.5f, 0.48f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Your move count: " + m.score, 0, -graphics.FontLoader.getFontSize()*4.5f, -0.43f*((float)gc.getHeight()/1440), m);
            case TRANSITION:

                break;
            case CREDITS:
                renderText(gc, g, m.getOpacity(), m.getScale(), "Ollie Steptoe", 0, -graphics.FontLoader.getFontSize()*3, 0.6f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Alistair Brewin", 90, -graphics.FontLoader.getFontSize()*3, 0.6f*((float)gc.getHeight()/1440), m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Anton Nikitin", -90, -graphics.FontLoader.getFontSize()*3, 0.6f*((float)gc.getHeight()/1440), m);
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

    private class BackgroundBox {
        float opacity = 0;
        float x, y, side;

        public BackgroundBox(GameContainer gc) {
            redefinePosition(gc);
            opacity = (float)Math.random();
        }

        void update(float delta) {
            opacity += 3f * delta;
        }

        void redefinePosition(GameContainer gc) {
            x = (float) Math.random() * gc.getWidth();
            y = (float) Math.random() * gc.getHeight();
            side = (float) (gc.getWidth() * (Math.random()+0.5f) * 0.15);
        }
    }

    private void renderBackground(GameContainer gc, Graphics graphics){
        graphics.setBackground(Color.lightGray);
        graphics.clear();

        for(BackgroundBox bb : bgBoxes) {
            float op = (float)(Math.sin(bb.opacity)/2)+0.5f;
            graphics.setLineWidth(3);
            graphics.setColor(new Color(1,1,1,0.35f*op));
            graphics.drawRect(bb.x, bb.y, bb.side, bb.side);
            if ((((Math.sin(bb.opacity-0.1f)/2)+0.5f) > op &&
                    op < 0.1))  {
                bb.redefinePosition(gc);
            }
        }
    }

    public Vector2f getMouseTilePos(GameContainer gc) {
        float SCALE = ((Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / m.gridSize) * m.getScale();

        float offset = - (m.gridSize / 2);

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

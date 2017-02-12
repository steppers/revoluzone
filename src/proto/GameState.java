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
    public Tile.Type activeTile = Tile.Type.EMPTY;
    Model m;
    Model m2;
    TransitionManager tm;
    State currentState = State.MENU;
    State previousState = State.MENU;

    Tile.Type editorType = Tile.Type.FIXED;
    ArrayList<BackgroundBox> bgBoxes;
    ArrayList<Rectangle> toolbar;
    ArrayList<Line> links;
    private boolean linking = false;
    private float linkX, linkY;

    public GameState() {
        m = new Model("0.txt", 0.5f);
        tm = new TransitionManager(this);
        bgBoxes = new ArrayList<>();
        toolbar = new ArrayList<>();
        links = new ArrayList<>();
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

        float size = (0.75f * gc.getHeight()) / Tile.Type.values().length;
        for (Tile.Type t : Tile.Type.values()) {
            Rectangle r = new Rectangle(10, (gc.getHeight()*0.125f) + size*t.ordinal(), size, size);
            toolbar.add(r);
        }
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
                renderToolbar(gc, g);
                g.setColor(Color.green);
                g.setLineWidth(3);
                Vector2f ms = getMouseTilePos(gc);
                if(ms != null && linking)
//                    g.draw(new Line(m.getWorldCoordOfTile(new Vector2f(linkX, linkY), gc), new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY())));
                for(Line l : links) {
                    g.draw(new Line(new Vector2f(l.getX1(), l.getY1()), m.getWorldCoordOfTile(new Vector2f(l.getX2(), l.getY2()), gc)));
                }
                g.setLineWidth(1);
                break;
            case TRANSITION:
                tm.render(gc, g);
                renderStateText(gc, g, previousState, m);
                renderStateText(gc, g, tm.getNewState(), tm.getNewModel());
                if(previousState == State.EDITOR && tm.getNewState() != State.MENU)
                    renderToolbar(gc, g);
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
            if (!m.isWaitingForBall()) {
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
                        updateEditor(gc, m);
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
            }
        }

        for(int j = 0; j < 5; j++) {
            bgBoxes.get(j).update(delta);
        }
    }

    private void updateEditor(GameContainer gc, Model m) {
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
        if(gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            Tile t = m.getTileFromMousePos(gc);
            if (t != null) {
                t.type = editorType;
                m.initRedBlue();
            }
            for (Rectangle r : toolbar) {
                if (r.contains(gc.getInput().getMouseX(),gc.getInput().getMouseY())) {
                    editorType = Tile.Type.values()[toolbar.indexOf(r)];
                }

            }

        }

        if(gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
            Vector2f p = getMouseTilePos(gc);
            if(!linking) {
                if (p != null) {
                    linking = true;
                    linkX = p.x;
                    linkY = p.y;
                }
            }
        } else {
            Vector2f p = getMouseTilePos(gc);
//            if(linking) {
                if (p != null) {
                    linking = false;
                    links.add(new Line(m.getWorldCoordOfTile(new Vector2f(linkX, linkY), gc), new Vector2f((int)p.x, (int)p.y)));
                    m.tiles[(int)linkX][(int)linkY].links.add(m.getTileFromMousePos(gc));
                } else {
                    linking = false;
                }
//            }
        }

//        if(gc.getInput().isMousePressed(Input.MOUSE_MIDDLE_BUTTON)) {
//            Vector2f p = getMouseTilePos(gc);
//            if (p != null) {
//                linking = false;
//                linkX = (int) p.x;
//                linkY = (int) p.y;
//            }
//        }

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
                    tm.transitionFade(m, new Model(m.getProperty("name")+".txt", 0.5f, 1f), State.CREDITS, 1f);
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
    private void updateCREDITS(GameContainer gc){
        if(gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
            tm.transitionRotate(m, currentState, 90, 0.2f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_LEFT)) {
            tm.transitionRotate(m, currentState, -90, 0.2f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            m.toggleRedBlue();
        }
        if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            tm.transitionFade(m, new Model("0.txt", 0.5f, 0f), State.MENU, 0.4f);
        }
    }

    private void renderStateText(GameContainer gc, Graphics g, State state, Model m) {
        switch(state) {
            case MENU:
//                renderText(gc, g, m.getOpacity(), m.getScale(), "Squaring the Circle", 0, -135, 0.8f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Level Select", 0, -135, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Credits", 90, -100, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Quit", -90, -45, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Editor", 180, -60, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Use arrow", 0, -110, 0.16f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "keys to turn", 0, -135, 0.10f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Use space to", 0, -145, 0.0f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "toggle", 0, -70, -0.06f, m);
                break;
            case LEVEL_SELECT:
                renderText(gc, g, m.getOpacity(), m.getScale(), m.getProperty("name"), 0, -90, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), m.getProperty("prev").split("\\.")[0], 90, -90, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), m.getProperty("next").split("\\.")[0], -90, -90, 0.6f, m);
                break;
            case LEVEL:
                renderText(gc, g, m.getOpacity(), m.getScale(), "Best move count: " + m.getProperty("score"), 0, -225, 0.45f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Your move count: " + m.score, 0, -225, -0.40f, m);
            case TRANSITION:

                break;
            case CREDITS:
                renderText(gc, g, m.getOpacity(), m.getScale(), "Ollie Steptoe", 0, -140, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Alistair Brewin", 90, -160, 0.6f, m);
                renderText(gc, g, m.getOpacity(), m.getScale(), "Anton Nikitin", -90, -150, 0.6f, m);
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
    private void renderToolbar(GameContainer gc, Graphics graphics) {
        for(int i = 0; i < toolbar.size(); i++) {
            graphics.setColor(Color.darkGray);
            graphics.draw(toolbar.get(i));
            new Tile(i).render(11, (int)((gc.getHeight()*0.125f) + toolbar.get(i).getWidth()*i+1), (int)(toolbar.get(i).getWidth()-1), graphics, m.getOpacity());
        }
    }

    private Vector2f getMouseTilePos(GameContainer gc) {
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

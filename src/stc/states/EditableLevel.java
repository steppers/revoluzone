package stc.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import stc.*;
import stc.UI.UIButton;
import stc.UI.UILabel;
import stc.UI.UIRenderable;
import stc.UI.UITextInput;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.security.ssl.Debug;

import java.io.Console;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ali on 23/08/2017.
 */
public class EditableLevel {

    private GameState gs;
    private TransitionManager tm;
    private Model m;

    private Tile.Type drawTileType = Tile.Type.EMPTY;
    private ArrayList<Rectangle> toolbar;
    private boolean linking = false;
    private float linkSrcX, linkSrcY;
    private float linkDstX, linkDstY;
    private ArrayList<Integer> toolbarOrder;

    public Tile[][] initTiles;

    private boolean escaping = false;

    private ArrayList<UIRenderable> staticUI;
    private ArrayList<UIRenderable> rotatingUI;

    public EditableLevel(GameState gameState, TransitionManager tm, GameContainer gc) {

        gs = gameState;
        this.tm = tm;
        this.m = gs.m;
        toolbar = new ArrayList<>();
        toolbarOrder = new ArrayList<>();

        initTiles = new Tile[m.gridSize][m.gridSize];
        initTiles = m.tiles;

        for(int i = 0; i < Tile.Type.values().length; i++) {
            m.remainingTileNumber[i] = m.allowedTileNumber[i] - m.tileCount()[i];
        }

        for (Tile.Type t : Tile.Type.values()) {
            if (m.allowedPlacedTileNumber[t.ordinal()] != 0) {
                toolbarOrder.add(t.ordinal());
            }
        }

        //Initialisation
        float size = (0.75f * gc.getHeight()) / Tile.Type.values().length;
        for (int i = 0; i < toolbarOrder.size(); i++) {
            Rectangle r = new Rectangle(10, (gc.getHeight() * 0.125f) + size * i, size, size);
            toolbar.add(r);
        }

        //Static UI
        staticUI = new ArrayList<>();
        UILabel tmpLabel = new UILabel(gc);
        tmpLabel.text = "Left click:\nSelect Tile / \nPlace Tile";
        tmpLabel.scale = m.getScale() / 0.6f;
        tmpLabel.color = new Color(Color.green).darker(0.4f);
        tmpLabel.anchor.set(1.0f, 0.0833333f);
        tmpLabel.offset.set(-0.1f, 0.0f);
        staticUI.add(tmpLabel.clone());

        tmpLabel.text = "Right click:\nStart Link";
        tmpLabel.anchor.set(1.0f, 0.25f);
        staticUI.add(tmpLabel.clone());

        tmpLabel.text = "Middle click:\nRemove links";
        tmpLabel.anchor.set(1.0f, 0.4166666f);
        staticUI.add(tmpLabel.clone());

        for (int i = 0; i < toolbarOrder.size(); i++) {
            tmpLabel.text = "x " + Integer.toString(m.remainingTileNumber[toolbarOrder.get(i)]);
            tmpLabel.anchor.set(0.17f, 0.155f + 0.062f * i);
            staticUI.add(tmpLabel.clone());
        }

        //Rotating UI
        rotatingUI = new ArrayList<>();
        tmpLabel = new UILabel("Selected:\n" + drawTileType.getName(), gc);
        tmpLabel.anchor.set(0.5f, 0.5f);
        tmpLabel.offset.set(0f, -0.41f);
        tmpLabel.scale = m.getScale();
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.rotation = 90f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.rotation = 180f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.rotation = -90f;
        rotatingUI.add(tmpLabel.clone());
        UIButton tmpButton = new UIButton("Play Mode", gc);
        tmpButton.anchor.set(0.9f, 0.8f);
        tmpButton.offset.set(-0.05f, 0.0f);
        tmpButton.color = new Color(Color.lightGray).darker(0.3f);
        tmpButton.setOnClickCallback(() -> {
            gs.currentState = GameState.State.LEVEL;
            m.reset();
        });
        staticUI.add(tmpButton.clone());
    }

    public void update(GameContainer gc) {
        for(int i = 0; i < Tile.Type.values().length; i++) {
            m.remainingTileNumber[i] = m.allowedTileNumber[i] - m.tileCount()[i];
            if(toolbarOrder.contains(i)){
                UILabel tmpLabel = (UILabel)staticUI.get(3 + toolbarOrder.indexOf(i));
                tmpLabel.text = "x " + Integer.toString(m.remainingTileNumber[i]);
                staticUI.set(3 + toolbarOrder.indexOf(i), tmpLabel);
            }
        }
        m = gs.m;
        if(!linking) {
            for(UIRenderable r : staticUI) {
                r.update();
            }
            for(UIRenderable r : rotatingUI) {
                r.update();
            }
            if(gc.getInput().isKeyDown(Input.KEY_ESCAPE) || escaping) {
                escaping = true;
                m.score = 0;
                if(m.getRotation() != 0) {
                    tm.transitionFadeRotate(m, new Model(m.getProperty("filename"), 1.0f, 0f), GameState.State.EDITABLE_LEVEL, -m.getRotation(), 0.3f);
                }
                if(!tm.isTransitioning()) {
                    tm.transitionShrink(m, GameState.State.LEVEL_SELECT, 0.6f, 0.3f);
                    escaping = false;
                }
            }
            else if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
                tm.transitionRotate(m, gs.currentState, 90, 0.2f);
            }
            else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
                tm.transitionRotate(m, gs.currentState, -90, 0.2f);
            }
            else if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
                if (m.getTileUnderBall().type != Tile.Type.BLUE && m.getTileUnderBall().type != Tile.Type.RED) {
                    m.toggleRedBlue();
                }
            }
            //Left mouse clicks
            if(gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                Tile t = m.getTileFromMousePos(gc);
                //Set a tile if on the level
                if (t != null
                        && (t.type == Tile.Type.EMPTY || drawTileType == Tile.Type.EMPTY)
                        && t.x != 0 && t.x != m.gridSize-1 && t.y != 0 && t.y != m.gridSize-1
                        && t != m.getTileUnderBall()
                        && t.hasSlider(m) == null
                        && m.tileCount()[drawTileType.ordinal()] < m.allowedTileNumber[drawTileType.ordinal()]) {

                    if (drawTileType == Tile.Type.RAIL && m.tileCount()[Tile.Type.RAIL.ordinal()] < m.allowedPlacedTileNumber[Tile.Type.RAIL.ordinal()]) {
                            t.isRail = true;
                        } else {
                            if (drawTileType != Tile.Type.SLIDER) {
                                t.isRail = false;
                                t.type = drawTileType;
                                t.resetType = drawTileType;
                                if(drawTileType == Tile.Type.RED || drawTileType == Tile.Type.BLUE) {
                                    t.reset(m.redEnabled);
                                }

                            } else if(m.tileCount()[Tile.Type.SLIDER.ordinal()] < m.allowedPlacedTileNumber[Tile.Type.SLIDER.ordinal()]){
                                boolean add = true;
                                for(Slider s : m.sliders) {
                                    if(s.resetX == t.x && s.resetY == t.y)
                                        add = false;
                                }
                                if(add) {
                                    m.addSlider(t.x, t.y);
                                }
                            }
                        }
                        m.recalcAll();
                    }
                //Set current tile type if on toolbar
                {
                    for (int i = 0; i < toolbarOrder.size(); i++) {
                        Rectangle r = toolbar.get(i);
                        if (r.contains(gc.getInput().getMouseX(), gc.getInput().getMouseY())) {
                            drawTileType = Tile.Type.values()[toolbarOrder.get(i)];
                            for (UIRenderable l : rotatingUI) {
                                ((UILabel) l).text = "Selected:\n" + drawTileType.getName();
                            }
                        }

                    }
                }
            }

            //Detect link start
            if(gc.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
                Vector2f p = gs.getMouseTilePos(gc);
                if(p != null) {
                    if (p.x < m.gridSize - 1 && p.y < m.gridSize - 1 && p.x > 0 && p.y > 0) {
                        linking = true;
                        linkSrcX = p.x;
                        linkSrcY = p.y;
                        linkDstX = p.x;
                        linkDstY = p.y;
                        ((UILabel)staticUI.get(1)).text = "Right click:\nEnd Link";
                    }
                }
            }

            //Clear links from tile
            if(gc.getInput().isMousePressed(Input.MOUSE_MIDDLE_BUTTON)) {
                Tile t = m.getTileFromMousePos(gc);
                if(t != null) {
                    t.links.clear();
                }
            }

            //Update the tiles under the ball
            Tile t = m.getTileUnderBall();
            t.activate(m);
            for(Slider s: m.sliders){
                Tile ts = m.getTileUnderSlider(s);
                ts.activate(m);
            }
        } else {
            //Update the current link end
            Vector2f p = gs.getMouseTilePos(gc);
            if(p != null) {
                linkDstX = p.x;
                linkDstY = p.y;
                //Detect link end
                if (gc.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
                    if (p != null) {
                        if (p.x < m.gridSize - 1 && p.y < m.gridSize - 1 && p.x > 0 && p.y > 0) {
                            linking = false;
                            m.tiles[(int) linkSrcX][(int) linkSrcY].links.add(m.tiles[(int) p.x][(int) p.y]);
                            linkSrcX = -100f;
                            linkSrcY = -100f;
                            linkDstX = -100f;
                            linkDstY = -100f;
                            ((UILabel) staticUI.get(1)).text = "Right click:\nStart Link";
                        }
                    }
                }
            }
        }
        //Prevent inputs queuing up
        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {}
        if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {}
        if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {}
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {}
        if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {}
    }

    public void render(GameContainer gc, Graphics g) {
        m = gs.m;
        m.render(gc, g);
        renderToolbar(gc, g);
        g.setColor(Color.orange);
        g.setLineWidth(3);
        if(linking) {
            Line l = new Line(new Vector2f((int)linkSrcX, (int)linkSrcY), new Vector2f((int)linkDstX, (int)linkDstY));
            m.drawLink(l, gc, g);
        }
        for(int x = 0; x < m.gridSize; x++) {
            for (int y = 0; y < m.gridSize; y++) {
                for (Tile t : m.tiles[x][y].links) {
                    Line l = new Line(x, y, t.x, t.y);
                    m.drawLink(l, gc, g);
                }
            }
        }
        g.setLineWidth(1);
        renderText(g, m);
    }

    public void renderTransition(GameContainer gc, Graphics g) {
        m = gs.m;
        renderToolbar(gc, g);
        Color c = new Color(Color.orange);
        c.a = (m.getScale()-0.6f)*2.5f;
        g.setColor(c);
        g.setLineWidth(3);
        for(int x = 0; x < m.gridSize; x++) {
            for (int y = 0; y < m.gridSize; y++) {
                for (Tile t : m.tiles[x][y].links) {
                    Line l = new Line(x, y, t.x, t.y);
                    m.drawLink(l, gc, g);
                }
            }
        }
        for(UIRenderable r : staticUI) {
            r.update();
        }
        for(UIRenderable r : rotatingUI) {
            r.update();
        }
    }

    public void renderText(Graphics g, Model m) {
        if(gs.currentState == GameState.State.TRANSITION) {
            for(UIRenderable r : rotatingUI) {
                r.color.a = (m.getScale()-0.6f)*2.5f;
                r.offsetRotation(m.getRotation());
                r.scaleOffset(m.getScale());
                r.render(g);
            }
        } else {
            for(UIRenderable r : rotatingUI) {
                r.color.a = m.getOpacity();
                r.offsetRotation(m.getRotation());
                r.scaleOffset(m.getScale());
                r.render(g);
            }
        }

        for(UIRenderable r : staticUI) {
            r.scale = m.getScale();
            r.scaleOffset(m.getScale());
            r.color.a = (m.getScale()-0.6f)*2.5f;
            r.render(g);
        }
    }

    private void renderToolbar(GameContainer gc, Graphics graphics) {
        graphics.resetTransform();
        Color c = new Color(Color.darkGray);
        c.a = (m.getScale()-0.6f)*2.5f;
        for(int i = 0; i < toolbarOrder.size(); i++) {
            graphics.setColor(c);
            graphics.setLineWidth(3f);
            graphics.draw(toolbar.get(i));
            new Tile(toolbarOrder.get(i)).render(11, (int) ((gc.getHeight() * 0.125f) + (toolbar.get(i).getHeight() * i) + 1), (int) toolbar.get(i).getWidth() - 1, graphics, (m.getScale() - 0.6f) * 2f);
        }
    }
}


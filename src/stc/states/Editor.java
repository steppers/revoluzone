package stc.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import stc.*;
import stc.UI.TextLabel;
import stc.UI.TextRenderer;

import java.util.ArrayList;

/**
 * Created by Ollie on 19/05/2017.
 */
public class Editor {

    private GameState gs;
    private TransitionManager tm;
    private Model m;
    private TextRenderer tr;

    private Tile.Type drawTileType = Tile.Type.EMPTY;
    private ArrayList<Rectangle> toolbar;
    private boolean linking = false;
    private float linkSrcX, linkSrcY;
    private float linkDstX, linkDstY;

    private ArrayList<TextLabel> labels;
    private TextLabel placeInstructions;
    private TextLabel linkAddInstructions;
    private TextLabel linkRemoveInstructions;

    public Editor(GameState gameState, TransitionManager tm, GameContainer gc) {
        gs = gameState;
        this.tm = tm;
        tr = gs.textRenderer;
        m = gs.m;
        toolbar = new ArrayList<>();

        //Initialisation
        float size = (0.75f * gc.getHeight()) / Tile.Type.values().length;
        for (Tile.Type t : Tile.Type.values()) {
            Rectangle r = new Rectangle(10, (gc.getHeight()*0.125f) + size*t.ordinal(), size, size);
            toolbar.add(r);
        }

        //Labels
        labels = new ArrayList<>();
        TextLabel temp = new TextLabel("Selected:\n" + drawTileType.getName());
        temp.anchor.set(0.5f, 0.5f);
        temp.offset.set(0f, -0.41f);
        temp.scale = m.getScale()/0.6f;
        labels.add(temp.clone());
        temp.rotation = 90f;
        labels.add(temp.clone());
        temp.rotation = 180f;
        labels.add(temp.clone());
        temp.rotation = -90f;
        labels.add(temp.clone());

        temp = new TextLabel();
        temp.scale = m.getScale()/0.6f;
        temp.color = Color.green.darker(0.4f);
        temp.anchor.set(1.0f, 0.1f);
        temp.text = "Left click:\nPlace Tile";
        temp.offset.set(-0.1f, 0.0f);
        placeInstructions = temp.clone();
        temp.anchor.set(1.0f, 0.3f);
        temp.text = "Right click:\nStart Link";
        temp.offset.set(-0.1f, 0.0f);
        linkAddInstructions = temp.clone();
        temp.anchor.set(1.0f, 0.5f);
        temp.text = "Middle click:\nRemove links";
        temp.offset.set(-0.1f, 0.0f);
        linkRemoveInstructions = temp.clone();
    }

    public void update(GameContainer gc) {
        m = gs.m;
        if(!linking) {
            if(gc.getInput().isKeyPressed(Input.KEY_R)) {
                m.reset();
            }
            if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
                tm.transitionShrink(m, GameState.State.MENU, 0.6f, 0.3f);
            }
            if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
                tm.transitionRotate(m, gs.currentState, 90, 0.2f);
            }
            else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
                tm.transitionRotate(m, gs.currentState, -90, 0.2f);
            }
            if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
                m.toggleRedBlue();
            }
            //Left mouse clicks
            if(gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                Tile t = m.getTileFromMousePos(gc);
                //Set a tile if on the level
                if (t != null) {
                    if(drawTileType == Tile.Type.RAIL) {
                        t.isRail = true;
                    } else {
                        t.type = drawTileType;
                        t.resetType = drawTileType;
                        if(drawTileType != Tile.Type.SLIDER)
                            t.isRail = false;
                    }
                    m.reset();
                }
                //Set current tile type if on toolbar
                for (Rectangle r : toolbar) {
                    if (r.contains(gc.getInput().getMouseX(),gc.getInput().getMouseY())) {
                        drawTileType = Tile.Type.values()[toolbar.indexOf(r)];
                        for(TextLabel l : labels) {
                            l.text = "Selected:\n" + drawTileType.getName();
                        }
                    }
                }
            }

            //Detect link start
            if(gc.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
                Vector2f p = gs.getMouseTilePos(gc);
                if(p != null) {
                    if (p != null) {
                        linking = true;
                        linkSrcX = p.x;
                        linkSrcY = p.y;
                        linkDstX = p.x;
                        linkDstY = p.y;
                        linkAddInstructions.text = "Right click:\nEnd Link";
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
            t.activate();
        } else {
            //Prevent inputs queuing up
            if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {}
            if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {}
            if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {}
            if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {}
            if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {}

            //Update the current link end
            Vector2f p = gs.getMouseTilePos(gc);
            if(p != null) {
                linkDstX = p.x;
                linkDstY = p.y;
                //Detect link end
                if (gc.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
                    if (p != null) {
                        linking = false;
                        m.tiles[(int) linkSrcX][(int) linkSrcY].links.add(m.tiles[(int) p.x][(int) p.y]);
                        linkSrcX = -100f;
                        linkSrcY = -100f;
                        linkDstX = -100f;
                        linkDstY = -100f;
                        linkAddInstructions.text = "Right click:\nStart Link";
                    }
                }
            }
        }
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
        g.setColor(Color.orange);
        g.setLineWidth(3);
        for(int x = 0; x < m.gridSize; x++) {
            for (int y = 0; y < m.gridSize; y++) {
                for (Tile t : m.tiles[x][y].links) {
                    Line l = new Line(x, y, t.x, t.y);
                    m.drawLink(l, gc, g);
                }
            }
        }
    }

    public void renderText(Graphics g, Model m) {
        if(gs.currentState == GameState.State.TRANSITION) {
            for(TextLabel l : labels) {
                l.color.a = (m.getScale()-0.6f)*2f;
                l.offsetRotation(m.getRotation());
                l.scaleOffset(m.getScale());
                tr.renderText(g, l);
            }
        } else {
            for (TextLabel l : labels) {
                l.color.a = m.getOpacity();
                l.offsetRotation(m.getRotation());
                l.scaleOffset(m.getScale());
                tr.renderText(g, l);
            }
        }
        placeInstructions.scale = m.getScale();
        placeInstructions.scaleOffset(m.getScale());
        placeInstructions.color.a = (m.getScale()-0.6f)*2f;
        tr.renderText(g, placeInstructions);
        linkAddInstructions.scale = m.getScale();
        linkAddInstructions.scaleOffset(m.getScale());
        linkAddInstructions.color.a = (m.getScale()-0.6f)*2f;
        tr.renderText(g, linkAddInstructions);
        linkRemoveInstructions.scale = m.getScale();
        linkRemoveInstructions.scaleOffset(m.getScale());
        linkRemoveInstructions.color.a = (m.getScale()-0.6f)*2f;
        tr.renderText(g, linkRemoveInstructions);
    }

    private void renderToolbar(GameContainer gc, Graphics graphics) {
        graphics.resetTransform();
        for(int i = 0; i < toolbar.size(); i++) {
            Color c = Color.darkGray;
            c.a = (m.getScale()-0.6f)*2f;
            graphics.setColor(c);
            graphics.setLineWidth(3f);
            graphics.draw(toolbar.get(i));
            new Tile(i).render(11, (int) ((gc.getHeight() * 0.125f) + (toolbar.get(i).getHeight() * i) + 1), (int)toolbar.get(i).getWidth()-1, graphics, (m.getScale()-0.6f)*2f);
        }
    }

}
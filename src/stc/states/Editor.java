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

import java.util.ArrayList;

/**
 * Created by Ollie on 19/05/2017.
 */
public class Editor {

    private GameState gs;
    private TransitionManager tm;
    private Model m;

    private Tile.Type drawTileType = Tile.Type.EMPTY;
    private ArrayList<Rectangle> toolbar;
    private boolean linking = false;
    private float linkSrcX, linkSrcY;
    private float linkDstX, linkDstY;

    private ArrayList<UIRenderable> staticUI;
    private ArrayList<UIRenderable> rotatingUI;

    public Editor(GameState gameState, TransitionManager tm, GameContainer gc) {
        gs = gameState;
        this.tm = tm;
        this.m = gs.m;
        toolbar = new ArrayList<>();

        //Initialisation
        float size = (0.75f * gc.getHeight()) / Tile.Type.values().length;
        for (Tile.Type t : Tile.Type.values()) {
            Rectangle r = new Rectangle(10, (gc.getHeight()*0.125f) + size*t.ordinal(), size, size);
            toolbar.add(r);
        }

        //Static UI
        staticUI = new ArrayList<>();
        UILabel tmpLabel = new UILabel(gc);
        tmpLabel.text = "Left click:\nPlace Tile";
        tmpLabel.scale = m.getScale()/0.6f;
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

        UIButton tmpButton = new UIButton("save", gc);
        tmpButton.anchor.set(1.0f, 0.95f);
        tmpButton.offset.set(-0.05f, 0.0f);
        tmpButton.color = new Color(Color.lightGray).darker(0.3f);
        tmpButton.setOnClickCallback(() -> m.saveToFile("user_levels/" + ((UITextInput)staticUI.get(4)).getText(), ((UITextInput)staticUI.get(4)).getText()));
        staticUI.add(tmpButton.clone());

        UITextInput tmpInput = new UITextInput("namehere", gc);
        tmpInput.anchor.set(1.0f, 0.95f);
        tmpInput.offset.set(-0.2f, 0.0f);
        tmpInput.setColor(new Color(Color.lightGray).darker(0.3f));
        staticUI.add(tmpInput);

        tmpLabel.text = "Map size";
        tmpLabel.anchor.set(1.0f, 0.88f);
        tmpLabel.offset.set(-0.14f, 0.0f);
        staticUI.add(tmpLabel.clone());

        tmpButton.setText("+");
        tmpButton.anchor.set(1.0f, 0.88f);
        tmpButton.offset.set(-0.071f, 0.0f);
        tmpButton.setOnClickCallback(() -> m.resize(m.gridSize-1));
        staticUI.add(tmpButton.clone());

        tmpButton.setText("-");
        tmpButton.anchor.set(1.0f, 0.88f);
        tmpButton.offset.set(-0.03f, 0.0f);
        tmpButton.setOnClickCallback(() -> {
            if(m.gridSize > 4) {
                m.resize(m.gridSize - 3);
                if ((int) m.ball.x == (m.gridSize - 1)) {
                    m.ball = new Ball(m.gridSize - 2, (int) m.ball.y);
                }
                if ((int) m.ball.y == (m.gridSize - 1)) {
                    m.ball = new Ball((int) m.ball.x, m.gridSize - 2);
                }
            }
        });
        staticUI.add(tmpButton.clone());

        //Rotating UI
        rotatingUI = new ArrayList<>();
        tmpLabel = new UILabel("Selected:\n" + drawTileType.getName(), gc);
        tmpLabel.anchor.set(0.5f, 0.5f);
        tmpLabel.offset.set(0f, -0.41f);
        tmpLabel.scale = m.getScale()/0.6f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.rotation = 90f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.rotation = 180f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.rotation = -90f;
        rotatingUI.add(tmpLabel.clone());
    }

    public void update(GameContainer gc) {
        m = gs.m;
        if(!linking) {
            for(UIRenderable r : staticUI) {
                r.update();
            }
            for(UIRenderable r : rotatingUI) {
                r.update();
            }
            if(gc.getInput().isKeyPressed(Input.KEY_R)) {
                m.reset();
                m.recalcAll();
            }
            if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
                tm.transitionShrink(m, GameState.State.MENU, 0.6f, 0.3f);
            }
            if(gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
                tm.transitionRotate(m, gs.currentState, 90, 0.2f);
            }
            else if(gc.getInput().isKeyDown(Input.KEY_LEFT)) {
                tm.transitionRotate(m, gs.currentState, -90, 0.2f);
            }
            if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
                if(!((UITextInput)staticUI.get(4)).acceptingInput()) {
                    if (m.getTileUnderBall().type != Tile.Type.BLUE && m.getTileUnderBall().type != Tile.Type.RED)
                        m.toggleRedBlue();
                }
            }
            //Left mouse clicks
            if(gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                Tile t = m.getTileFromMousePos(gc);
                //Set a tile if on the level
                if (t != null && t.x != 0 && t.x != m.gridSize-1 && t.y != 0 && t.y != m.gridSize-1) {
                    if(t != m.getTileUnderBall()) {
                        if (drawTileType == Tile.Type.RAIL) {
                            t.isRail = true;
                        } else {
                            if (drawTileType != Tile.Type.SLIDER) {
                                t.isRail = false;
                                Slider s;
                                if((s = t.hasSlider(m)) != null) {
                                    m.sliders.remove(s);
                                }
                                t.type = drawTileType;
                                t.resetType = drawTileType;
                            } else {
                                m.addSlider(t.x, t.y);
                            }
                        }
                        m.recalcAll();
                    }
                }
                //Set current tile type if on toolbar
                for (Rectangle r : toolbar) {
                    if (r.contains(gc.getInput().getMouseX(),gc.getInput().getMouseY())) {
                        drawTileType = Tile.Type.values()[toolbar.indexOf(r)];
                        for(UIRenderable l : rotatingUI) {
                            ((UILabel)l).text = "Selected:\n" + drawTileType.getName();
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
        } else {
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
                        ((UILabel)staticUI.get(1)).text = "Right click:\nStart Link";
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
        for(int i = 0; i < toolbar.size(); i++) {
            graphics.setColor(c);
            graphics.setLineWidth(3f);
            graphics.draw(toolbar.get(i));
            new Tile(i).render(11, (int) ((gc.getHeight() * 0.125f) + (toolbar.get(i).getHeight() * i) + 1), (int)toolbar.get(i).getWidth()-1, graphics, (m.getScale()-0.6f)*2f);
        }
    }

}
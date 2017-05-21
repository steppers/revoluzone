package stc.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import stc.*;
import stc.UI.proto.UILabel;
import stc.UI.proto.UIRenderable;

import java.util.ArrayList;

/**
 * Created by Ollie on 19/05/2017.
 */
public class Menu {
    private GameState gs;
    private TransitionManager tm;
    private Model m;

    private ArrayList<UIRenderable> staticUI;
    private ArrayList<UIRenderable> rotatingUI;

    public Menu(GameState gameState, TransitionManager tm, GameContainer gc) {
        gs = gameState;
        this.tm = tm;
        this.m = gs.m;

        //Static UI
        staticUI = new ArrayList<>();
        UILabel tmpLabel = new UILabel(gc);
        tmpLabel.text = "Press Space to toggle blocks\nArrows to rotate, Enter & Esc to navigate menus";
        tmpLabel.anchor.set(0.5f, 0.5f);
        tmpLabel.offset.set(0f, 0.7f);
        tmpLabel.color = Color.green.darker(0.4f);
        staticUI.add(tmpLabel.clone());

        //Rotating UI
        rotatingUI = new ArrayList<>();
        tmpLabel = new UILabel("Level Select", gc);
        tmpLabel.anchor.set(0.5f, 0.5f);
        tmpLabel.offset.set(0f, -0.46f);
        tmpLabel.scale = m.getScale()/0.6f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = "Quit";
        tmpLabel.rotation = -90f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = "Editor";
        tmpLabel.rotation = 180f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = "Credits";
        tmpLabel.rotation = 90f;
        rotatingUI.add(tmpLabel.clone());
    }

    public void update(GameContainer gc) {
        m = gs.m;
        if(gc.getInput().isKeyDown(Input.KEY_ENTER)) {
            int r = (int)m.getRotation();
            while(r < 0)
                r += 360;
            switch(r % 360) {
                case 0:
                    tm.transitionFade(m, new Model("Level 1.txt", 0.6f, 0f), GameState.State.LEVEL_SELECT, 0.3f);
                    break;
                case 90:
                    tm.transitionShrink(m, GameState.State.QUIT, 0.0f, 0.3f);
                    break;
                case 180:
                    tm.transitionGrow(m, GameState.State.EDITOR, 1.0f, 0.3f);
                    break;
                case 270:
                    tm.transitionFade(m, new Model(m.getProperty("filename"), 0.6f, 0.3f), GameState.State.CREDITS, 0.4f);
                    break;
            }
        }
        if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            tm.transitionRotate(m, gs.currentState, 90, 0.2f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            tm.transitionRotate(m, gs.currentState, -90, 0.2f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            if(m.getTileUnderBall().type != Tile.Type.BLUE && m.getTileUnderBall().type != Tile.Type.RED)
                m.toggleRedBlue();
        }

        for(UIRenderable r : staticUI) {
            r.update();
        }
        for(UIRenderable r : rotatingUI) {
            r.update();
        }

        //Update the tiles under the ball
        Tile t = m.getTileUnderBall();
        t.activate();
    }

    public void render(GameContainer gc, Graphics g) {
        gs.m.render(gc, g);
        renderText(g, gs.m);
    }

    public void renderText(Graphics g, Model m) {
        if(gs.currentState == GameState.State.TRANSITION) {
            if(tm.getNewState() == GameState.State.EDITOR || gs.previousState == GameState.State.EDITOR) {
                for(UIRenderable r : rotatingUI) {
                    r.color.a = 1f-(m.getScale()-0.6f)*2.5f;
                    r.scale = m.getScale()/0.6f;
                    r.offsetRotation(m.getRotation());
                    r.scaleOffset(m.getScale());
                    r.render(g);
                }
            } else {
                for(UIRenderable r : rotatingUI) {
                    r.color.a = m.getOpacity();
                    r.scale = m.getScale()/0.6f;
                    r.offsetRotation(m.getRotation());
                    r.scaleOffset(m.getScale());
                    r.render(g);
                }
            }
        } else {
            for(UIRenderable r : rotatingUI) {
                r.color.a = m.getOpacity();
                r.scale = m.getScale()/0.6f;
                r.offsetRotation(m.getRotation());
                r.scaleOffset(m.getScale());
                r.render(g);
            }
        }
        for(UIRenderable r : staticUI) {
            r.scale = m.getScale()/0.6f;
            r.scaleOffset(m.getScale());
            r.color.a = m.getOpacity();
            r.render(g);
        }
    }
}

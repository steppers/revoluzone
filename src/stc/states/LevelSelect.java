package stc.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import stc.GameState;
import stc.Model;
import stc.TransitionManager;
import stc.UI.UILabel;
import stc.UI.UIRenderable;

import java.util.ArrayList;

/**
 * Created by Ollie on 20/05/2017.
 */
public class LevelSelect {

    private GameState gs;
    private TransitionManager tm;
    private Model m;

    private ArrayList<UIRenderable> staticUI;
    private ArrayList<UIRenderable> rotatingUI;

    public LevelSelect(GameState gameState, TransitionManager tm, GameContainer gc) {
        gs = gameState;
        this.tm = tm;
        m = gs.m;

        //Static UI
        staticUI = new ArrayList<>();
        UILabel tmpLabel = new UILabel(gc);
        tmpLabel.text = "Arrow keys to choose level\nEnter to play, Esc to go back";
        tmpLabel.anchor.set(0.5f, 0.5f);
        tmpLabel.offset.set(0f, 0.7f);
        tmpLabel.color = Color.green.darker(0.4f);
        staticUI.add(tmpLabel.clone());

        //Rotating UI
        rotatingUI = new ArrayList<>();
        tmpLabel = new UILabel(m.getProperty("name"), gc);
        tmpLabel.anchor.set(0.5f, 0.5f);
        tmpLabel.offset.set(0f, -0.46f);
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = m.getProperty("prev").split("\\.")[0];
        tmpLabel.rotation = 90f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = m.getProperty("next").split("\\.")[0];
        tmpLabel.rotation = -90f;
        rotatingUI.add(tmpLabel.clone());
    }

    public void update(GameContainer gc) {
        m = gs.m;
        if(gc.getInput().isKeyDown(Input.KEY_ENTER)) {
            tm.transitionGrow(m, GameState.State.LEVEL, 1.0f, 0.3f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            tm.transitionFade(m, new Model("menu.txt", 0.6f, 0f), GameState.State.MENU, 0.4f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            tm.transitionFadeRotate(m, new Model(m.getProperty("next"), 0.6f, 0f), gs.currentState, 90, 0.3f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            tm.transitionFadeRotate(m, new Model(m.getProperty("prev"), 0.6f, 0f), gs.currentState, -90, 0.3f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            m.toggleRedBlue();
        }

        for(UIRenderable r : staticUI) {
            r.update();
        }
        for(UIRenderable r : rotatingUI) {
            r.update();
        }
    }

    public void render(GameContainer gc, Graphics g) {
        gs.m.render(gc, g);
        renderText(g, gs.m);
    }

    public void renderText(Graphics g, Model m) {
        ((UILabel)rotatingUI.get(0)).text = m.getProperty("name");
        ((UILabel)rotatingUI.get(1)).text = m.getProperty("prev").split("\\.")[0];
        ((UILabel)rotatingUI.get(2)).text = m.getProperty("next").split("\\.")[0];
        if(gs.currentState == GameState.State.TRANSITION) {
            if(tm.getNewState() == GameState.State.LEVEL || gs.previousState == GameState.State.LEVEL) {
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

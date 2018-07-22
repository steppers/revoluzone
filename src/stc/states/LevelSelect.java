package stc.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import stc.GameState;
import stc.Model;
import stc.TransitionManager;
import stc.UI.UIButton;
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
        /*UILabel tmpLabel = new UILabel(gc);
        tmpLabel.text = "Arrow keys to choose level\nEnter to play, Esc to go back";
        tmpLabel.anchor.set(0.5f, 0.5f);
        tmpLabel.offset.set(0f, 0.7f);
        tmpLabel.color = Color.green.darker(0.4f);
        staticUI.add(tmpLabel.clone());*/

        //Rotating UI
        rotatingUI = new ArrayList<>();
        UIButton tmpButton = new UIButton(m.getProperty("name"), gc);
        tmpButton.anchor.set(0.5f, 0.5f);
        tmpButton.offset.set(0f, -0.46f);
        tmpButton.setTransparentBox(true);
        tmpButton.setOnClickCallback(() -> {
            if(!tm.isTransitioning() && m.getRotation() == 0 && !m.locked) {
                tm.transitionGrow(m, GameState.State.LEVEL, 1.0f, 0.3f);
            }
        });
        rotatingUI.add(tmpButton.clone());
        tmpButton.setText(m.getProperty("prev").split("\\.")[0]);
        tmpButton.rotation = 90f;
        tmpButton.setOnClickCallback(() -> {
            if(!tm.isTransitioning() && m.getRotation() == 0) {
                tm.transitionFadeRotate(m, new Model(m.getProperty("prev"), 0.6f, 0f), gs.currentState, -90, 0.3f);
            }
        });
        rotatingUI.add(tmpButton.clone());
        tmpButton.setText(m.getProperty("next").split("\\.")[0]);
        tmpButton.rotation = -90f;
        tmpButton.setOnClickCallback(() -> {
            if(!tm.isTransitioning() && m.getRotation() == 0) {
                tm.transitionFadeRotate(m, new Model(m.getProperty("next"), 0.6f, 0f), gs.currentState, 90, 0.3f);
            }
        });
        rotatingUI.add(tmpButton.clone());
    }

    public void update(GameContainer gc) {
        m = gs.m;
        if(gc.getInput().isKeyDown(Input.KEY_ENTER) && !m.locked) {
            tm.transitionGrow(m, GameState.State.LEVEL, 1.0f, 0.3f);
        }
        else if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            tm.transitionFade(m, new Model("menu.txt", 0.6f, 0f), GameState.State.MENU, 0.4f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            tm.transitionFadeRotate(m, new Model(m.getProperty("next"), 0.6f, 0f), gs.currentState, 90, 0.3f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            tm.transitionFadeRotate(m, new Model(m.getProperty("prev"), 0.6f, 0f), gs.currentState, -90, 0.3f);
        }
    else if(gc.getInput().isKeyPressed(Input.KEY_SPACE) && !m.locked) {
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
        Model tempPrev = new Model(m.getProperty("prev"), 1f, 1f);
        Model tempNext = new Model(m.getProperty("next"), 1f, 1f);
        ((UIButton)rotatingUI.get(0)).setText(m.getProperty("name"));
        if(Integer.parseInt(m.getProperty("score")) <= Integer.parseInt(m.getProperty("goal"))) {
            ((UIButton) rotatingUI.get(0)).setTextColor(Color.green);
        }
        ((UIButton)rotatingUI.get(1)).setText(tempPrev.getProperty("name"));
        if(Integer.parseInt(tempPrev.getProperty("score")) <= Integer.parseInt(tempPrev.getProperty("goal"))) {
            ((UIButton) rotatingUI.get(1)).setTextColor(Color.green);
        }
        ((UIButton)rotatingUI.get(2)).setText(tempNext.getProperty("name"));
        if(Integer.parseInt(tempNext.getProperty("score")) <= Integer.parseInt(tempNext.getProperty("goal"))) {
            ((UIButton) rotatingUI.get(2)).setTextColor(Color.green);
        }
        if(gs.currentState == GameState.State.TRANSITION) {
            if(tm.getNewState() == GameState.State.LEVEL || gs.previousState == GameState.State.LEVEL || gs.previousState == GameState.State.EDITABLE_LEVEL) {
                for(UIRenderable r : rotatingUI) {
                    if(r.rotation == 0 && gs.previousState != GameState.State.EDITABLE_LEVEL){
                        r.scale = 1f;
                        r.offsetRotation(m.getRotation());
                        r.scaleOffset(0.325f*0.6f + 0.675f*m.getScale());
                        r.render(g);
                    }else {
                        r.color.a = 1f - (m.getScale() - 0.6f) * 2.5f;
                        r.scale = m.getScale() / 0.6f;
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

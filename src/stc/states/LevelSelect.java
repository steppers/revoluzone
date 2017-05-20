package stc.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import stc.GameState;
import stc.Model;
import stc.TransitionManager;
import stc.UI.TextLabel;
import stc.UI.TextRenderer;

import java.util.ArrayList;

/**
 * Created by Ollie on 20/05/2017.
 */
public class LevelSelect {

    private GameState gs;
    private TransitionManager tm;
    private Model m;
    private TextRenderer tr;

    private ArrayList<TextLabel> labels;
    private TextLabel instructions;

    public LevelSelect(GameState gameState, TransitionManager tm) {
        gs = gameState;
        this.tm = tm;
        tr = gs.textRenderer;
        m = gs.m;

        //Labels
        labels = new ArrayList<>();
        TextLabel temp = new TextLabel(m.getProperty("name"));
        temp.anchor.set(0.5f, 0.5f);
        temp.offset.set(0f, -0.46f);
        labels.add(temp.clone());
        temp.text = m.getProperty("prev").split("\\.")[0];
        temp.rotation = 90f;
        labels.add(temp.clone());
        temp.text = m.getProperty("next").split("\\.")[0];
        temp.rotation = -90f;
        labels.add(temp.clone());

        instructions = new TextLabel("Arrow keys to choose level\nEnter to play, Esc to go back");
        instructions.anchor.set(0.5f, 0.5f);
        instructions.offset.set(0f, 0.7f);
        instructions.color = Color.green.darker(0.4f);
    }

    public void update(GameContainer gc) {
        m = gs.m;
        if(gc.getInput().isKeyDown(Input.KEY_ENTER)) {
            tm.transitionGrow(m, GameState.State.LEVEL, 1.0f, 0.3f);
        }
        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            tm.transitionFade(m, new Model("0.txt", 0.6f, 0f), GameState.State.MENU, 0.4f);
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
    }

    public void render(GameContainer gc, Graphics g) {
        gs.m.render(gc, g);
        renderText(g, gs.m);
    }

    public void renderText(Graphics g, Model m) {
        labels.get(0).text = m.getProperty("name");
        labels.get(1).text = m.getProperty("prev").split("\\.")[0];
        labels.get(2).text = m.getProperty("next").split("\\.")[0];
        if(gs.currentState == GameState.State.TRANSITION) {
            if(tm.getNewState() == GameState.State.LEVEL || gs.previousState == GameState.State.LEVEL) {
                for(TextLabel l : labels) {
                    l.color.a = 1f-(m.getScale()-0.6f)*2f;
                    l.scale = m.getScale()/0.6f;
                    l.offsetRotation(m.getRotation());
                    l.scaleOffset(m.getScale());
                    tr.renderText(g, l);
                }
            } else {
                for (TextLabel l : labels) {
                    l.color.a = m.getOpacity();
                    l.scale = m.getScale()/0.6f;
                    l.offsetRotation(m.getRotation());
                    l.scaleOffset(m.getScale());
                    tr.renderText(g, l);
                }
            }
        } else {
            for (TextLabel l : labels) {
                l.color.a = m.getOpacity();
                l.scale = m.getScale()/0.6f;
                l.offsetRotation(m.getRotation());
                l.scaleOffset(m.getScale());
                tr.renderText(g, l);
            }
        }
        instructions.scale = m.getScale()/0.6f;
        instructions.scaleOffset(m.getScale());
        instructions.color.a = m.getOpacity();
        tr.renderText(g, instructions);
    }

}

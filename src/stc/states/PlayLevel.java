package stc.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import stc.GameState;
import stc.Model;
import stc.Tile;
import stc.Slider;
import stc.TransitionManager;
import stc.UI.TextLabel;
import stc.UI.TextRenderer;

import java.util.ArrayList;

/**
 * Created by Ollie on 20/05/2017.
 */
public class PlayLevel {

    private GameState gs;
    private TransitionManager tm;
    private Model m;
    private TextRenderer tr;

    private ArrayList<TextLabel> labels;
    private TextLabel messageLeft;
    private TextLabel messageRight;

    public PlayLevel(GameState gameState, TransitionManager tm) {
        gs = gameState;
        this.tm = tm;
        tr = gs.textRenderer;
        m = gs.m;

        //Labels
        labels = new ArrayList<>();
        TextLabel temp = new TextLabel("Best move count: " + m.getProperty("score"));
        temp.anchor.set(0.5f, 0.5f);
        temp.offset.set(0f, -0.4f);
        temp.scale = m.getScale()/0.6f;
        labels.add(temp.clone());
        temp.text = "Your move count: " + m.score;
        temp.rotation = 180f;
        labels.add(temp.clone());

        messageLeft = new TextLabel();
        String msg = m.getProperty("message_left");
        if(msg != null)
            messageLeft.text = msg;
        messageLeft.anchor.set(0.0f, 0.5f);
        messageLeft.offset.set(0.11f, 0.0f);
        messageLeft.color = Color.green.darker(0.4f);

        messageRight = new TextLabel();
        msg = m.getProperty("message_right");
        if(msg != null)
            messageRight.text = msg;
        messageRight.anchor.set(1.0f, 0.5f);
        messageRight.offset.set(-0.11f, 0.0f);
        messageRight.color = Color.green.darker(0.4f);
    }

    public void update(GameContainer gc) {
        m = gs.m;
        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            tm.transitionShrink(m, GameState.State.LEVEL_SELECT, 0.6f, 0.3f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            m.score += 1;
            tm.transitionRotate(m, gs.currentState, 90, 0.2f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            m.score += 1;
            tm.transitionRotate(m, gs.currentState, -90, 0.2f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            if(m.getTileUnderBall().type != Tile.Type.BLUE && m.getTileUnderBall().type != Tile.Type.RED)
                m.toggleRedBlue();
        }
        if(m.hasCompleted()) {
            if(m.score < Integer.parseInt(m.getProperty("score"))) {
                m.setProperty("score", String.valueOf(m.score)); //Could be saved to file too
            }
            tm.transitionFadeRotate(m, new Model(m.getProperty("next"), 1.0f, 0f), GameState.State.LEVEL, 90, 0.3f);
        }
        Tile t = m.getTileUnderBall();
        t.activate();
        for(Slider s: m.sliders){
            Tile ts = m.getTileUnderSlider(s);
            ts.activate();
        }
    }

    public void render(GameContainer gc, Graphics g) {
        gs.m.render(gc, g);
        renderText(g, gs.m);
    }

    public void renderText(Graphics g, Model m) {
        labels.get(0).text = "Best move count: " + m.getProperty("score");
        labels.get(1).text = "Your move count: " + m.score;
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

        String msg = m.getProperty("message_left");
        if(msg != null)
            messageLeft.text = msg;
        else
            messageLeft.text = "";
        messageLeft.scale = m.getScale();
        messageLeft.scaleOffset(m.getScale());
        messageLeft.color.a = (m.getScale()-0.6f)*2f;
        tr.renderText(g, messageLeft);

        msg = m.getProperty("message_right");
        if(msg != null)
            messageRight.text = msg;
        else
            messageRight.text = "";
        messageRight.scale = m.getScale();
        messageRight.scaleOffset(m.getScale());
        messageRight.color.a = (m.getScale()-0.6f)*2f;
        tr.renderText(g, messageRight);
    }

}

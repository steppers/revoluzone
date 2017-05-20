package proto.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import proto.GameState;
import proto.Model;
import proto.Tile;
import proto.TransitionManager;
import proto.UI.TextLabel;
import proto.UI.TextRenderer;

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
    }

}

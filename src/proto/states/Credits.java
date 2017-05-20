package proto.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import proto.GameState;
import proto.Model;
import proto.TransitionManager;
import proto.UI.TextLabel;
import proto.UI.TextRenderer;

import java.util.ArrayList;

/**
 * Created by Ollie on 19/05/2017.
 */
public class Credits {

    private GameState gs;
    private TransitionManager tm;
    private Model m;
    private TextRenderer tr;

    private ArrayList<TextLabel> labels;

    public Credits(GameState gameState, TransitionManager tm) {
        gs = gameState;
        this.tm = tm;
        tr = gs.textRenderer;

        //Labels
        labels = new ArrayList<>();
        TextLabel temp = new TextLabel("Oliver Steptoe");
        temp.anchor.set(0.5f, 0.5f);
        temp.offset.set(0f, -0.46f);
        labels.add(temp.clone());
        temp.text = "Alistair Brewin";
        temp.rotation = -90f;
        labels.add(temp.clone());
        temp.text = "Anton Nikitin";
        temp.rotation = 90f;
        labels.add(temp.clone());
    }

    public void update(GameContainer gc) {
        m = gs.m;
        if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            tm.transitionRotate(m, gs.currentState, 90, 0.2f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            tm.transitionRotate(m, gs.currentState, -90, 0.2f);
        }
        if(gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
            m.toggleRedBlue();
        }
        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            tm.transitionFade(m, new Model("0.txt", 0.6f, 0f), GameState.State.MENU, 0.4f);
        }
    }

    public void render(GameContainer gc, Graphics g) {
        gs.m.render(gc, g);
        renderText(g, gs.m);
    }

    public void renderText(Graphics g, Model m) {
        for(TextLabel l : labels) {
            l.color.a = m.getOpacity();
            l.scale = m.getScale()/0.6f;
            l.offsetRotation(m.getRotation());
            l.scaleOffset(m.getScale());
            tr.renderText(g, l);
        }
    }

}

package stc.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import stc.GameState;
import stc.Model;
import stc.TransitionManager;
import stc.UI.TextLabel;
import stc.UI.TextRenderer;
import stc.UI.proto.UILabel;
import stc.UI.proto.UIRenderable;

import java.util.ArrayList;

/**
 * Created by Ollie on 19/05/2017.
 */
public class Credits {

    private GameState gs;
    private TransitionManager tm;
    private Model m;

    private ArrayList<UIRenderable> rotatingUI;

    public Credits(GameState gameState, TransitionManager tm, GameContainer gc) {
        gs = gameState;
        this.tm = tm;
        this.m = gs.m;

        //Rotating UI
        rotatingUI = new ArrayList<>();
        UILabel tmpLabel = new UILabel("Oliver Steptoe", gc);
        tmpLabel.anchor.set(0.5f, 0.5f);
        tmpLabel.offset.set(0f, -0.46f);
        tmpLabel.scale = m.getScale()/0.6f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = "Alistair Brewin";
        tmpLabel.rotation = -90f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = "Anton Nikitin";
        tmpLabel.rotation = 180f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = "Many thanks to\n" +
                "Southampton Game Jam\n" +
                "2017";
        tmpLabel.rotation = 90f;
        tmpLabel.offset.set(0f, -0.5f);
        rotatingUI.add(tmpLabel.clone());
    }

    public void update(GameContainer gc, float delta) {
        m = gs.m;
        m.setRotation(m.getRotation() + 35f * delta);
        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            tm.transitionFade(m, new Model(m.getProperty("filename"), 0.6f, 0.3f), GameState.State.MENU, 0.4f);
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
        for(UIRenderable r : rotatingUI) {
            r.color.a = m.getOpacity();
            r.scale = m.getScale()/0.6f;
            r.offsetRotation(m.getRotation());
            r.scaleOffset(m.getScale());
            r.render(g);
        }
    }

}

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
 * Created by Ollie on 19/05/2017.
 */
public class Credits {

    private GameState gs;
    private TransitionManager tm;
    private Model m;

    private ArrayList<UIRenderable> rotatingUI;
    private ArrayList<UIRenderable> staticUI;

    private String[] names = {"Oliver Steptoe", "Alistair Brewin", "Anton Nikitin", "Daniel Bradley", "Many thanks to Southampton Game Jam 2017"};

    public Credits(GameState gameState, TransitionManager tm, GameContainer gc) {
        gs = gameState;
        this.tm = tm;
        this.m = gs.m;

        //Static UI
        staticUI = new ArrayList<>();
        UILabel tmpLabel = new UILabel(gc);
        tmpLabel.text = names[4];
        tmpLabel.anchor.set(0.5f, 0.5f);
        tmpLabel.offset.set(0f, 0.7f);
        tmpLabel.color = Color.green.darker(0.4f);
        staticUI.add(tmpLabel.clone());


        //Rotating UI
        rotatingUI = new ArrayList<>();
        tmpLabel.color = Color.white;
        tmpLabel.text = names[0];
        tmpLabel.anchor.set(0.5f, 0.5f);
        tmpLabel.offset.set(0f, -0.46f);
        tmpLabel.scale = m.getScale()/0.6f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = names[1];
        tmpLabel.rotation = -90f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = names[2];
        tmpLabel.rotation = 180f;
        rotatingUI.add(tmpLabel.clone());
        tmpLabel.text = names[3];
        tmpLabel.rotation = 90f;
        tmpLabel.offset.set(0f, -0.5f);
        rotatingUI.add(tmpLabel.clone());
    }

    public void update(GameContainer gc) {
        m = gs.m;
        if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) {
            tm.transitionFade(m, new Model(m.getProperty("filename"), 0.6f, 0.3f), GameState.State.MENU, 0.4f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            tm.transitionRotate(m, gs.currentState, 90, 0.2f);
        }
        else if(gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            tm.transitionRotate(m, gs.currentState, -90, 0.2f);
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
        for(UIRenderable r : staticUI) {
            r.color.a = m.getOpacity();
            r.scale = m.getScale()/0.6f;
            r.scaleOffset(m.getScale());
            r.render(g);
        }
    }

}

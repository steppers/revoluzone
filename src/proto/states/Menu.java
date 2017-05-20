package proto.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import proto.*;
import proto.UI.TextLabel;
import proto.UI.TextRenderer;

import java.util.ArrayList;

/**
 * Created by Ollie on 19/05/2017.
 */
public class Menu {
    private GameState gs;
    private TransitionManager tm;
    private Model m;
    private TextRenderer tr;

    private ArrayList<TextLabel> labels;
    private TextLabel instructions;

    public Menu(GameState gameState, TransitionManager tm) {
        gs = gameState;
        this.tm = tm;
        tr = gs.textRenderer;

        //Labels
        labels = new ArrayList<>();
        TextLabel temp = new TextLabel("Level Select");
        temp.anchor.set(0.5f, 0.5f);
        temp.offset.set(0f, -0.46f);
        labels.add(temp.clone());
        temp.text = "Quit";
        temp.rotation = -90f;
        labels.add(temp.clone());
        temp.text = "Editor";
        temp.rotation = -180f;
        labels.add(temp.clone());
        temp.text = "Credits";
        temp.rotation = 90f;
        labels.add(temp.clone());

        instructions = new TextLabel("Press Space to toggle blocks\nArrows to rotate, Enter & Esc to navigate menus");
        instructions.anchor.set(0.5f, 0.5f);
        instructions.offset.set(0f, 0.7f);
        instructions.color = Color.green.darker(0.4f);
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
                    tm.transitionFade(m, new Model(m.getProperty("name")+".txt", 0.6f, 0.3f), GameState.State.CREDITS, 0.4f);
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
            m.toggleRedBlue();
        }
    }

    public void render(GameContainer gc, Graphics g) {
        gs.m.render(gc, g);
        renderText(g, gs.m);
    }

    public void renderText(Graphics g, Model m) {
        if(gs.currentState == GameState.State.TRANSITION) {
            if(tm.getNewState() == GameState.State.EDITOR || gs.previousState == GameState.State.EDITOR) {
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

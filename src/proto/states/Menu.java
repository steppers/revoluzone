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
    private TextLabel instructions1;
    private TextLabel instructions2;

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

        instructions1 = new TextLabel("Press Space to toggle blocks");
        instructions1.anchor.set(0.5f, 0.5f);
        instructions1.offset.set(0f, 0.70f);
        instructions1.color = Color.green.darker(0.4f);
        instructions2 = instructions1.clone();
        instructions2.offset.set(0f, 0.84f);
        instructions2.text = "Arrows to rotate, Enter & Esc to navigate menus";
    }

    public void update(GameContainer gc) {
        m = gs.m;
        if(gc.getInput().isKeyDown(Input.KEY_ENTER)) {
            int r = (int)m.getRotation();
            while(r < 0)
                r += 360;
            switch(r % 360) {
                case 0:
                    tm.transitionFade(m, new Model("Level 1.txt", 0.5f, 0f), GameState.State.LEVEL_SELECT, 0.6f);
                    break;
                case 90:
                    System.exit(-1);
                    break;
                case 180:
                    tm.transitionGrow(m, GameState.State.EDITOR, 1.0f, 0.3f);
                    break;
                case 270:
                    tm.transitionFade(m, new Model(m.getProperty("name")+".txt", 0.5f, 1f), GameState.State.CREDITS, 1f);
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
        for(TextLabel l : labels) {
            l.color.a = m.getOpacity();
            l.offsetRotation(m.getRotation());
            l.scaleOffset(m.getScale());
            tr.renderText(g, l);
        }
        instructions1.scaleOffset(m.getScale());
        tr.renderText(g, instructions1);
        instructions2.scaleOffset(m.getScale());
        tr.renderText(g, instructions2);

//        //                renderText(gc, g, m.getOpacity(), m.getScale(), "Squaring the Circle", 0, -135, 0.8f, m);
//        renderText(gc, g, m.getOpacity(), m.getScale(), "Level Select", 0, -graphics.FontLoader.getFontSize()*3, 0.6f*((float)gc.getHeight()/1440), m);
//        renderText(gc, g, m.getOpacity(), m.getScale(), "Credits", 90, -graphics.FontLoader.getFontSize()*2, 0.6f*((float)gc.getHeight()/1440), m);
//        renderText(gc, g, m.getOpacity(), m.getScale(), "Quit", -90, -graphics.FontLoader.getFontSize(), 0.6f*((float)gc.getHeight()/1440), m);
//        renderText(gc, g, m.getOpacity(), m.getScale(), "Editor", 180, -graphics.FontLoader.getFontSize()*(float)(5/4), 0.6f*((float)gc.getHeight()/1440), m);
//        renderText(gc, g, m.getOpacity(), m.getScale(), "Use arrow", 0, -graphics.FontLoader.getFontSize()*2.5f, 0.16f*((float)gc.getHeight()/1440), m);
//        renderText(gc, g, m.getOpacity(), m.getScale(), "keys to turn", 0, -graphics.FontLoader.getFontSize()*3, 0.10f*((float)gc.getHeight()/1440), m);
//        renderText(gc, g, m.getOpacity(), m.getScale(), "Use space to", 0, -graphics.FontLoader.getFontSize()*3, 0.0f*((float)gc.getHeight()/1440), m);
//        renderText(gc, g, m.getOpacity(), m.getScale(), "toggle", 0, -graphics.FontLoader.getFontSize()*2.5f, -0.06f*((float)gc.getHeight()/1440), m);
    }
}

package editor;

import filemanage.Parser;
import graphics.FontLoader;
import graphics.StateRenderer;
import model.Tile;
import model.WorldModel;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import states.World;

/**
 * Created by an6g15 on 11/02/2017.
 */
public class Editor {

    private StateRenderer renderer;
    private WorldModel state;

    private Input currentInput;
    private Parser parser;


    public Editor(){
        state = new WorldModel();
    }

    public void update(GameContainer gc, float delta) {
        state.update(delta);

        if(gc.getInput().isButton1Pressed(Input.MOUSE_RIGHT_BUTTON)){
            int x = gc.getInput().getMouseX();
            int y = gc.getInput().getMouseY();
        }

    }

    public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics graphics) {
        graphics.setFont(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString()));
        graphics.drawString("Welcome to the editor :)", 70, 50);

    }

    public WorldModel getState(){
        return state;
    }

}

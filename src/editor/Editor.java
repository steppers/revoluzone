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

    }

    public void render(GameContainer gc, Graphics graphics) {
//        graphics.setFont(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString()));
    }

    public WorldModel getState(){
        return state;
    }

}

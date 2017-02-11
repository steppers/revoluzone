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

/**
 * Created by an6g15 on 11/02/2017.
 */
public class Editor {

    private StateRenderer renderer;
    private WorldModel state;
    private boolean inputEnabled;
    private String typeString = "";

    private Input currentInput;
    private Parser parser;
    Tile grabbed;
    Tile.Type type;

    public Editor(StateRenderer r){
        renderer = r;
        state = new WorldModel();
        state.recalcBall();
    }

    public void update(GameContainer gc, float delta) {
        state.update(delta);

        if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON )){
            if(((grabbed = getTileFromMousePos(gc)) != null) && grabbed.getType() == Tile.Type.EMPTY){
                switch (type){
                    case RED:
                        grabbed.setType(type);
                        state.recalcBall();
                        break;
                    case BLUE:
                        grabbed.setType(type);
                        state.recalcBall();
                        break;
                    case FINISH:
                        grabbed.setType(type);
                        state.recalcBall();
                        break;
                }
            }
        }

        if(gc.getInput().isKeyPressed(Input.KEY_R)){
            type = Tile.Type.RED;
            typeString = Tile.Type.RED.toString();
        }
        if(gc.getInput().isKeyPressed(Input.KEY_B)){
            type = Tile.Type.BLUE;
            typeString = Tile.Type.BLUE.toString();
        }
        if(gc.getInput().isKeyPressed(Input.KEY_F)){
            type = Tile.Type.FINISH;
            typeString = Tile.Type.FINISH.toString();
        }
    }

    public void render(GameContainer gc, StateBasedGame stateBasedGame, Graphics graphics) {
        graphics.setFont(FontLoader.getFont(FontLoader.Fonts.PixelGame.toString()));
        graphics.drawString("Welcome to the editor :)", 70, 50);
        graphics.drawString("Current Tool Selected: ", 70, 200);
        graphics.drawString(typeString, 70, 275);

        Integer x = gc.getInput().getMouseX();
        Integer y = gc.getInput().getMouseY();

        graphics.drawString("X: " + x.toString(), 70, 100);
        graphics.drawString("Y: " + y.toString(), 70, 150);

    }

    public WorldModel getState(){
        return state;
    }

    private Tile getTileFromMousePos(GameContainer gc) {
        float SCALE = ((Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / state.GRID_SIZE) * renderer.getScale();

        Tile[][] grid = state.getGrid();
        float offset = - (state.getGridSize() / 2);

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);

        Vector2f mouse = new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY());
        mouse = mouse.sub(screenOffset);
        mouse = mouse.scale(1/SCALE);
        mouse = mouse.sub(state.getRotation());
        mouse = mouse.sub(new Vector2f(offset, offset));

        int x, y;
        x = (int)mouse.x;
        y = (int)mouse.y;

        if(x >= 0 && x < state.getGridSize() && y >= 0 && y < state.getGridSize()) {
            return grid[x][y];
        }
        return null;
    }
}

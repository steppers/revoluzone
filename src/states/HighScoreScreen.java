package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by an6g15 on 10/02/2017.
 */
public class HighScoreScreen extends BasicGameState{

    private Integer id;

    public HighScoreScreen(Integer id){
        this.id = id;
    }
    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawString("High Score Screen !", gameContainer.getScreenWidth() / 2, 50);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {

    }
}

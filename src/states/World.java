package states;

import Listeners.ScoreListener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by an6g15 on 10/02/2017.
 */
public class World extends BasicGameState {

    private Integer stateId;
    private Integer levelId;
    private Integer highScore;
    private boolean isFinished;
    private static ScoreListener listener;

    public World(Integer stateId){
        this.stateId = stateId;
    }

    public static void setListener(ScoreListener listenerPassed){
        listener = listenerPassed;
    }

    @Override
    public int getID() {
        return this.stateId;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        if(isFinished){
            listener.levelComplete(levelId, highScore);
        }
    }
}

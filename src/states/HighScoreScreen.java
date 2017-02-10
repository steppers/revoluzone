package states;

import Listeners.ScoreListener;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Hashtable;

/**
 * Created by an6g15 on 10/02/2017.
 */
public class HighScoreScreen extends BasicGameState implements ScoreListener{

    private Integer id;
    Hashtable<Integer, Integer> highScores;
    StringBuilder highScoreBuilder;
    static HighScoreScreen instance;

    public static HighScoreScreen getInstance(Integer id){
        if(instance != null) {
            return instance;
        }else{
            instance = new HighScoreScreen(id);
            World.setListener(instance);
            return instance;
        }
    }

    private HighScoreScreen(Integer id){
        highScores = new Hashtable<>();
        this.id = id;
        highScoreBuilder = new StringBuilder();
    }
    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawString("High Score Screen !", gameContainer.getScreenWidth() / 2, 50);
        for(int i : highScores.keySet()){
            graphics.drawString("Score for level: " + i + " ---- " + highScores.get(i), (gameContainer.getScreenWidth() / 2) - 32, 50 + 20*i);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
    }

    @Override
    public void levelComplete(int turnsTaken, int levelId) {
        if(highScores.containsKey(levelId)) {
            highScores.replace(levelId, highScores.get(levelId), turnsTaken);
        }else{
            highScores.put(levelId, turnsTaken);
        }
    }
}

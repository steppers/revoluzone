package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import states.HighScoreScreen;
import states.Menu;
import states.World;

public class StateManager extends StateBasedGame {

    public static enum StateId {
        MENU,
        WORLD,
        HIGHSCORE
    };

    public StateManager(String gameName){
        super(gameName);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.addState(new Menu(StateId.MENU.ordinal()));
        this.addState(new World(StateId.WORLD.ordinal()));
        this.addState(new HighScoreScreen(StateId.HIGHSCORE.ordinal()));
        this.enterState(StateId.HIGHSCORE.ordinal());
    }
}

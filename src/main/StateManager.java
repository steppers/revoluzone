package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import states.World;

public class StateManager extends StateBasedGame {
    public enum StateId {
        MENU,
        WORLD,
        HIGHSCORE
    };

    public StateManager(String gameName){
        super(gameName);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.addState(new World(StateId.WORLD.ordinal()));
        this.enterState(StateId.WORLD.ordinal());
    }
}

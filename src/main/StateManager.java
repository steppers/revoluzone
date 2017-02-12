package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import proto.GameState;
import states.World;

public class StateManager extends StateBasedGame {

    public StateManager(String gameName){
        super(gameName);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.addState(new GameState());
        this.enterState(0);
    }
}

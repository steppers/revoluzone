package main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import states.Menu;
import states.World;

public class StateManager extends StateBasedGame {

    private final Integer MENU_ID = 0;
    private final Integer WORLD_ID = 1;

    public StateManager(String gameName){
        super(gameName);
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.addState(new Menu(MENU_ID));
        this.addState(new World(WORLD_ID));
        this.enterState(MENU_ID);
    }
}

package com.stc.core.states;
import com.stc.core.*;
import com.stc.core.levels.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.stc.core.ui.*;

public class GameState extends State implements InputProcessor
{
	private UIButton leftButton;
	private UIButton rightButton;
	private UIButton selectButton;
	
	private enum MenuState {
		MAIN_MENU,
		CREDITS,
		OPTIONS,
		LEVEL_SELECT,
		PLAY
	}
	
	private World world, worldTo;
	private LevelInstance level, levelTo;
	private MenuState state, stateTo;
	private boolean transitioning;
	
	public GameState() {
		world = new World();
		worldTo = new World();
        level = LevelManager.instance().getLevelInstance("test");
		Gdx.input.setInputProcessor(this);
		
		// Scale world in
		world.setScale(0.0f);
		world.setupScaleLerp(0.0f, 1.0f, 0.8f);
		
		// Buttons
		float buttonSize = Gdx.graphics.getWidth()/5;
		leftButton = new UIButton(buttonSize/2 + 30, buttonSize/2 + 30, buttonSize, buttonSize, "rotate_left.png");
		rightButton = new UIButton(Gdx.graphics.getWidth() - buttonSize/2 -30, buttonSize/2 + 30, buttonSize, buttonSize, "rotate_right.png");
		selectButton = new UIButton(Gdx.graphics.getWidth()/2, buttonSize/2 + 30, buttonSize, buttonSize, "select.png");
		
		// Initial state
		this.state = MenuState.MAIN_MENU;
		transitioning = false;
	}
	
	@Override
    public void update(float delta) {
        world.update(delta);
		if(transitioning && !world.changing()) {
			transitioning = false;
			World tmp = world;
			world = worldTo;
			worldTo = tmp;
			state = stateTo;
			level = levelTo;
		}
		if(transitioning) {
			worldTo.update(delta);
		}
    }

    @Override
    public void render() {
		level.render(world);
		renderStrings(world, state);
		
		if(transitioning) {
			levelTo.render(worldTo);
			renderStrings(worldTo, stateTo);
		}
		
		leftButton.render(world);
		rightButton.render(world);
		selectButton.render(world);
    }
	
	private void renderStrings(World inWorld, MenuState state) {
		switch(state) {
			case MAIN_MENU:
				inWorld.drawString(0, 1.2f, "Levels", 0);
				inWorld.drawString(0, 1.2f, "Credits", -90);
				inWorld.drawString(0, 1.2f, "Quit", 90);
				inWorld.drawString(0, 1.2f, "Achievements", 180);
				break;
			case CREDITS:
				inWorld.drawString(0, 1.2f, "Oliver Steptoe", 0);
				inWorld.drawString(0, 1.2f, "Ali Brewin", -90);
				inWorld.drawString(0, 1.2f, "Daniel Bradley", 90);
				inWorld.drawString(0, 1.2f, "Anton Nikitin", 180);
				break;
		}
	}
	
	@Override
	public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}
	
	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		return false;
	}
	
	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		return false;
	}
	
	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		screenY = Gdx.graphics.getHeight() - screenY;
		
		if (button != Input.Buttons.LEFT || pointer > 0 || world.changing()) return false;
		
		if(leftButton.contains(screenX, screenY))
			world.rotate(90, 0.3f);
		else if(rightButton.contains(screenX, screenY))
			world.rotate(-90, 0.3f);
		else if(selectButton.contains(screenX, screenY)) {
			int rotation = (int)world.getRotation();
			switch(state) {
				case MAIN_MENU:
					switch(rotation) {
						case 90:
							levelTo = level;
							stateTo = MenuState.CREDITS;
							worldTo.setRotation(rotation);
							worldTo.setupTextOpacityLerp(0.0f, 1.0f, 0.7f);
							world.setupTextOpacityLerp(1.0f, 0.0f, 0.7f);
							transitioning = true;
					}
					break;
				case CREDITS:
					levelTo = level;
					stateTo = MenuState.MAIN_MENU;
					worldTo.setupTextOpacityLerp(0.0f, 1.0f, 0.7f);
					world.setupTextOpacityLerp(1.0f, 0.0f, 0.7f);
					transitioning = true;
					break;
			}
		}
			
		return true;
	}
	
	@Override
	public boolean keyDown (int keycode) {
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		return false;
	}

	@Override
	public boolean scrolled (int amount) {
		return false;
	}
}

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
	private UIButton backButton;
	private UIButton toggleButton;
	
	private Interpolator selectButtonLerp = new Interpolator(1.0f, 1.0f, 0.0f);
	private Interpolator backButtonLerp = new Interpolator(0.0f, 0.0f, 0.0f);
	
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
	private boolean rotating;
	
	public GameState() {
		world = new World();
		worldTo = new World();
        level = LevelManager.instance().getLevelInstance("Menu");
		Gdx.input.setInputProcessor(this);
		
		// Scale world in
		world.setScale(0.0f);
		world.setupScaleLerp(0.0f, Globals.SCALE_MENU, 0.4f);
		selectButtonLerp.begin(0.0f, 1.0f, 0.4f);
		worldTo.setScale(Globals.SCALE_MENU);
		level.triggerUpdate(0);
		
		// Buttons
		float buttonSize = Gdx.graphics.getWidth()/5;
		leftButton = new UIButton(buttonSize/2 + 30, buttonSize/2 + 30, buttonSize, buttonSize, "rotate_left.png");
		rightButton = new UIButton(Gdx.graphics.getWidth() - buttonSize/2 -30, buttonSize/2 + 30, buttonSize, buttonSize, "rotate_right.png");
		selectButton = new UIButton(Gdx.graphics.getWidth()/2, buttonSize*1.5f + 30, buttonSize, buttonSize, "select.png");
		backButton = new UIButton(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() - buttonSize/2 - 30, buttonSize, buttonSize, "select.png", 180);
		toggleButton = new UIButton(Gdx.graphics.getWidth()/2, buttonSize/2 + 30, buttonSize * 2, buttonSize, "toggle.png");
		
		// Initial state
		this.state = MenuState.MAIN_MENU;
		transitioning = false;
		rotating = false;
	}
	
	@Override
    public void update(float delta) {
		level.update(world, delta);
        world.update(delta);
		if(transitioning && !world.changing()) {
			transitioning = false;
			World tmp = world;
			world = worldTo;
			worldTo = tmp;
			state = stateTo;
			level = levelTo;
		}
		if(rotating && !world.changing()) {
			rotating = false;
			level.triggerUpdate((int)world.getRotation());
		}
		if(transitioning) {
			worldTo.update(delta);
		}
		
		selectButtonLerp.update(delta);
		backButtonLerp.update(delta);
		
		if(state == MenuState.PLAY && level.isComplete() && !transitioning) {
			levelTo = level.getNextLevel();
			stateTo = MenuState.PLAY;
			world.rotate(-90, Globals.SPEED_TRANSITION);
			world.setupOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
			world.setupTextOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
			worldTo.setupRotationLerp(90, 0, Globals.SPEED_TRANSITION);
			worldTo.setupOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
			worldTo.setupTextOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
			transitioning = true;
			rotating = true;
		}
    }

    @Override
    public void render() {
		level.render(world);
		renderStrings(world, state, level);
		
		if(transitioning) {
			levelTo.render(worldTo);
			renderStrings(worldTo, stateTo, levelTo);
		}
		
		leftButton.render(world.getScale());
		rightButton.render(world.getScale());
		toggleButton.render(world.getScale());
		
		selectButton.render(selectButtonLerp.lerp());
		backButton.render(backButtonLerp.lerp());
    }
	
	private void renderStrings(World inWorld, MenuState state, LevelInstance inLevel) {
		switch(state) {
			case MAIN_MENU:
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Levels", 0);
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Credits", -90);
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Quit", 90);
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Achievements", 180);
				break;
			case CREDITS:
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Oliver Steptoe", -90);
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Ali Brewin", 0);
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Daniel Bradley", 90);
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Anton Nikitin", 180);
				break;
			case LEVEL_SELECT:
				inWorld.drawString(0, Globals.TEXT_OFFSET, inLevel.getPrevLevelName(), 90);
				inWorld.drawString(0, Globals.TEXT_OFFSET, inLevel.getLevelName(), 0);
				inWorld.drawString(0, Globals.TEXT_OFFSET, inLevel.getNextLevelName(), -90);
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
		
		if (button != Input.Buttons.LEFT || pointer > 0 || world.changing() || level.isUpdating()) return false;
		
		int rotation = (int)world.getRotation();
		
		if(leftButton.contains(screenX, screenY)) {
			if(state == MenuState.LEVEL_SELECT) {
				levelTo = LevelManager.instance().getLevelInstance(level.getNextLevelName());
				stateTo = MenuState.LEVEL_SELECT;
				worldTo.setupOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
				worldTo.setupTextOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
				worldTo.setupRotationLerp(-90, 0, Globals.SPEED_TRANSITION);
				world.setupOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
				world.setupTextOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
				world.setupRotationLerp(0, 90, Globals.SPEED_TRANSITION);
				transitioning = true;
				rotating = true;
			} else {
				world.rotate(90, Globals.SPEED_ROTATION);
				rotating = true;
			}
		}
		else if(rightButton.contains(screenX, screenY)) {
			if(state == MenuState.LEVEL_SELECT) {
				levelTo = LevelManager.instance().getLevelInstance(level.getPrevLevelName());
				stateTo = MenuState.LEVEL_SELECT;
				worldTo.setupOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
				worldTo.setupTextOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
				worldTo.setupRotationLerp(90, 0, Globals.SPEED_TRANSITION);
				world.setupOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
				world.setupTextOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
				world.setupRotationLerp(0, -90, Globals.SPEED_TRANSITION);
				transitioning = true;
				rotating = true;
			} else {
				world.rotate(-90, Globals.SPEED_ROTATION);
				rotating = true;
			}
		}
		else if(selectButton.contains(screenX, screenY)) {
			switch(state) {
				case MAIN_MENU:
					switch(rotation) {
						case 0:
							levelTo = LevelManager.instance().getLevelInstance("Level 1");
							stateTo = MenuState.LEVEL_SELECT;
							worldTo.setRotation(rotation);
							worldTo.setupOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
							worldTo.setupTextOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
							worldTo.setScale(Globals.SCALE_MENU);
							backButtonLerp.begin(0.0f, 1.0f, Globals.SPEED_TRANSITION);
							world.setupTextOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
							transitioning = true;
							break;
						case 90:
							levelTo = level;
							stateTo = MenuState.CREDITS;
							worldTo.setOpacity(1.0f);
							worldTo.setScale(Globals.SCALE_MENU);
							worldTo.setRotation(rotation);
							worldTo.setupTextOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
							backButtonLerp.begin(0.0f, 1.0f, Globals.SPEED_TRANSITION);
							world.setupTextOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
							transitioning = true;
							break;
						case 270:
							Gdx.app.exit();
							break;
					}
					break;
				case LEVEL_SELECT:
					levelTo = level;
					stateTo = MenuState.PLAY;
					world.setupScaleLerp(Globals.SCALE_MENU, 1.0f, Globals.SPEED_TRANSITION);
					world.setupTextOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
					worldTo.setOpacity(1.0f);
					worldTo.setRotation(0);
					worldTo.setupScaleLerp(Globals.SCALE_MENU, 1.0f, Globals.SPEED_TRANSITION);
					worldTo.setupTextOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
					selectButtonLerp.begin(1.0f, 0.0f, Globals.SPEED_TRANSITION);
					transitioning = true;
					break;
			}
		}
		else if(backButton.contains(screenX, screenY)) {
			switch(state) {
				case CREDITS:
					levelTo = level;
					stateTo = MenuState.MAIN_MENU;
					worldTo.setRotation(rotation);
					worldTo.setupTextOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
					backButtonLerp.begin(1.0f, 0.0f, Globals.SPEED_TRANSITION);
					world.setupTextOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
					transitioning = true;
					break;
				case LEVEL_SELECT:
					levelTo = LevelManager.instance().getLevelInstance("Menu");
					stateTo = MenuState.MAIN_MENU;
					worldTo.setRotation(0);
					worldTo.setupOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
					worldTo.setScale(Globals.SCALE_MENU);
					worldTo.setupTextOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
					backButtonLerp.begin(1.0f, 0.0f, Globals.SPEED_TRANSITION);
					world.setupTextOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
					world.setupOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
					transitioning = true;
					break;
				case PLAY:
					levelTo = level;
					stateTo = MenuState.LEVEL_SELECT;
					world.setupScaleLerp(1.0f, Globals.SCALE_MENU, Globals.SPEED_TRANSITION);
					world.setupTextOpacityLerp(1.0f, 0.0f, Globals.SPEED_TRANSITION);
					worldTo.setupScaleLerp(1.0f, Globals.SCALE_MENU, Globals.SPEED_TRANSITION);
					worldTo.setupTextOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
					worldTo.setupOpacityLerp(0.0f, 1.0f, Globals.SPEED_TRANSITION);
					worldTo.setRotation(0);
					selectButtonLerp.begin(0.0f, 1.0f, Globals.SPEED_TRANSITION);
					transitioning = true;
					break;
			}
		}
		else if(toggleButton.contains(screenX, screenY)) {
			level.toggleRedBlue();
			level.triggerUpdate((int)world.getRotation());
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

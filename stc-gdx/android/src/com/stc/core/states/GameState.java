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
        level = LevelManager.instance().getLevelInstance("test");
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
		selectButton = new UIButton(Gdx.graphics.getWidth()/2, buttonSize/2 + 30, buttonSize, buttonSize, "select.png");
		backButton = new UIButton(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() - buttonSize/2 - 30, buttonSize, buttonSize, "select.png", 180);
		
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
    }

    @Override
    public void render() {
		level.render(world);
		renderStrings(world, state);
		
		if(transitioning) {
			levelTo.render(worldTo);
			renderStrings(worldTo, stateTo);
		}
		
		leftButton.render(world.getScale());
		rightButton.render(world.getScale());
		
		selectButton.render(selectButtonLerp.lerp());
		backButton.render(backButtonLerp.lerp());
    }
	
	private void renderStrings(World inWorld, MenuState state) {
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
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Level 1", -90);
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Level 2", 0);
				inWorld.drawString(0, Globals.TEXT_OFFSET, "Level 3", 90);
				//inWorld.drawString(0, Globals.TEXT_OFFSET, "", 180);
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
			world.rotate(90, Globals.SPEED_ROTATION);
			rotating = true;
		}
		else if(rightButton.contains(screenX, screenY)) {
			world.rotate(-90, Globals.SPEED_ROTATION);
			rotating = true;
		}
		else if(selectButton.contains(screenX, screenY)) {
			switch(state) {
				case MAIN_MENU:
					switch(rotation) {
						case 0:
							levelTo = level;
							stateTo = MenuState.LEVEL_SELECT;
							worldTo.setRotation(rotation);
							worldTo.setupTextOpacityLerp(0.0f, 1.0f, 0.7f);
							backButtonLerp.begin(0.0f, 1.0f, 0.7f);
							world.setupTextOpacityLerp(1.0f, 0.0f, 0.7f);
							transitioning = true;
							break;
						case 90:
							levelTo = level;
							stateTo = MenuState.CREDITS;
							worldTo.setRotation(rotation);
							worldTo.setupTextOpacityLerp(0.0f, 1.0f, 0.7f);
							backButtonLerp.begin(0.0f, 1.0f, 0.7f);
							world.setupTextOpacityLerp(1.0f, 0.0f, 0.7f);
							transitioning = true;
							break;
						case 270:
							Gdx.app.exit();
							break;
					}
					break;
			}
		}
		else if(backButton.contains(screenX, screenY)) {
			switch(state) {
				case CREDITS:
					levelTo = level;
					stateTo = MenuState.MAIN_MENU;
					worldTo.setRotation(rotation);
					worldTo.setupTextOpacityLerp(0.0f, 1.0f, 0.7f);
					backButtonLerp.begin(1.0f, 0.0f, 0.7f);
					world.setupTextOpacityLerp(1.0f, 0.0f, 0.7f);
					transitioning = true;
					break;
				case LEVEL_SELECT:
					levelTo = level;
					stateTo = MenuState.MAIN_MENU;
					worldTo.setRotation(rotation);
					worldTo.setupTextOpacityLerp(0.0f, 1.0f, 0.7f);
					backButtonLerp.begin(1.0f, 0.0f, 0.7f);
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

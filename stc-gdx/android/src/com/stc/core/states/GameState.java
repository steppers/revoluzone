package com.stc.core.states;
import com.stc.core.*;
import com.stc.core.levels.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.stc.core.ui.*;

public class GameState extends State implements InputProcessor
{
	
	private World world;
	private LevelInstance level;
	
	private UIButton leftButton;
	private UIButton rightButton;
	
	public GameState() {
		world = new World();
        level = LevelManager.instance().getLevelInstance("test");
		Gdx.input.setInputProcessor(this);
		
		// Scale world in
		world.setScale(0.0f);
		world.setupScaleLerp(0.0f, 1.0f, 1.0f);
		
		// Buttons
		leftButton = new UIButton(130, 130, 200, 200, "rotate_left.png");
		rightButton = new UIButton(Gdx.graphics.getWidth() - 130, 130, 200, 200, "rotate_right.png");
	}
	
	@Override
    public void update(float delta) {
        world.update(delta);
    }

    @Override
    public void render() {
		level.render(world);
		world.drawString(0, 1.2f, "Levels", 0);
		world.drawString(0, 1.2f, "Credits", -90);
		world.drawString(0, 1.2f, "Quit", 90);
		world.drawString(0, 1.2f, "Achievements", 180);
		
		leftButton.render(world);
		rightButton.render(world);
    }

	@Override
	public void renderText() {}
	
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

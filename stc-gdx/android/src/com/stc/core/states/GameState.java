package com.stc.core.states;
import com.stc.core.*;
import com.stc.core.levels.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;

public class GameState extends State implements InputProcessor
{
	
	private World world;
	private LevelInstance level;
	
	private Rectangle rotateLeftZone;
	private Rectangle rotateRightZone;
	private Rectangle selectZone;
	
	public GameState() {
		world = new World();
        level = LevelManager.instance().getLevelInstance("test");
		Gdx.input.setInputProcessor(this);
		
		world.setScale(0.0f);
		world.setupScaleLerp(0.0f, 1.0f, 1.0f);
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
		if (button != Input.Buttons.LEFT || pointer > 0 || world.changing()) return false;
		
		if(screenX > Gdx.graphics.getWidth()/2)
			world.rotate(-90, 0.3f);
			
		if(screenX < Gdx.graphics.getWidth()/2)
			world.rotate(90, 0.3f);
			
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

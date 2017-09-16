package com.stc.core.states;
import com.stc.core.*;
import com.stc.core.levels.*;
import com.badlogic.gdx.*;

public class GameState extends State implements InputProcessor
{
	
	private World world;
	private LevelInstance level;
	
	public GameState() {
		world = new World();
        level = LevelManager.instance().getLevelInstance("test");
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
    public void update(float delta) {
        world.update(delta);
    }

    @Override
    public void render() {
		level.render(world);
    }

	@Override
	public void renderText()
	{
		
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

package com.stc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.stc.states.Menu;

public class Main extends ApplicationAdapter {

	private Background background;
	private State state;
    private static ShapeRenderer shapeRenderer;
    public static Model model;
	
	@Override
	public void create () {
        shapeRenderer = new ShapeRenderer();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        background = new Background();
        state = new Menu();
        model = new Model();
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		//Updates
		background.update(delta);
		state.update(delta);

		//Render
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		background.render();
        state.render();
        shapeRenderer.end();
	}

	@Override
	public void resize (int width, int height) {
		Globals.display_width = width;
		Globals.display_height = height;
	}
	
	@Override
	public void dispose () {
		background.dispose();
        state.dispose();
	}

	public static ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }
}

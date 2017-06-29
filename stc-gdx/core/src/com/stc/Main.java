package com.stc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends ApplicationAdapter {

	private Background background;

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		background = new Background();

//		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		//Updates
		background.update(delta);

		//Render
		background.render();
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
	}

	@Override
	public void resize (int width, int height) {
		Globals.display_width = width;
		Globals.display_height = height;
	}
	
	@Override
	public void dispose () {
//		batch.dispose();
//		img.dispose();
	}
}

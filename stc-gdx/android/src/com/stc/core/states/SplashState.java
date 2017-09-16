package com.stc.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.stc.core.Renderer;
import com.stc.core.State;
import com.stc.core.*;

/**
 * Created by steppers on 8/4/17.
 */

public class SplashState extends State {

    final float fadeInDuration = 1f;
    final float logoDuration = 2f;
    final float fadeOutDuration = 0.5f;

    float elapsed = 0f;
    float alpha = 0f;

    private SpriteBatch sb;
    private BitmapFont font;
    private Color color;
    private GlyphLayout layout;

    public SplashState() {
        sb = Renderer.spriteBatch();
        font = Renderer.gameFont();
        color = Color.WHITE.cpy();
        layout = new GlyphLayout(font, "Squaring the Circle");
    }

    @Override
    public void update(float delta) {
        elapsed += delta;

        if(elapsed < fadeInDuration)
            alpha = elapsed / fadeInDuration;
        else if(elapsed < logoDuration + fadeInDuration)
            alpha = 1;
        else if(elapsed < fadeOutDuration + logoDuration + fadeInDuration)
            alpha = 1-((elapsed-fadeInDuration-logoDuration) / fadeOutDuration);
        else
            StateManager.transitionTo(new GameState());

        color.a = alpha;
        font.setColor(color);
    }

    @Override
    public void render() {
        
    }

	@Override
	public void renderText()
	{
		sb.begin();
        font.draw(sb, "Squaring the Circle", 0, (Gdx.graphics.getHeight()+layout.height)/2.0f, Gdx.graphics.getWidth(), Align.center, false);
        sb.end();

        // Fix the blend state back (sb.end() resets it)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
}

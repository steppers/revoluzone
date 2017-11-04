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
import com.badlogic.gdx.math.*;

/**
 * Created by steppers on 8/4/17.
 */

public class SplashState extends State {

    final float fadeInDuration = 0.4f;
    final float logoDuration = 1f;
    final float fadeOutDuration = 0.3f;

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
        layout = new GlyphLayout(font, "Squaring\nthe\nCircle");
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
        sb.begin();
		Matrix4 t = new Matrix4().idt();
		t.translate(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0.0f);
		t.scale(1.2f, 1.2f, 1.0f);
		sb.setTransformMatrix(t);
        font.draw(sb, "Squaring\nthe\nCircle", -layout.width/2.0f, layout.height/2.0f, layout.width, Align.center, false);
        sb.end();

        // Fix the blend state back (sb.end() resets it)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
}

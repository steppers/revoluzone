package com.stc.proto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by steppers on 8/2/17.
 */

public class Renderer {

    private static final String kGameFont = "PixelGameFont.ttf";

    private static ShapeRenderer sShapeRenderer;
    private static SpriteBatch sSpriteBatch;

    private static BitmapFont sFont48;

    public static void init() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Globals.display_width = Gdx.graphics.getWidth();
        Globals.display_height = Gdx.graphics.getHeight();

        sShapeRenderer = new ShapeRenderer();
        sSpriteBatch = new SpriteBatch();

        FreeTypeFontGenerator sFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + kGameFont));
        FreeTypeFontParameter sFontParameter = new FreeTypeFontParameter();

        sFontParameter.size = 48;
        sFont48 = sFontGenerator.generateFont(sFontParameter);
        sFont48.getData().scale(1.5f);

        sFontGenerator.dispose();
    }

    public static ShapeRenderer shapeRenderer() {
        return sShapeRenderer;
    }
    public static SpriteBatch spriteBatch() {
        return sSpriteBatch;
    }
    public static BitmapFont gameFont() {
        return sFont48;
    }

    public static void dispose() {
        sShapeRenderer.dispose();
        sSpriteBatch.dispose();
        sFont48.dispose();
    }

}

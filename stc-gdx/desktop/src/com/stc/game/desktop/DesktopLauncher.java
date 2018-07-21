package com.stc.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.stc.game.Globals;
import com.stc.game.RevGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Globals.DEFAULT_WIDTH_DESKTOP;
        config.height = Globals.DEFAULT_HEIGHT_DESKTOP;
        config.fullscreen = true;
		new LwjglApplication(new RevGame(), config);
	}
}

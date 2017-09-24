package com.stc.core;
import com.badlogic.gdx.graphics.*;

/**
 * Created by steppers on 6/29/17.
 */

public class Globals {

    public static final int DEFAULT_WIDTH_DESKTOP = 1920;
    public static final int DEFAULT_HEIGHT_DESKTOP = 960;
    public static float display_width, display_height;
	public static String orientation = "landscape";

	public static final String DIR_LEVEL_TMP = "user_levels/";
    public static final String LEVEL_DIR = "levels/";
	public static final String DIR_EXT_ROOT = "squaringthecircle/";
	
	public static final float SCALE_MENU = 0.8f;
	public static final float TEXT_OFFSET = 1.25f;
	
	public static final Color COLOR_TEXT = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	public static final Color COLOR_BUTTON_BG_UP = new Color(0.5f, 0.5f, 0.9f, 1.0f);
	public static final Color COLOR_BUTTON_BORDER = new Color(0.7f, 0.7f, 0.9f, 1.0f);
	
	public static final Color COLOR_FLOOR = new Color(0.7f, 0.7f, 0.7f, 1.0f);
	public static final Color COLOR_SHADOW = new Color(0.4f, 0.4f, 0.4f, 0.5f);
	public static final Color COLOR_WALL = new Color(0.9f, 0.9f, 0.9f, 1.0f);
	
	public static final Color COLOR_BALL = new Color(Color.CYAN);
	public static final Color COLOR_START_PAD_INNER = new Color(Color.GREEN);
	public static final Color COLOR_START_PAD_OUTER = new Color(Color.RED);
	public static final Color COLOR_FINISH_INNER = new Color(Color.BLACK);
	public static final Color COLOR_FINISH_OUTER = new Color(0.85f, 0.85f, 0.95f, 1.0f);
	public static final Color COLOR_RED_ACTIVE = new Color(1.0f, 0.2f, 0.2f, 1.0f);
	public static final Color COLOR_RED_INACTIVE = new Color(1.0f, 0.2f, 0.2f, 0.5f);
	public static final Color COLOR_BLUE_ACTIVE = new Color(0.2f, 0.2f, 1.0f, 1.0f);
	public static final Color COLOR_BLUE_INACTIVE = new Color(0.2f, 0.2f, 1.0f, 0.5f);
	public static final Color COLOR_SWITCH_INNER_INACTIVE = new Color(Color.RED);
	public static final Color COLOR_SWITCH_INNER_ACTIVE = new Color(Color.GREEN);
	public static final Color COLOR_SWITCH_OUTER = new Color(0.8f, 0.8f, 0.8f, 1.0f);

	public static final float G = 5.0f;
	
	public static final float SPEED_ROTATION = 0.2f;
	public static final float SPEED_TRANSITION = 0.4f;
	public static final float SHADOW_OFFSET = 0.07f;
	
	// Functions
	public static final Color getColor(Color c, float opacity) {
		Color nc = new Color(c);
		nc.a *= opacity;
		return c;
	}
	
}

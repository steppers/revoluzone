package com.stc.game.levels;

public enum LO_TYPE
{
	
	EMPTY("Empty", false),
	WALL("Wall", true),
	RED("Red", false),
	BLUE("Blue", false),
	START("Start", false),
	FINISH("Finish", false),
	SWITCH("Switch", false),
	LOCKED_FINISH("Locked Finish", true),
	RAIL("Rail", false),
	KILLZONE("Killzone", false),
	BALL("Ball", true),
	SLIDER("Slider", true),
	UNDEFINED("Undefined", false);

	String id;
	boolean solid;
	LO_TYPE(String id, boolean solid) {
		this.id = id;
		this.solid = solid;
	}

	public String getId() {
		return id;
	}

	public boolean isSolid() {
		return solid;
	}
	
}

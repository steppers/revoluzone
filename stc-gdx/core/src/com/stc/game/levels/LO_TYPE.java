package com.stc.game.levels;

public enum LO_TYPE
{
	
	EMPTY("Empty", false, true),
	WALL("Wall", true, true),
	RED("Red", false, true),
	BLUE("Blue", false, true),
	START("Start", false, true),
	FINISH("Finish", false, true),
	SWITCH("Switch", false, true),
	LOCKED_FINISH("Locked Finish", true, true),
	RAIL("Rail", false, true),
	KILLZONE("Killzone", false, true),
	BALL("Ball", true, false),
	SLIDER("Slider", true, false),
	TELEPORT("Teleporter", false, true),
	UNDEFINED("Undefined", false, true);

	String mId;
	boolean mSolid;
	boolean mStatic;

	LO_TYPE(String inId, boolean inSolid, boolean inStatic) {
		mId = inId;
		mSolid = inSolid;
		mStatic = inStatic;
	}

	public String getId() {
		return mId;
	}

	public boolean isSolid() {
		return mSolid;
	}

	public boolean isStatic() { return mStatic; }
	
}

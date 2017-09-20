package com.stc.core.levels;

public enum TileType
{
	EMPTY("Empty", false),
	WALL("Wall", true);
	
	String id;
	boolean solid;
	TileType(String id, boolean solid) {
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

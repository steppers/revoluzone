package com.stc.core.levels;

public enum TileType
{
	EMPTY("Empty"),
	WALL("Wall");
	
	String id;
	TileType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}

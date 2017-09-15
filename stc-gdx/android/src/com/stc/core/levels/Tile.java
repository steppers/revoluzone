package com.stc.core.levels;

public class Tile
{
	public enum Type {
		EMPTY("Empty"),
		WALL("Wall");
		
		String id;
		Type(String id) {
			this.id = id;
		}
		
		public String getId() {
			return id;
		}
	}
	
	private Type type;
	
	public Tile(Type type) {
		this.type = type;
	}
	
	private Type getType() {
		return type;
	}
}

package com.stc.core;

public class Interpolator
{
	
	private float duration;
	private float elapsed;
	
	private float from, to;
	private float current;
	
	public Interpolator(float from, float to, float duration) {
		elapsed = 0;
		this.duration = duration;
		this.from = from;
		this.to = to;
		this.current = from;
	}
	
	public void begin(float from, float to, float duration) {
		elapsed = 0;
		this.duration = duration;
		this.from = from;
		this.to = to;
		this.current = from;
	}
	
	public void update(float delta) {
		if (duration != 0) {
			elapsed += delta;
			float factor = elapsed / duration;
			current = (factor * (to - from)) + from;
		
			if(to > from) {
				if(current > to) {
					current = to;
					duration = 0;
				}
			} else {
				if(current < to) {
					current = to;
					duration = 0;
				}
			}
		}
	}
	
	public float lerp() {
		return current;
	}
	
	public boolean active() {
		return duration > 0;
	}
	
	public void clear(float value) {
		to = value;
		from = value;
		current = value;
	}
	
}

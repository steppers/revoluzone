package com.stc.proto;

/**
 * Created by steppers on 6/30/17.
 */

public abstract class State {

    protected float rotation = 0f;
    protected float textScale = 1f;
    protected float scale = 1f;

    public abstract void update(float delta);
    public abstract void render(float alpha, float scale);
    public abstract void renderText(float alpha, float scale);
    public abstract void dispose();

    public void setRotation(float rot) {
        rotation = rot;
    }

    public void rotate(float rot) {
        rotation += rot;
    }

    public float getRotation() {
        return rotation;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void scale(float scl, boolean clampToOne) {
        this.scale += scl;
        if(clampToOne) {
            this.scale = Math.min(this.scale, 1f);
            this.scale = Math.max(this.scale, -1f);
        }
    }

    public float getScale() {
        return scale;
    }

    public void setTextScale(float scale) {
        this.textScale = scale;
    }

    public void scaleText(float scl, boolean clampToOne) {
        this.textScale += scl;
        if(clampToOne) {
            this.textScale = Math.min(this.textScale, 1f);
            this.textScale = Math.max(this.textScale, -1f);
        }
    }

    public float getTextScale() {
        return textScale;
    }

}

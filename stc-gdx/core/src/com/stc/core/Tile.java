package com.stc;

/**
 * Created by steppers on 6/30/17.
 */

public class Tile {

    public enum Type {
        EMPTY("Empty"),
        FIXED("Fixed"),
        RED("Red"),
        BLUE("Blue"),
        START("Start"),
        FINISH("Finish"),
        SWITCH("Switch"),
        LOCKED_FINISH("Locked Finish"),
        KILL("Kill"),
        RED_FINISH("Red Finish"),
        BLUE_FINISH("Blue Finish"),
        TELEPORT("Teleport"),
        SLIDER("Slider"),
        RAIL("Rail");

        String name;

        Type(String name){
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

    public Type type = Type.FIXED;
    public Type resetType = Type.FIXED;

    public Tile(int type) {
        this.type = Type.values()[type];
        this.resetType = this.type;
    }

    public boolean isSolid(Model m) {
        switch(type) {
            case EMPTY:
                return false;
//                return hasSlider(m) != null;
            case FIXED:
                return true;
            case RED:
//                if(active) {
//                    return active;
//                }
//                else{
//                    return hasSlider(m) != null;
//                }
            case BLUE:
//                if(active) {
//                    return active;
//                }
//                else{
//                    return hasSlider(m) != null;
//                }
            case LOCKED_FINISH:
                return true;
            default:
                return true;
//                return hasSlider(m) != null;
        }
    }

}

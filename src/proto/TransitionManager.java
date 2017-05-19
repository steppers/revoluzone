package proto;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Created by steppers on 2/12/17.
 */
public class TransitionManager {

    public enum Type {
        FADE,
        FADE_ROTATE_CW,
        FADE_ROTATE_CCW,
        GROW,
        GROW_FADE,
        SHRINK,
        ROTATE_CW,
        ROTATE_CCW
    }

    private Model from;
    private Model to;

    private GameState.State toState;
    private GameState slickState;

    private Type type;
    private boolean transitioning = false;
    private float transitionSpeed;

    private float opacityDelta;
    private float scaleTarget;
    private float rotationTarget;

    public TransitionManager(GameState slickState) {
        this.slickState = slickState;
    }

    public void transitionFade(Model from, Model to, GameState.State newState, float duration) {
        toState = newState;
        this.type = Type.FADE;
        this.from = from;
        this.to = to;
        from.setOpacity(1);
        to.setOpacity(0);
        transitioning = true;
        slickState.previousState = slickState.currentState;
        slickState.currentState = GameState.State.TRANSITION;
        transitionSpeed = 1 / duration;
    }

    public void transitionFadeRotate(Model from, Model to, GameState.State newState, float angle, float duration) {
        toState = newState;
        this.from = from;
        this.to = to;
        from.setOpacity(1);
        to.setOpacity(0);

        if(angle > 0) {
            to.setRotation(-90);
            this.type = Type.FADE_ROTATE_CW;
        } else {
            to.setRotation(90);
            this.type = Type.FADE_ROTATE_CCW;
        }

        rotationTarget = from.getRotation() + angle;
        transitioning = true;
        slickState.previousState = slickState.currentState;
        slickState.currentState = GameState.State.TRANSITION;
        transitionSpeed = 1 / duration;
    }

    public void transitionGrow(Model from, GameState.State newState, float toSize, float duration) {
        toState = newState;
        this.type = Type.GROW;
        this.from = from;
        this.to = from;
        scaleTarget = toSize;
        transitioning = true;
        slickState.previousState = slickState.currentState;
        slickState.currentState = GameState.State.TRANSITION;
        transitionSpeed = (toSize - from.getScale()) / duration;
    }

    public void transitionShrink(Model from, GameState.State newState, float toSize, float duration) {
        toState = newState;
        this.type = Type.SHRINK;
        this.from = from;
        this.to = from;
        scaleTarget = toSize;
        transitioning = true;
        slickState.previousState = slickState.currentState;
        slickState.currentState = GameState.State.TRANSITION;
        transitionSpeed = (toSize - from.getScale()) / duration;
    }

    public void transitionRotate(Model from, GameState.State newState, float angle, float duration) {
        toState = newState;
        this.from = from;
        this.to = from;

        if(angle > 0) {
            this.type = Type.ROTATE_CW;
        } else {
            this.type = Type.ROTATE_CCW;
        }

        rotationTarget = from.getRotation() + angle;
        transitioning = true;
        slickState.previousState = slickState.currentState;
        slickState.currentState = GameState.State.TRANSITION;
        transitionSpeed = 90f / duration;
    }

    public void update(float delta) {
        if(transitioning) {
            switch(type) {
                case FADE:
                    from.setOpacity(from.getOpacity() - transitionSpeed*delta);
                    to.setOpacity(to.getOpacity() + transitionSpeed*delta);
                    if(to.getOpacity() > 1) {
                        transitioning = false;
                        to.setOpacity(1);
                    }
                    break;
                case GROW:
                    from.setScale(from.getScale() + transitionSpeed*delta);
                    if(from.getScale() > scaleTarget) {
                        transitioning = false;
                        from.setScale(scaleTarget);
                    }
                    break;
                case SHRINK:
                    from.setScale(from.getScale() + transitionSpeed*delta);
                    if(from.getScale() < scaleTarget) {
                        transitioning = false;
                        from.setScale(scaleTarget);
                    }
                    break;
                case ROTATE_CW:
                    from.setRotation(from.getRotation() + transitionSpeed*delta);
                    if(from.getRotation() > rotationTarget) {
                        transitioning = false;
                        from.setRotation(rotationTarget);
                        to.recalcSlider();
                        to.recalcBall();
                    }
                    break;
                case ROTATE_CCW:
                    from.setRotation(from.getRotation() - transitionSpeed*delta);
                    if(from.getRotation() < rotationTarget) {
                        transitioning = false;
                        from.setRotation(rotationTarget);
                        to.recalcSlider();
                        to.recalcBall();
                    }
                    break;
                case FADE_ROTATE_CW:
                    from.setOpacity(from.getOpacity() - transitionSpeed*delta);
                    to.setOpacity(to.getOpacity() + transitionSpeed*delta);
                    from.setRotation(from.getRotation() + transitionSpeed*90*delta);
                    to.setRotation(to.getRotation() + transitionSpeed*90*delta);
                    if(to.getOpacity() > 1) {
                        transitioning = false;
                        from.setRotation(rotationTarget);
                        to.setRotation(0);
                        to.setOpacity(1);
                        to.recalcSlider();
                        to.recalcBall();
                    }
                    break;
                case FADE_ROTATE_CCW:
                    from.setOpacity(from.getOpacity() - transitionSpeed*delta);
                    to.setOpacity(to.getOpacity() + transitionSpeed*delta);
                    from.setRotation(from.getRotation() - transitionSpeed*90*delta);
                    to.setRotation(to.getRotation() - transitionSpeed*90*delta);
                    if(to.getOpacity() > 1) {
                        transitioning = false;
                        from.setRotation(rotationTarget);
                        to.setRotation(0);
                        to.setOpacity(1);
                        to.recalcSlider();
                        to.recalcBall();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void render(GameContainer gc, Graphics g) {
        if(type == Type.FADE_ROTATE_CCW || type == Type.FADE_ROTATE_CW) {
            to.renderBackPlane(gc, g);
            to.renderFloorPlane(gc, g);
            to.renderShadow(gc, g);
            to.renderObject(gc, g);
            from.renderBackPlane(gc, g);
            from.renderFloorPlane(gc, g);
            from.renderShadow(gc, g);
            from.renderObject(gc, g);
        } else {
            from.renderBackPlane(gc, g);
            from.renderFloorPlane(gc, g);
            from.renderShadow(gc, g);
            from.renderObject(gc, g);
            if (to != from) {
                to.renderBackPlane(gc, g);
                to.renderFloorPlane(gc, g);
                to.renderShadow(gc, g);
                to.renderObject(gc, g);
            }
        }
    }

    public boolean isTransitioning() {
        return transitioning;
    }

    public Model getNewModel() {
        return to;
    }

    public GameState.State getNewState() {
        return toState;
    }

}

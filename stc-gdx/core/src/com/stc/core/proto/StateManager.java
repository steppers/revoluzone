package com.stc.proto;

import com.stc.proto.states.SplashState;

/**
 * Created by steppers on 8/7/17.
 */

public class StateManager {

    private static State currentState = new SplashState();
    private static State nextState;

    private static boolean transitioning = false;
    private static float alphaFrom, alphaTo, alpha;
    private static float scaleFrom, scaleTo, scale;
    private static float textAlphaFrom, textAlphaTo, textAlpha;
    private static float textScaleFrom, textScaleTo, textScale;
    private static float transitionDuration;
    private static float elapsed;

    public static void update(float delta) {
        if(transitioning) {
            elapsed += delta;
            if(elapsed >= transitionDuration) {
                elapsed = transitionDuration;
                transitioning = false;
            }

            float percent = elapsed / transitionDuration;
            alpha = alphaFrom + ((alphaTo - alphaFrom)*percent);
            scale = scaleFrom + ((scaleTo - scaleFrom)*percent);
            textAlpha = textAlphaFrom + ((textAlphaTo - textAlphaFrom)*percent);
            textScale = textScaleFrom + ((textScaleTo - textScaleFrom)*percent);
        }

        currentState.update(delta);
    }

    public static void render() {
        if(transitioning) {
            nextState.render(alpha, scale);
            nextState.renderText(textAlpha, textScale);
        }

        currentState.render(alpha, scale);
        currentState.renderText(textAlpha, textScale);
    }


}

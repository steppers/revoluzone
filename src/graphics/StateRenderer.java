package graphics;

import model.Tile;
import model.WorldModel;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.*;

/**
 * Created by steppers on 2/11/17.
 */
public class StateRenderer {

    private WorldModel currentState;
    private WorldModel nextState;

    public enum TransitionType {
        FADE,
        GROW,
        SHRINK
    }

    private TransitionType transition = TransitionType.FADE;
    private float transitionProgress = 1f;
    private float transitionTarget = 0;
    private float transitionRate = 0f;
    private boolean transitioning = false;

    private float opacity = 1;
    private float opacityNext = 1;
    private float scale = 0.5f;
    private float scaleNext = 1;

    public StateRenderer(WorldModel startState) {
        currentState = startState;
    }

    public WorldModel getNextState() {
        return nextState;
    }

    public void setState(WorldModel state) {
        this.currentState = state;
    }

    public void update(float delta) {
        if(transitioning) {
            switch(transition) {
                case FADE:
                    transitionProgress -= transitionRate * delta;
                    if(transitionProgress < 0) {
                        transitionProgress = 0;
                        transitioning = false;
                        currentState = nextState;
                    }
                    opacity = transitionProgress;
                    opacityNext = 1 - transitionProgress;
                    break;
                case GROW:
                    transitionProgress += transitionRate * delta;
                    if(transitionProgress > 1) {
                        transitionProgress = 1;
                        transitioning = false;
                        currentState = nextState;
                    }
                    scale = transitionProgress;
                    opacity = 1 - transitionProgress;
                    scaleNext = transitionProgress;
                    opacityNext = transitionProgress;
                    break;
                case SHRINK:
                    transitionProgress -= transitionRate * delta;
                    if(transitionProgress < 0.5f) {
                        transitionProgress = 0.5f;
                        transitioning = false;
                        currentState = nextState;
                    }
                    scale = transitionProgress;
                    opacity = transitionProgress;
                    scaleNext = transitionProgress;
                    opacityNext = 1-transitionProgress;
                    break;
            }
        }
    }

    public void render(GameContainer gc, Graphics g) {
        if(transitioning)
            renderWorld(gc, g, currentState, scaleNext, opacityNext);
        renderWorld(gc, g, currentState, scale, opacity);
    }

    public void transitionFade(WorldModel nextState, float transitionRate) {
        transition = TransitionType.FADE;
        this.transitionProgress = 1;
        this.transitionRate = transitionRate;
        this.nextState = nextState;
        scaleNext = scale;
        transitioning = true;
    }

    public void transitionShrink(WorldModel nextState, float transitionRate) {
        transition = TransitionType.SHRINK;
        this.transitionProgress = 1;
        this.transitionRate = transitionRate;
        this.nextState = nextState;
        transitioning = true;
    }

    public void transitionGrow(WorldModel nextState, float transitionRate) {
        transition = TransitionType.GROW;
        this.transitionProgress = 0.5f;
        this.transitionRate = transitionRate;
        this.nextState = nextState;
        transitioning = true;
    }

    public boolean isTransitioning() {
        return transitioning;
    }

    public float getScale() {
        return scale;
    }

    public float getOpacity() {
        return opacity;
    }

    void renderWorld(GameContainer gc, Graphics g, WorldModel state, float scale, float opacity) {
        Color op = new Color(1,1,1,opacity);
        float SCALE = ((Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / state.GRID_SIZE) * scale;

        Tile[][] grid = state.getGrid();
        Tile t;
        float offset = - (state.getGridSize() / 2) + 0.5f;

        Rectangle rect = new Rectangle(-SCALE/2, -SCALE/2, SCALE, SCALE);
        Shape tile = rect.transform(Transform.createRotateTransform((float)(state.getRotation()*Math.PI)/180));

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);

        //First render pass (Floor)
        g.setColor(Color.white.darker(0.2f).multiply(op)); //Floor color
        for(int x = 0; x < state.getGridSize(); x++) {
            for (int y = 0; y < state.getGridSize(); y++) {
                Vector2f pos = new Vector2f(offset + x, offset + y);
                pos.sub(-state.getRotation());
                pos.scale(SCALE);
                pos.add(screenOffset);
                tile.setLocation(pos.x, pos.y);
                g.fill(tile);
            }
        }

        //Second render pass (Shadows)
        {
            Vector2f shadow = new Vector2f(0.07f, 0.07f).sub(state.getRotation() + 25).add(new Vector2f(offset, offset));
            g.setColor(Color.white.darker(0.8f).multiply(op)); //Shadow color
            for (int x = 0; x < state.getGridSize(); x++) {
                for (int y = 0; y < state.getGridSize(); y++) {
                    if (state.isSolid(grid[x][y])) {
                        Vector2f pos = new Vector2f(shadow.x + x, shadow.y + y);
                        pos.sub(-state.getRotation());
                        pos.scale(SCALE);
                        pos.add(screenOffset);
                        tile.setLocation(pos.x, pos.y);
                        g.fill(tile);
                    }
                }
            }

            //Calc ball pos
            Vector2f pos = state.getBall().getPos().add(shadow);
            pos.sub(-state.getRotation());
            pos.scale(SCALE);
            pos.add(screenOffset);

            Circle c = new Circle(0, 0, SCALE / 2);
            Shape circ = c.transform(Transform.createRotateTransform((float) (state.getRotation() * Math.PI) / 180));
            circ.setLocation(pos.x, pos.y);
            g.fill(circ);
        }

        //Third render pass (blocks)
        for(int x = 0; x < state.getGridSize(); x++) {
            for(int y = 0; y < state.getGridSize(); y++) {
                t = grid[x][y];
                Vector2f pos = new Vector2f(offset + x, offset + y);
                pos.sub(-state.getRotation());
                pos.scale(SCALE);
                pos.add(screenOffset);
                tile.setLocation(pos.x, pos.y);

                switch (t.getType()) {
                    case EMPTY:
                        break;
                    case FIXED:
                        g.setColor(Color.white.darker(0.4f).multiply(op));
                        g.fill(tile);
                        break;
                    case RED:
                        g.setColor(Color.red.multiply(op));
                        if(!t.isActive())
                            g.setColor(g.getColor().multiply(new Color(1, 1, 1, 0.3f)).multiply(op));
                        g.fill(tile);
                        break;
                    case BLUE:
                        g.setColor(Color.blue.multiply(op));
                        if(!t.isActive())
                            g.setColor(g.getColor().multiply(new Color(1, 1, 1, 0.3f)).multiply(op));
                        g.fill(tile);
                        break;
                    case KILL:
                        g.setColor(Color.yellow.multiply(op));
                        g.fill(tile);
                        break;
                    case FINISH:
                        g.setColor(Color.green.multiply(op));
                        g.fill(tile);
                        break;
                    case START:
                        g.setColor(Color.green.multiply(op));
                        g.fill(tile);
                        break;
                    case SWITCH:
                        if(t.isActive())
                            g.setColor(Color.white.multiply(op));
                        else{
                            g.setColor(Color.black.multiply(op));
                        g.fill(tile);
                        }
                }
            }
        }

        Vector2f pos = state.getBall().getPos().add(new Vector2f(offset, offset));
        pos.sub(-state.getRotation());
        pos.scale(SCALE);
        pos.add(screenOffset);

        Circle c = new Circle(0, 0, SCALE/2);
        Shape circ = c.transform(Transform.createRotateTransform((float)(state.getRotation()*Math.PI)/180));
        circ.setLocation(pos.x, pos.y);
        g.setColor(Color.cyan.multiply(op));
        g.fill(circ);
    }

}

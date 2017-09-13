package com.stc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by steppers on 6/30/17.
 */

public class Model {

    private Tile[][] tiles;
    private int gridSize = 6;
    private float scale = 1.0f;
    public float rotation = 0.0f;

    private ShapeRenderer renderer;

    Model() {
        tiles = new Tile[gridSize][gridSize];
        for(int x = 0; x < gridSize; x++) {
            for(int y = 0; y < gridSize; y++) {
                tiles[x][y] = new Tile(Tile.Type.FIXED.ordinal());
            }
        }
        for(int x = 1; x < gridSize-1; x++) {
            for(int y = 1; y < gridSize-1; y++) {
                tiles[x][y] = new Tile(Tile.Type.EMPTY.ordinal());
            }
        }
        tiles[3][3] = new Tile(Tile.Type.FIXED.ordinal());

        renderer = Main.getShapeRenderer();
    }

    public void update(float delta) {

    }

    public void render() {
        renderBackPlane();
        renderFloorPlane();
        renderShadow();
        renderObject();
    }

    void renderBackPlane() {
        float SCALE = ((Math.min(Globals.display_height, Globals.display_width) * 0.70f) / gridSize) * scale;
        float offset = - ((float)gridSize / 2);

        Color c = Color.WHITE.cpy().mul(0.8f, 0.8f, 0.8f, 1f);
        renderer.setColor(c);
        renderer.identity();
        renderer.translate(Globals.display_width/2, Globals.display_height/2, 0);
        renderer.rotate(0,0,1,-rotation);
        renderer.scale(SCALE, SCALE, 1);

        for(int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                renderer.rect(offset + x, offset + y, 1.0f, 1.0f);
            }
        }
    }

    void renderFloorPlane() {
        float SCALE = ((Math.min(Globals.display_height, Globals.display_width) * 0.70f) / gridSize) * scale;
        Tile t;
        float offset = - ((float)gridSize / 2) + 0.5f;
    }

    void renderShadow() {
        float SCALE = ((Math.min(Globals.display_height, Globals.display_width) * 0.70f) / gridSize) * scale;
        float offset = - ((float)gridSize / 2);

        Color c = Color.WHITE.cpy().mul(0.2f, 0.2f, 0.2f, 0.7f);
        renderer.setColor(c);
        renderer.identity();
        renderer.translate(Globals.display_width/2, Globals.display_height/2, 0);
        renderer.rotate(0,0,1,-rotation);
        renderer.scale(SCALE, SCALE, 1);

        Vector2 shadow = new Vector2(0.07f, 0.07f).rotate(rotation + 25).add(new Vector2(offset, offset));
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                if(tiles[x][y].type == Tile.Type.LOCKED_FINISH)
                    continue;
//                if (tiles[x][y].isSolid(this) && (tiles[x][y].hasSlider(this) == null)) {
//                    Vector2f pos = new Vector2f(shadow.x + x, shadow.y + y);
//                    pos.sub(-rotation);
//                    pos.scale(SCALE);
//                    pos.add(screenOffset);
//                    tile.setLocation(pos.x, pos.y);
//                    g.fill(tile);
//                }
                if (tiles[x][y].isSolid(this)) {
                    renderer.rect(shadow.x + x, shadow.y + y, 1.0f, 1.0f);
                }
            }
        }
    }

    void renderObject() {
        float SCALE = ((Math.min(Globals.display_height, Globals.display_width) * 0.70f) / gridSize) * scale;
        float offset = - ((float)gridSize / 2);

        renderer.identity();
        renderer.translate(Globals.display_width/2, Globals.display_height/2, 0);
        renderer.rotate(0,0,1,-rotation);
        renderer.scale(SCALE, SCALE, 1);

        for(int x = 0; x < gridSize; x++) {
            for(int y = 0; y < gridSize; y++) {
                switch (tiles[x][y].type) {
                    case FIXED:
                        renderer.setColor(Color.WHITE.cpy().mul(0.6f, 0.6f, 0.6f, 1f));
                        renderer.rect(offset + x, offset + y, 1.0f, 1.0f);
                        break;
                    case RED:
//                        if(!tiles[x][y].active)
//                            break;
//                        g.setColor(Color.red.multiply(opCol));
//                        g.fill(tile);
                        break;
                    case BLUE:
//                        if(!tiles[x][y].active)
//                            break;
//                        g.setColor(Color.blue.multiply(opCol));
//                        g.fill(tile);
                        break;
                    default:
                        break;
                }
            }
        }
    }

}

package proto;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Created by steppers on 2/12/17.
 */
public class Model extends Renderable {

    private static final float ROT_VEL = 360f;

    public int gridSize;
    public int score = 0;

    private HashMap<String, String> properties;

    public Tile[][] tiles;
    public Ball ball;

    private float rotation = 0;
    private float scale = 1;
    private float opacity = 1;
    private float textOpacity = 1;

    private Color opCol = new Color(1,1,1,1);

    public Model(String fileName, float scale) {
        properties = new HashMap<>();
        loadFromFile(fileName);
        setOpacity(1);
        setScale(scale);
        initRedBlue();
        recalcBall();
    }

    public Model(String fileName, float scale, float opacity) {
        properties = new HashMap<>();
        loadFromFile(fileName);
        setOpacity(opacity);
        setScale(scale);
        initRedBlue();
        recalcBall();
    }

    public void update(float delta) {
        //Update our rotation here
        ball.update(delta);
    }

    public Tile getTileUnderBall() {
        return tiles[(int)ball.x][(int)ball.y];
    }

    private void initRedBlue() {
        for(int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                if(tiles[x][y].type == Tile.Type.RED) {
                    tiles[x][y].active = true;
                } else {
                    tiles[x][y].active = false;
                }
            }
        }
    }

    public void toggleRedBlue() {
        for(int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                if(tiles[x][y].type == Tile.Type.RED || tiles[x][y].type == Tile.Type.BLUE) {
                    tiles[x][y].active = !tiles[x][y].active;
                }
            }
        }
        recalcBall();
    }

    public boolean isWaitingForBall() {
        return ball.isMoving();
    }

    public void recalcBall() {
        int r = (int)rotation % 360;
        while(r < 0)
            r += 360;
        int y = (int)(ball.y);
        int x = (int)(ball.x);
        switch(r) {
            case 0:
                for(y = y+1; y < gridSize; y++) {
                    if(tiles[x][y].isSolid()) {
                        ball.move(x, y-1);
                        return;
                    }
                }
                break;
            case 90:
                for(x = x+1; x < gridSize; x++) {
                    if(tiles[x][y].isSolid()) {
                        ball.move(x-1, y);
                        return;
                    }
                }
                break;
            case 180:
                for(y = y-1; y >= 0; y--) {
                    if(tiles[x][y].isSolid()) {
                        ball.move(x, y+1);
                        return;
                    }
                }
                break;
            case 270:
                for(x = x-1; x >= 0; x--) {
                    if(tiles[x][y].isSolid()) {
                        ball.move(x+1, y);
                        return;
                    }
                }
                break;
        }
    }

    public void render(GameContainer gc, Graphics g) {
        renderBackPlane(gc, g);
        renderFloorPlane(gc, g);
        renderShadow(gc, g);
        renderObject(gc, g);
    }

    @Override
    public void renderBackPlane(GameContainer gc, Graphics g) {
        float SCALE = ((Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / gridSize) * scale;
        float offset = - (gridSize / 2) + 0.5f;

        Rectangle rect = new Rectangle(-SCALE/2, -SCALE/2, SCALE, SCALE);
        Shape tile = rect.transform(Transform.createRotateTransform((float)(rotation*Math.PI)/180));

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);

        g.setColor(Color.white.darker(0.2f).multiply(opCol)); //Floor color
        for(int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                Vector2f pos = new Vector2f(offset + x, offset + y);
                pos.sub(-rotation);
                pos.scale(SCALE);
                pos.add(screenOffset);
                tile.setLocation(pos.x, pos.y);
                g.fill(tile);
            }
        }
    }

    @Override
    public void renderFloorPlane(GameContainer gc, Graphics g) {
        float SCALE = ((Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / gridSize) * scale;
        Tile t;
        float offset = - (gridSize / 2) + 0.5f;

        //Full floor tile
        Rectangle rect = new Rectangle(-SCALE/2, -SCALE/2, SCALE, SCALE);
        Shape tile = rect.transform(Transform.createRotateTransform((float)(rotation*Math.PI)/180));
        Rectangle stripeRect = new Rectangle(-SCALE/2, -SCALE/2, SCALE, SCALE);
        Shape stripe = stripeRect.transform(Transform.createScaleTransform(0.333f, 1f));
        stripe = stripe.transform(Transform.createRotateTransform((float)(rotation*Math.PI)/180));

        //Switch rect
        rect = new Rectangle(-(SCALE/2)*0.6f, -(SCALE/2)*0.6f, SCALE*0.6f, SCALE*0.6f);
        Shape switch1 = rect.transform(Transform.createRotateTransform((float)(rotation*Math.PI)/180));
        Circle cicleSwitch = new Circle(0,0, SCALE*0.5f*0.4f);

        //Start/finish
        Circle circleLarge = new Circle(0,0, SCALE*0.5f*0.8f);
        Circle circleSmall = new Circle(0,0, SCALE*0.5f*0.7f);

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);

        for(int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                Vector2f pos = new Vector2f(offset + x, offset + y);
                pos.sub(-rotation);
                pos.scale(SCALE);
                pos.add(screenOffset);
                t = tiles[x][y];
                switch(t.type) {
                    case KILL:
                        g.setColor(Color.yellow.multiply(opCol));
                        tile.setLocation(pos.x, pos.y);
                        g.fill(tile);
                        g.setColor(Color.black.multiply(opCol));
                        stripe.setLocation(pos.x, pos.y);
                        g.fill(stripe);
                        break;
                    case RED:
                        if(t.active)
                            break;
                        g.setColor(Color.red.multiply(opCol).multiply(new Color(1, 1, 1, 0.3f)));
                        tile.setLocation(pos.x, pos.y);
                        g.fill(tile);
                        break;
                    case BLUE:
                        if(t.active)
                            break;
                        g.setColor(Color.blue.multiply(opCol).multiply(new Color(1, 1, 1, 0.3f)));
                        tile.setLocation(pos.x, pos.y);
                        g.fill(tile);
                        break;
                    case GREEN:
                        if(t.active)
                            break;
                        g.setColor(Color.green.multiply(opCol).multiply(new Color(1, 1, 1, 0.3f)));
                        tile.setLocation(pos.x, pos.y);
                        g.fill(tile);
                        break;
                    case START:
                        g.setColor(Color.green.darker().multiply(opCol));
                        circleLarge.setCenterX(pos.x);
                        circleLarge.setCenterY(pos.y);
                        g.fill(circleLarge);
                        g.setColor(Color.green.multiply(opCol));
                        circleSmall.setCenterX(pos.x);
                        circleSmall.setCenterY(pos.y);
                        g.fill(circleSmall);
                        break;
                    case FINISH:
                        g.setColor(Color.darkGray.multiply(opCol));
                        circleLarge.setCenterX(pos.x);
                        circleLarge.setCenterY(pos.y);
                        g.fill(circleLarge);
                        g.setColor(Color.black.multiply(opCol));
                        circleSmall.setCenterX(pos.x);
                        circleSmall.setCenterY(pos.y);
                        g.fill(circleSmall);
                        break;
                    case SWITCH:
                        g.setColor(Color.darkGray.multiply(opCol));
                        switch1.setLocation(pos.x, pos.y);
                        g.fill(switch1);
                        if(t.active)
                            g.setColor(Color.green.multiply(opCol));
                        else
                            g.setColor(Color.red.multiply(opCol));
                        cicleSwitch.setCenterX(pos.x);
                        cicleSwitch.setCenterY(pos.y);
                        g.fill(cicleSwitch);
                        break;
                    case LOCKED_FINISH:
                        g.setColor(Color.darkGray.multiply(opCol));
                        circleLarge.setCenterX(pos.x);
                        circleLarge.setCenterY(pos.y);
                        g.fill(circleLarge);
                        g.setColor(Color.red.multiply(opCol));
                        circleSmall.setCenterX(pos.x);
                        circleSmall.setCenterY(pos.y);
                        g.fill(circleSmall);
                        break;
                }
            }
        }

    }

    @Override
    public void renderShadow(GameContainer gc, Graphics g) {
        float SCALE = ((Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / gridSize) * scale;
        float offset = - (gridSize / 2) + 0.5f;

        Rectangle rect = new Rectangle(-SCALE/2, -SCALE/2, SCALE, SCALE);
        Shape tile = rect.transform(Transform.createRotateTransform((float)(rotation*Math.PI)/180));

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);
        
        Vector2f shadow = new Vector2f(0.07f, 0.07f).sub(rotation + 25).add(new Vector2f(offset, offset));
        g.setColor(Color.white.darker(0.8f).multiply(opCol).multiply(new Color(1,1,1,0.7f))); //Shadow color
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                if(tiles[x][y].type == Tile.Type.LOCKED_FINISH)
                    continue;
                if (tiles[x][y].isSolid()) {
                    Vector2f pos = new Vector2f(shadow.x + x, shadow.y + y);
                    pos.sub(-rotation);
                    pos.scale(SCALE);
                    pos.add(screenOffset);
                    tile.setLocation(pos.x, pos.y);
                    g.fill(tile);
                }
            }
        }

        //Render Ball shadow
        ball.renderShadow(gc, g, this);
    }

    @Override
    public void renderObject(GameContainer gc, Graphics g) {
        float SCALE = ((Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / gridSize) * scale;
        float offset = - (gridSize / 2) + 0.5f;

        Rectangle rect = new Rectangle(-SCALE/2, -SCALE/2, SCALE, SCALE);
        Shape tile = rect.transform(Transform.createRotateTransform((float)(rotation*Math.PI)/180));

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);

        for(int x = 0; x < gridSize; x++) {
            for(int y = 0; y < gridSize; y++) {
                Vector2f pos = new Vector2f(offset + x, offset + y);
                pos.sub(-rotation);
                pos.scale(SCALE);
                pos.add(screenOffset);
                tile.setLocation(pos.x, pos.y);

                switch (tiles[x][y].type) {
                    case FIXED:
                        g.setColor(Color.white.darker(0.4f).multiply(opCol));
                        g.fill(tile);
                        break;
                    case RED:
                        if(!tiles[x][y].active)
                            break;
                        g.setColor(Color.red.multiply(opCol));
                        g.fill(tile);
                        break;
                    case BLUE:
                        if(!tiles[x][y].active)
                            break;
                        g.setColor(Color.blue.multiply(opCol));
                        g.fill(tile);
                        break;
                    case GREEN:
                        if(!tiles[x][y].active)
                            break;
                        g.setColor(Color.green.multiply(opCol));
                        g.fill(tile);
                        break;
                    default:
                        break;
                }
            }
        }
        ball.renderObject(gc, g, this);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        opCol = new Color(1,1,1,opacity);
    }

    public float getOpacity() {
        return opacity;
    }

    public void setTextOpacity(float opacity) {
        this.textOpacity = opacity;
    }

    public float getTextOpacity() {
        return textOpacity;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String val) {
        properties.put(key, val);
    }

    public boolean hasCompleted() {
        return tiles[(int)ball.x][(int)ball.y].type == Tile.Type.FINISH;
    }

    private void loadFromFile(String path) {
        File file = new File("res/config/" + path);
        if(!file.exists()) {
            System.out.println("File not found: " + path);
            System.exit(1);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            ball = new Ball(3, 3);

            //Finds the tile array
            int y = 1;
            while((line = br.readLine()).startsWith("{")) {
                String[] ids = line.substring(1, line.length()-1).split(",");
                if(tiles == null)
                    initTiles(ids.length);
                for(int i = 0; i < ids.length; i++) {
                    tiles[i+1][y] = new Tile(Integer.parseInt(ids[i]));
                    if(ids[i].equals("5")) {
                        ball = new Ball(i+1, y);
                    }
                }
                y++;
            }
            processPropertyLine(line);

            //Process modifiers
            while((line = br.readLine()) != null) {
                processPropertyLine(line);
            }

        } catch (Exception e) {
            System.err.println("Error reading file: " + path);
            e.printStackTrace();
            System.exit(2);
        }
    }

    private void processPropertyLine(String line) {
        String type = line.split("=")[0].trim();
        String data = line.split("=")[1].trim();
        switch (type) {
            case "name":
                properties.put("name", data);
                break;
            case "next":
                properties.put("next", data);
                break;
            case "prev":
                properties.put("prev", data);
                break;
            case "score":
                properties.put("score", data);
                break;
            case "link"://Links switches, teleporters and sliders
                String first = data.split("->")[0];
                String second = data.split("->")[1];
                int sx, sy, tx, ty;
                sx = Integer.parseInt(first.split(",")[0]);
                sy = Integer.parseInt(first.split(",")[1]);
                tx = Integer.parseInt(second.split(",")[0]);
                ty = Integer.parseInt(second.split(",")[1]);
                tiles[sx][sy].links.add(tiles[tx][ty]);
                break;
            default:
                properties.put(type, data);
        }
    }

    private void initTiles(int size) {
        tiles = new Tile[size+2][size+2];
        gridSize = size+2;

        for(int x = 0; x < size+2; x++) {
            for(int y = 0; y < size+2; y++) {
                if(x == 0 || y == 0 || x == size+1 || y == size+1) {
                    tiles[x][y] = new Tile(Tile.Type.FIXED.ordinal());
                } else {
                    tiles[x][y] = new Tile(Tile.Type.EMPTY.ordinal());
                }
            }
        }
    }

    public Tile getTileFromMousePos(GameContainer gc) {
        float SCALE = ((Math.min(gc.getHeight(), gc.getWidth()) * 0.70f) / gridSize) * scale;

        float offset = - (gridSize / 2);

        Vector2f screenOffset = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);

        Vector2f mouse = new Vector2f(gc.getInput().getMouseX(), gc.getInput().getMouseY());
        mouse = mouse.sub(screenOffset);
        mouse = mouse.scale(1/SCALE);
        mouse = mouse.sub(rotation);
        mouse = mouse.sub(new Vector2f(offset, offset));

        int x, y;
        x = (int)mouse.x;
        y = (int)mouse.y;

        if(x >= 0 && x < gridSize && y >= 0 && y < gridSize) {
            return tiles[x][y];
        }
        return null;
    }

}

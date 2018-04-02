package stc;

import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.*;

import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import javax.imageio.ImageIO;

/**
 * Created by Jamie on 20/03/2018.
 */
public class SplashScreen {

    float opacity = 1;
    boolean fadeIn = true;
    boolean fadeOut = false;
    boolean introComplete = false;
    float count;
    TransitionManager tm;

    public SplashScreen(TransitionManager tm){
        count = 0;
        this.tm = tm;
    }

    public void update(float delta){
        if(fadeIn) {
            if (opacity > 0) {
                opacity -= delta;
            }else{
                fadeIn = false;
            }
        }else if(fadeOut){
            if(opacity < 1) {
                opacity += delta;
            }else{
                introComplete = true;
                tm.transitionIntro();
            }
        }else{
            count += delta;
            if(count > 5){
                fadeOut = true;
            }
        }
    }

    public void setOpacity(float opacity){
        this.opacity = opacity;
    }

    public float getOpacity(){
        return this.opacity;
    }

    public void render(GameContainer gc, Graphics g){

        if(!introComplete) {
            org.newdawn.slick.Image controls;
            try {
                controls = new org.newdawn.slick.Image("res/images/control splash screen.png");
                controls.drawWarped(0, 0, 0, gc.getHeight(), gc.getWidth(), gc.getHeight(), gc.getWidth(), 0);
            } catch (Exception e) {
            }
        }
        Rectangle r = new Rectangle(0,0,gc.getWidth(),gc.getHeight());
        g.setColor(Color.black.multiply(new Color(1,1,1, opacity)));
        g.fill(r);
    }

}

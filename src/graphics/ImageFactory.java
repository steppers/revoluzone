package graphics;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.Hashtable;

/**
 * Created by an6g15 on 11/02/2017.
 */
public class ImageFactory {

    static {
        numbers = new Hashtable<>();
    }

    private static Hashtable<Integer, Image> numbers;

    private void initaliseImages(){
        try{
            numbers.put(0, new Image("res/zero.png"));
        }
        catch (SlickException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public Image getNumber(int numberToGet){

    }
}

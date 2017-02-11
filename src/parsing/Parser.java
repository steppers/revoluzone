package parsing;

import java.io.*;
import java.util.ArrayList;

public class Parser {
    BufferedReader reader;
    File config;
    String nextName;

    public Parser(){
    }

    /**
     * Trys to open the file.
     * Returns 0 if success
     * 1 if file not found
     * @param path
     * @return
     */
    public int loadFile(String path){
        config = new File(path);
        if(!config.exists() || !config.isDirectory()) {
            System.out.println("File doesn't exist or directory is bad! (Code 1)");
            return 1;
        }else{
            return 0;
        }
    }

    public int getData(){
        Integer levelID = 0;
        Integer score = 0;
        ArrayList<String> mapList = new ArrayList<>();
        try{
            reader = new BufferedReader(new FileReader(config));

            String line = "";

            //Reads the file to ascertain ID
            while(!((line = reader.readLine()).contains("next_level"))){
                mapList.add(line);
            }
            while(!((line = reader.readLine()).contains("score:"))){
                nextName = line.split("=")[1];
            }
        }catch (FileNotFoundException e) {
            //This should never be thrown as we have checked for this above
            System.out.println("File doesn't exist or directory is bad! (Code 1)");
        }catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();

        } finally {
            try{
                reader.close();
            } catch (IOException e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return 0;
    }
}

class Level{
    private Integer levelID;
    private Integer[][] tileInfo;
    private Level(Integer[][] level, Integer levelID){
        this.tileInfo = level;
        this.levelID = levelID;
    }
}
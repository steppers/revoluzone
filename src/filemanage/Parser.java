package filemanage;

import model.WorldModel;
import states.World;

import java.io.*;
import java.util.ArrayList;

public class Parser {
    BufferedReader reader;
    File config;
    String nextName;
    Processor fileProcessor;

    public Parser(){
    }

    /**
     * Tries to open the file.
     * Returns 0 if success
     * 1 if file not found
     * @param path
     * @return
     */
    public int loadFile(String path){
        config = new File(path);
        if(!config.exists()) {
            System.out.println("File doesn't exist or directory is bad! (Code 1)");
            return 1;
        }else{
            return 0;
        }
    }

    public WorldModel getData(){
        Integer levelID = 0;
        Integer score = 0;
        ArrayList<String> mapList = new ArrayList<>();
        try{
            levelID = Integer.parseInt(config.getName().substring(0,1));
        }catch (NumberFormatException e){
            System.err.println("Invalid file name - defaulting ID to -1.");
            levelID = -1;
        }
        try{
            reader = new BufferedReader(new FileReader(config));

            String line = "";

            //Reads lines and inputs to array of map data
            while(!((line = reader.readLine()).contains("next_level"))){
                mapList.add(line);
            }
            //Read next file name
            nextName = line.split("=")[1];
            if(!((line = reader.readLine()).contains("score:"))){
                try{
                    score = Integer.parseInt(line.split("=")[1]);
                }catch (NumberFormatException e){
                    System.err.println("Error ! Not a valid number. Defaulting to 0!");
                    score = 0;
                }
            }
        }catch (FileNotFoundException e) {
            //This should never be thrown as we have checked for this above
            System.out.println("File doesn't exist or directory is bad! (Code 1)");
            e.printStackTrace();
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
        return parseComplete(levelID, score, mapList);
    }

    /**
     * Calls the processor to getState converting data to game state information.
     */
    public WorldModel parseComplete(Integer levelId, Integer score, ArrayList<String> mapData){
        fileProcessor = new Processor(levelId, score, mapData);
        return fileProcessor.getState();

    }
}
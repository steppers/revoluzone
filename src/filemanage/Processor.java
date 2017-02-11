package filemanage;

import model.Tile;
import model.WorldModel;

import java.util.ArrayList;

public class Processor {

    private Integer levelId;
    private Integer score;
    private ArrayList<String> mapData;
    WorldModel newState;
    private Tile[][] tileInfo;

    public Processor(Integer levelId, Integer score, ArrayList<String> mapData){
        this.levelId = levelId;
        this.score = score;
        this.mapData = mapData;
        this.tileInfo = new Tile[mapData.size()+2][mapData.size()+2];
        this.newState = new WorldModel();
    }

    public WorldModel getState(){
        generateMap();
        return newState;
    }

    private void generateMap(){
        for(int i = 0; i < mapData.size()+2; i++){
            for(int j = 0; j < mapData.size()+2; j++){
                if(i == 0 || i == mapData.size()+1 || j == 0 || j == mapData.size()+1) {
                    tileInfo[i][j] = new Tile(Tile.Type.FIXED.ordinal());
                    continue;
                }
                tileInfo[i][j] = new Tile(Integer.parseInt(mapData.get(j-1).substring(1, mapData.get(j-1).indexOf("}")).split(",")[i-1]));
                if(tileInfo[i][j].getType() == Tile.Type.BLUE){
                    tileInfo[i][j].setActive(true);
                }
                if(tileInfo[i][j].getType() == Tile.Type.START){
                    newState.getBall().setPos(i, j);
                }
            }
        }
        newState.setGrid(tileInfo);
        newState.setGridSize(mapData.size()+2);
    }
}

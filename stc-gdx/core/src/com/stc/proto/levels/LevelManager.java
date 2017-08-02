package com.stc.proto.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.stc.proto.Globals;

import java.io.File;
import java.util.HashMap;

/**
 * Created by steppers on 8/2/17.
 */

public class LevelManager {

    // Singleton
    private static final LevelManager ins = new LevelManager();

    // Holds all levels, fully loaded or not.
    private HashMap<String, Level> levels;

    /*
     * Completes an initial load of all .lvl files in the levels folder
     * and validates them to ensure consist menu navigation.
     */
    private LevelManager() {
        levels = new HashMap<String, Level>();

        // Load all level files in root directory
        FileHandle root = Gdx.files.local(Globals.LEVEL_DIR);
        loadDirectory(root);
        validate();
    }

    /*
     * Scans a directory and loads all .lvl files
     */
    private void loadDirectory(FileHandle directory) {
        FileHandle[] files = directory.list();
        for (FileHandle f : files) {
            if(f.isDirectory()) {
                loadDirectory(f); // Recurse
            } else {
                if(!f.extension().equals("lvl"))
                    continue;

                loadFile(f);
            }
        }
    }

    /*
     * Creates a Level entry in the current level map.
     */
    private void loadFile(FileHandle file) {
        levels.put(file.nameWithoutExtension(), new Level(file.file()));
    }

    /*
     * Scans for and identifies inconsistencies in the level order
     * and removes references to levels that do not exist.
     */
    private void validate() {

    }

    public boolean addLevel(String levelname) {
        System.out.println("[!] Warning: Add level unimplemented!");
        return true;
    }

    public Level getLevel(String levelname) {
        return levels.get(levelname);
    }

    public boolean exists(String levelname) {
        return levels.containsKey(levelname);
    }

    public static LevelManager instance() {
        return ins;
    }

}

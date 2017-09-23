package com.stc.core.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.stc.core.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
        FileHandle root = Gdx.files.internal(Globals.LEVEL_DIR);
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
		Level l = new Level(file);
        levels.put(l.getLevelName(), l);
    }

    /*
     * Scans for and identifies inconsistencies in the level order
     * and removes references to levels that do not exist.
     */
    private void validate() {
        // Scan for files that don't exist
        ArrayList<Level> correctNext = new ArrayList<Level>();
        ArrayList<Level> correctPrev = new ArrayList<Level>();
        HashSet<String> references = new HashSet<String>();
        for (Level l: levels.values()) {
            if(!levels.containsKey(l.getNextLevelName()))
                correctNext.add(l);
            if(!levels.containsKey(l.getPrevLevelName()))
                correctPrev.add(l);

            references.add(l.getNextLevelName());
            references.add(l.getPrevLevelName());
        }

        // Scan for unreferenced files and delete them
        // TODO: If files referenced from these are not referenced anywhere else
        // they'll be left hanging around
        for (String l : levels.keySet()) {
            if(!references.contains(l))
                levels.remove(l);
        }

        //TODO: Correct levels with invalid links
    }

    public boolean addLevel(String levelname) {
        System.out.println("[!] Warning: Add level unimplemented!");
        return true;
    }

    public LevelInstance getLevelInstance(String levelname) {
		if(!levels.containsKey(levelname))
			return null;
        return levels.get(levelname).getInstance();
    }

    public boolean exists(String levelname) {
        return levels.containsKey(levelname);
    }

    public static LevelManager instance() {
        return ins;
    }

}

package com.madmath.core.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.madmath.core.main.MadMath;

public class Map {
    MadMath game;

    TiledMap tiledMap;

    public Map(MadMath game){
        this.game = game;
        tiledMap = new TiledMap();
        TiledMapTileSet tiles = new TiledMapTileSet();
        tiledMap.getTileSets().addTileSet(tiles);
        tiledMap.getTileSets().getTileSet(0);
    }
}

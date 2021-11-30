package com.madmath.core.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

public class Tile implements TiledMapTile {
    enum TileSort{
        Ground,
        Wall,
        Fountain,
        
    }
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void setId(int i) {

    }

    @Override
    public BlendMode getBlendMode() {
        return null;
    }

    @Override
    public void setBlendMode(BlendMode blendMode) {

    }

    @Override
    public TextureRegion getTextureRegion() {
        return null;
    }

    @Override
    public float getOffsetX() {
        return 0;
    }

    @Override
    public void setOffsetX(float v) {

    }

    @Override
    public float getOffsetY() {
        return 0;
    }

    @Override
    public void setOffsetY(float v) {

    }

    @Override
    public MapProperties getProperties() {
        return null;
    }
}

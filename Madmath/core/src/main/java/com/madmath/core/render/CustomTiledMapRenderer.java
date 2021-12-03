package com.madmath.core.render;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.madmath.core.entity.Entity;

public class CustomTiledMapRenderer extends OrthogonalTiledMapRenderer {

    public CustomTiledMapRenderer(TiledMap map) {
        super(map);
    }

    @Override
    public void renderObject(MapObject object) {
    }
}

package com.madmath.core.map;

import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.entity.Entity;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午11:14
 */
public class TrapTile extends AnimTile{

    float knockbackFactor;

    public TrapTile(float interval, Array<StaticTiledMapTile> frameTiles) {
        super(interval, frameTiles);
        knockbackFactor = 0.5f;
    }

    public float getKnockbackFactor() {
        return knockbackFactor;
    }

    public int trigger(Entity entity){
        return 1;
    }

}

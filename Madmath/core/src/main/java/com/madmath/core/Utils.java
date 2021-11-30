package com.madmath.core;

import com.badlogic.gdx.math.Vector2;
import com.madmath.core.map.StaticTile;
import java.util.Random;
import java.util.concurrent.Callable;

public class Utils {

    static public Vector2[] EnterSortPro = new Vector2[]{
            new Vector2(StaticTile.TileSort.floor_ladder.ordinal(),50),
            new Vector2(StaticTile.TileSort.floor_stair.ordinal(),50),
    };

    static public Vector2[] FloorSortPro = new Vector2[]{
            new Vector2(StaticTile.TileSort.floor_1.ordinal(),270),
            new Vector2(StaticTile.TileSort.floor_2.ordinal(),25),
            new Vector2(StaticTile.TileSort.floor_3.ordinal(),50),
            new Vector2(StaticTile.TileSort.floor_4.ordinal(),2),
            new Vector2(StaticTile.TileSort.floor_5.ordinal(),250),
            new Vector2(StaticTile.TileSort.floor_6.ordinal(),3),
            new Vector2(StaticTile.TileSort.floor_7.ordinal(),3),
            new Vector2(StaticTile.TileSort.floor_8.ordinal(),3),
    };

    static public Vector2[] WallSortPro = new Vector2[]{
            new Vector2(StaticTile.TileSort.wall_left.ordinal(),2),
            new Vector2(StaticTile.TileSort.wall_mid.ordinal(),110),
            new Vector2(StaticTile.TileSort.wall_right.ordinal(),2),
            new Vector2(StaticTile.TileSort.wall_hole_1.ordinal(),4),
            new Vector2(StaticTile.TileSort.wall_hole_2.ordinal(),3),
    };
    static public Vector2[] WallWithBannerSortPro = new Vector2[]{
            new Vector2(StaticTile.TileSort.wall_left.ordinal(),6),
            new Vector2(StaticTile.TileSort.wall_mid.ordinal(),238),
            new Vector2(StaticTile.TileSort.wall_right.ordinal(),6),
            new Vector2(StaticTile.TileSort.wall_hole_1.ordinal(),10),
            new Vector2(StaticTile.TileSort.wall_hole_2.ordinal(),7),
            new Vector2(StaticTile.TileSort.wall_banner_blue.ordinal(),12),
            new Vector2(StaticTile.TileSort.wall_banner_green.ordinal(),12),
            new Vector2(StaticTile.TileSort.wall_banner_red.ordinal(),12),
            new Vector2(StaticTile.TileSort.wall_banner_yellow.ordinal(),12),
    };

    //sample: ((1,30),(2,20),(3,30),(4,100)) means 0|..1..|30|..2..|50|..3..|80|..4..|180
    static public  Callable<Float> ProbabilityGenerator(final Vector2...vet) {
        return () -> {
            float total = 0;
            for (Vector2 v: vet) {
                total+=v.y;
            }
            total *= new Random().nextFloat();
            float boundary = 0;
            for (Vector2 v: vet) {
                boundary+=v.y;
                if(total<boundary)  return v.x;
            }
            return vet[vet.length-1].y;
        };
    }
}

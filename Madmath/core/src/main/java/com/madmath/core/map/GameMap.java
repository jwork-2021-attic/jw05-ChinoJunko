package com.madmath.core.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.Utils;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;

import java.util.Random;
import java.util.concurrent.Callable;

public class GameMap {
    GameScreen gameScreen;
    ResourceManager manager;
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer renderer;

    public GameMap(GameScreen gameScreen){
        this.gameScreen = gameScreen;
        manager = gameScreen.getGame().manager;
        initTileMap();
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public TiledMapTileSet getNewTileSet(){
        TiledMapTileSet tileSet = new TiledMapTileSet();
        for (int i = 0; i < StaticTile.TileSort.values().length; i++) {
            StaticTile staticTile = new StaticTile(manager.tiles16x16[i]);
            staticTile.setId(i);
            tileSet.putTile(i,staticTile);
        }
        StaticTile[] spikes = new StaticTile[12];
        for (int i = 0; i < manager.spikes16x16[0].length; i++) {
            StaticTile staticTile = new StaticTile(manager.spikes16x16[0][i]);
            staticTile.setId(i+StaticTile.TileSort.values().length);
            tileSet.putTile(staticTile.getId(),staticTile);
            if(i==0){
                spikes[0]=staticTile;
                spikes[11]=staticTile;
            } else if(i==manager.spikes16x16[0].length-1){
                spikes[5]=staticTile;
                spikes[6]=staticTile;
            }
            spikes[1+i]=staticTile;
            spikes[10-i]=staticTile;
        }
        Array<StaticTiledMapTile> spikeFrameArray = new Array<StaticTiledMapTile>(spikes);
        AnimTile animTile = new AnimTile(0.2f,spikeFrameArray);
        animTile.setId(StaticTile.TileSort.values().length+4+AnimTile.TileSort.floor_spikes_anim.ordinal());
        tileSet.putTile(animTile.getId(),animTile);
        return tileSet;
    }

    public void initTileMap(){
        tiledMap = new TiledMap();
        tiledMap.getTileSets().addTileSet(getNewTileSet());
        initButtomLayer();
    }

    public void initButtomLayer(){
        TiledMapTileLayer tiledMapTileLayer = new TiledMapTileLayer(120,25,16,16);
        Callable<Float> floorCallable = Utils.ProbabilityGenerator(Utils.FloorSortPro);
        Callable<Float> wallCallable = Utils.ProbabilityGenerator(Utils.WallSortPro);
        Callable<Float> bannerCallable = Utils.ProbabilityGenerator(Utils.WallWithBannerSortPro);
        for (int i = 0; i < tiledMapTileLayer.getWidth(); i++) {
            for (int j = 8; j < tiledMapTileLayer.getHeight()-3; j++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                try{
                    cell.setTile(tiledMap.getTileSets().getTile(floorCallable.call().intValue()));
                }catch (Exception e){
                    System.out.println("Some Erorr caused by Tile Loading");
                    cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.floor_1.ordinal()));
                }
                tiledMapTileLayer.setCell(i,j,cell);
            }
            for (int j = tiledMapTileLayer.getHeight()-3; j < tiledMapTileLayer.getHeight(); j++){
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                try{
                    cell.setTile(tiledMap.getTileSets().getTile(j==tiledMapTileLayer.getHeight()-2?bannerCallable.call().intValue():wallCallable.call().intValue()));
                }catch (Exception e){
                    System.out.println("Some Erorr caused by Tile Loading");
                    cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.wall_mid.ordinal()));
                }
                tiledMapTileLayer.setCell(i,j,cell);
            }
        }
        int spikes_num = 80;
        Random random = new Random();
        for (int i = 0; i < spikes_num; i++) {
            TiledMapTileLayer.Cell cell=new TiledMapTileLayer.Cell();
            cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.values().length+4+AnimTile.TileSort.floor_spikes_anim.ordinal()));
            int spikeX = random.nextInt(119)+1, spikeY = random.nextInt(14) + 8;
            tiledMapTileLayer.setCell(spikeX,spikeY,cell);
        }
        Callable<Float> enterCallable = Utils.ProbabilityGenerator(Utils.EnterSortPro);
        try{
            TiledMapTileLayer.Cell cell=new TiledMapTileLayer.Cell();
            if(enterCallable.call()==StaticTile.TileSort.floor_ladder.ordinal()){
                cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.floor_ladder.ordinal()));
                tiledMapTileLayer.setCell(random.nextInt(20)+100,random.nextInt(12)+9,cell);
            }else {
                cell.setTile(tiledMap.getTileSets().getTile(StaticTile.TileSort.floor_stair.ordinal()));
                tiledMapTileLayer.setCell(random.nextInt(5)+115,random.nextInt(12)+9,cell);
            }
        }catch (Exception e){
            System.out.println("Some Erorr caused by Tile Loading");
        }
        tiledMap.getLayers().add(tiledMapTileLayer);
    }

    public void render(float v){
        renderer.setView(gameScreen.getCamera());
        renderer.render();
    }
}

package com.madmath.core.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.madmath.core.map.AnimTile;
import com.madmath.core.map.StaticTile;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ResourceManager {
    public AssetManager assetManager;

    public TextureAtlas atlas;
    //public TextureAtlas back;

    public TextureRegion[][] knight_f_idle_anim16x28;
    public TextureRegion[][] knight_f_run_anim16x28;
    public TextureRegion[][] startbutton100x50;
    public TextureRegion[][] exitbutton100x50;
    public TextureRegion[][] fountain_red16x48;
    public TextureRegion[][] floor_spikes_anim16x16;

    public TextureRegion[] tiles16x16;


    public TextureRegion gamebackground700x128;
    public TextureRegion background700x400;
    public TextureRegion gametitle200x100;
    public TextureRegion emptybutton100x50;
    public TextureRegion ui_heart_empty16x16;

    public Skin skin;
    public Skin dialogSkin;

    public static BitmapFont font;

    public TextureRegion[][] getFloor_spikes_anim16x16() {
        return floor_spikes_anim16x16;
    }

    public TextureRegion[][] getFountain_red16x48() {
        return fountain_red16x48;
    }

    @Nullable
    public Object getAssetsByName(String name) {
        String setter = "get" + String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
        try{
            Method method = getClass().getMethod(setter);
            return method.invoke(this);
        } catch (NoSuchMethodException e){
            System.out.println("Not Such Object");
            return null;
        } catch (InvocationTargetException | IllegalAccessException e) {
            System.out.println("Not Such Method");
            e.printStackTrace();
            return null;
        }
    }

    public ResourceManager(){
        assetManager = new AssetManager();

        assetManager.load("Texture.atlas",TextureAtlas.class);
        //assetManager.load("back.atlas",TextureAtlas.class);
        assetManager.load("skins/ui.atlas", TextureAtlas.class);
        assetManager.load("skins/dialog.atlas", TextureAtlas.class);

        assetManager.finishLoading();

        atlas = assetManager.get("Texture.atlas",TextureAtlas.class);
        //back = assetManager.get("back.atlas",TextureAtlas.class);

        background700x400 = atlas.findRegion("background");
        gametitle200x100 = atlas.findRegion("gametitle");
        emptybutton100x50 = atlas.findRegion("emptybutton");
        gamebackground700x128 = atlas.findRegion("hud_background");

        /*
        gamebackground192x176 = new TextureRegion[30];
        for (int i = 0; i < 30; i++) {
            gamebackground192x176[i] = back.findRegion(Integer.toString(1+i));
        }

         */

        tiles16x16 = new TextureRegion[StaticTile.TileSort.values().length];
        int tilesIndex = 0;
        for (StaticTile.TileSort k: StaticTile.TileSort.values()) {
            String s = k.name().split("_")[k.name().split("_").length-1];
            if(s.matches("\\d+")){
                tiles16x16[tilesIndex++] = atlas.findRegion(k.name().substring(0,k.name().lastIndexOf('_')),Integer.parseInt(s));
            }else {
                tiles16x16[tilesIndex++] = atlas.findRegion(k.name());
            }
        }

        knight_f_idle_anim16x28 = atlas.findRegion("knight_f_idle_anim").split(16,28);
        knight_f_run_anim16x28 = atlas.findRegion("knight_f_run_anim").split(16,28);
        startbutton100x50 = atlas.findRegion("startbutton").split(100,50);
        exitbutton100x50 = atlas.findRegion("exitbutton").split(100,50);
        fountain_red16x48 = atlas.findRegion("wall_fountain_red_anim").split(16,48);
        floor_spikes_anim16x16 = atlas.findRegion(AnimTile.TileSort.floor_spikes_anim.name()).split(16,16);

        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), atlas.findRegion("font/font.fnt"), false);
        font.setUseIntegerPositions(false);

        skin = new Skin(assetManager.get("skins/ui.atlas", TextureAtlas.class));
        skin.add("default-font", font);
        skin.load(Gdx.files.internal("skins/ui.json"));

        dialogSkin = new Skin(assetManager.get("skins/dialog.atlas", TextureAtlas.class));
        dialogSkin.add("default-font", font);
        dialogSkin.load(Gdx.files.internal("skins/dialog.json"));
    }

    public void dispose() {
        assetManager.dispose();
        atlas.dispose();
        font.dispose();
    }
}

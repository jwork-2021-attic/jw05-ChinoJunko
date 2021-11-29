package com.madmath.core.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceManager {
    public AssetManager assetManager;

    public TextureAtlas atlas;

    public TextureRegion[][] player16x28;
    public TextureRegion[][] startbutton100x50;
    public TextureRegion[][] exitbutton100x50;

    public TextureRegion background700x400;
    public TextureRegion gametitle200x100;
    public TextureRegion emptybutton100x50;

    public static BitmapFont font;

    public ResourceManager(){
        assetManager = new AssetManager();

        assetManager.load("Texture.atlas",TextureAtlas.class);

        assetManager.finishLoading();

        atlas = assetManager.get("Texture.atlas",TextureAtlas.class);

        background700x400 = atlas.findRegion("background");
        gametitle200x100 = atlas.findRegion("gametitle");
        emptybutton100x50 = atlas.findRegion("emptybutton");

        player16x28 = atlas.findRegion("knight_f_idle_anim").split(16,28);
        startbutton100x50 = atlas.findRegion("startbutton").split(100,50);
        exitbutton100x50 = atlas.findRegion("exitbutton").split(100,50);

        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), atlas.findRegion("font/font.fnt"), false);
        font.setUseIntegerPositions(false);
    }

    public void dispose() {
        assetManager.dispose();
        atlas.dispose();
        font.dispose();
    }
}

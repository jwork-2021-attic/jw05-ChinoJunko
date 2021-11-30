package com.madmath.core.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourceManager {
    public AssetManager assetManager;

    public TextureAtlas atlas;

    public TextureRegion[][] player16x28;
    public TextureRegion[][] startbutton100x50;
    public TextureRegion[][] exitbutton100x50;

    public TextureRegion background700x400;
    public TextureRegion gametitle200x100;
    public TextureRegion emptybutton100x50;

    public Skin skin;
    public Skin dialogSkin;

    public static BitmapFont font;

    public ResourceManager(){
        assetManager = new AssetManager();

        assetManager.load("Texture.atlas",TextureAtlas.class);
        assetManager.load("skins/ui.atlas", TextureAtlas.class);
        assetManager.load("skins/dialog.atlas", TextureAtlas.class);

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

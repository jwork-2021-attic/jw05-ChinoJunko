package com.madmath.core.screen;

import com.madmath.core.main.MadMath;
import com.madmath.core.map.GameMap;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.ui.HUD;

public class GameScreen extends AbstractScreen{

    HUD hud;

    GameMap map;

    enum State{
        PAUSE,
        RUNING
    }

    public GameScreen(final MadMath game, final ResourceManager manager){
        super(game, manager);

        map = new GameMap(this);

        hud = new HUD(this, manager);

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float v) {
        super.render(v);
        map.render(v);
        stage.act(v);
        stage.draw();
    }
}

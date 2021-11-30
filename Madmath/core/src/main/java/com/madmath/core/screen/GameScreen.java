package com.madmath.core.screen;

import com.badlogic.gdx.maps.Map;
import com.madmath.core.main.MadMath;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.ui.HUD;

public class GameScreen extends AbstractScreen{

    HUD hud;

    Map map;

    enum State{
        PAUSE,
        RUNING
    }

    public GameScreen(final MadMath game, final ResourceManager manager){
        super(game, manager);

        hud = new HUD(this, manager);

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float v) {
        super.render(v);
        stage.act(v);
        stage.draw();
    }
}

package com.madmath.core.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.animation.CustomAnimation;
import com.madmath.core.map.GameMap;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;

public class Player extends Entity{

    static public AnimationManager initPlayerAnim(ResourceManager manager){
        return new AnimationManager(new CustomAnimation(0.17f,new Array<>(manager.knight_f_idle_anim16x28[0])),new CustomAnimation(0.17f,new Array<>(manager.knight_f_run_anim16x28[0])));
    }

    public Player(int id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position) {
        super(id, animationManager, gameScreen, position);
        speed = 64f;
        maxHp = 10;
        hp = 10;
        //lostSpeed = 1.8f;
    }


}

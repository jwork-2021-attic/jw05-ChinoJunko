package com.madmath.core.control;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.madmath.core.entity.Entity;
import com.madmath.core.entity.Player;

public class PlayerInputProcessor extends InputAdapter {
    Player player;

    public PlayerInputProcessor(Player player){
        this.player = player;
    }

    @Override
    public boolean keyDown(int i) {
        switch (i){
            case Input.Keys.LEFT:
            case Input.Keys.A:
                player.addAcceleration(new Vector2(-1f,0));
                return true;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                player.addAcceleration(new Vector2(1f,0));
                return true;
            case Input.Keys.UP:
            case Input.Keys.W:
                player.addAcceleration(new Vector2(0,1f));
                return true;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                player.addAcceleration(new Vector2(0,-1f));
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int i) {
        switch (i){
            case Input.Keys.LEFT:
            case Input.Keys.A:
                player.addAcceleration(new Vector2(1f,0));
                return true;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                player.addAcceleration(new Vector2(-1f,0));
                return true;
            case Input.Keys.UP:
            case Input.Keys.W:
                player.addAcceleration(new Vector2(0,-1f));
                return true;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                player.addAcceleration(new Vector2(0,1f));
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        Vector3 point = player.gameScreen.getCamera().unproject(new Vector3(i,i1,0));
        if(player.getState() == Entity.State.Stand){
            player.setAnimDirection(point.x < player.getX());
        }
        return false;
    }


    @Override
    public boolean scrolled(int i) {
        if(i==-1){
            player.gameScreen.getCamera().zoom = Math.max(player.gameScreen.getCamera().zoom-0.03f,0.5f);
        }else{
            player.gameScreen.getCamera().zoom = Math.min(player.gameScreen.getCamera().zoom+0.03f,1f);
        }
        return true;
    }

}
/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:08
*/
package com.madmath.core.thread;

import com.madmath.core.control.PlayerInputProcessor;
import com.madmath.core.entity.Player;
import com.madmath.core.screen.AbstractScreen;
import com.madmath.core.screen.GameScreen;

public class PlayerThread implements Runnable {

    Player player;

    GameScreen gameScreen;

    @Override
    public void run() {
        gameScreen = GameScreen.getCurrencyGameScreen();
        player = new Player(1000,Player.initPlayerAnim(gameScreen.getGame().manager),gameScreen);
        gameScreen.getStage().addActor(player);
        //player.setZIndex((int) player.getY());
        gameScreen.player = player;
        gameScreen.livingEntity.add(player);

        gameScreen.addInputProcessor(new PlayerInputProcessor(player));

        while (true || gameScreen.getState()!= AbstractScreen.State.END){
            try {
                gameScreen.playerSemaphore.acquire();
                player.move(gameScreen.getCurrencyDelta());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

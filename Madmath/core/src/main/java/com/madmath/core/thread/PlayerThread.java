package com.madmath.core.thread;

import com.madmath.core.control.PlayerInputProcessor;
import com.madmath.core.entity.Player;
import com.madmath.core.map.GameMap;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.util.Utils;

public class PlayerThread implements Runnable {

    Player player;

    GameScreen gameScreen;

    @Override
    public void run() {
        gameScreen = GameScreen.getCurrencyGameScreen();
        player = new Player(1000,Player.initPlayerAnim(gameScreen.getGame().manager),gameScreen,gameScreen.getMap().getPlayerSpawnPoint());
        gameScreen.getStage().addActor(player);
        //player.setZIndex((int) player.getY());
        gameScreen.player = player;
        gameScreen.livingBox.add(player);

        gameScreen.addInputProcessor(new PlayerInputProcessor(player));

        while (gameScreen.getState()!= GameScreen.State.END){
            try {
                gameScreen.playerSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            player.move(gameScreen.getCurrencyDelta());
        }
    }


}

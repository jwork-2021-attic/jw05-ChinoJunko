/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:24
*/
package com.madmath.core.thread;

import com.badlogic.gdx.utils.Array;
import com.madmath.core.entity.Monster;
import com.madmath.core.screen.GameScreen;

public class MonsterThread implements Runnable {

    GameScreen gameScreen;

    Array<Monster> monsters;

    public MonsterThread(){
        monsters = new Array<>();
    }

    @Override
    public void run() {
        gameScreen = GameScreen.getCurrencyGameScreen();

        while (gameScreen.getState()!= GameScreen.State.END){
            try {
                gameScreen.monsterSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //act monster
            monsters.forEach(monster -> {
                monster.monsterAct(gameScreen.getCurrencyDelta());
            });
        }
    }

    public void addMonster(Monster monster){
        monsters.add(monster);
    }

    public boolean removeMonster(Monster monster){
        return monsters.removeValue(monster,true);
    }
}

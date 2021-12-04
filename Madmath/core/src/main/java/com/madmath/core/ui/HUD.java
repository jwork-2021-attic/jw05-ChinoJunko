/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:17
*/
package com.madmath.core.ui;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;

public class HUD extends UI{

    Image[] fullHeart;
    Image[] halfHeart;
    Image[] voidHeart;


    public HUD(GameScreen gameScreen, ResourceManager manager){
        super(gameScreen, manager);

        fullHeart = new Image[20];
        halfHeart = new Image[20];
        voidHeart = new Image[20];
        for (int i = 0; i < 20; i++) {
            //fullHeart
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(float dt) {
        stage.act();
        stage.draw();
    }


}

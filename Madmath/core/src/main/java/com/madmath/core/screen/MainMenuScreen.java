/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:07
*/
package com.madmath.core.screen;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.actor.AnimationActor;
import com.madmath.core.animation.CustomAnimation;
import com.madmath.core.main.MadMath;
import com.madmath.core.resource.ResourceManager;



public class MainMenuScreen extends AbstractScreen {

    private final Image gametitle;
    private final ImageButton[] buttons;
    private final int buttons_num;

    public MainMenuScreen(final MadMath game, final ResourceManager manager){
        super(game, manager);
        camera.zoom = 0.5f;

        gametitle = new Image(manager.gametitle200x100);
        gametitle.setPosition(240,250);
        stage.addActor(gametitle);
        gametitle.setZIndex(1);

        Image background = new Image(manager.background700x400);
        background.setPosition(0,0);
        stage.addActor(background);
        background.setZIndex(0);

        buttons_num = 2;
        buttons = new ImageButton[buttons_num];
        //buttons[0] = new ImageButton(new TextureRegionDrawable(manager.emptybutton100x50),new TextureRegionDrawable(manager.startbutton100x50[0][0]));
        //buttons[1] = new ImageButton(new TextureRegionDrawable(manager.emptybutton100x50),new TextureRegionDrawable(manager.exitbutton100x50[0][0]));
        buttons[0] = new ImageButton(manager.startButtonStyle);
        buttons[1] = new ImageButton(manager.exitButtonStyle);
        for (int i = 0; i < buttons_num; i++) {
            buttons[i].setSize(100,50);
            stage.addActor(buttons[i]);
        }
    }

    @Override
    public void show() {
        super.show();
        resetTitle();
    }

    public void resetTitle(){
        gametitle.setVisible(true);
        gametitle.addAction(Actions.sequence(Actions.alpha(0f),Actions.delay(0.1f),Actions.alpha(1f,2f)));
        for (int i = 0; i < buttons_num; i++) {
            buttons[i].addAction(Actions.sequence(Actions.delay(0.1f),
                    Actions.moveTo(300,-100),Actions.show(),Actions.moveTo(300,185-65*i,1.1f),
                    Actions.addAction(Actions.forever(Actions.sequence(
                            Actions.moveBy(0,5,1f),
                            Actions.moveBy(0,-10,2f),
                            Actions.moveBy(0,5,1f))))));
        }
        buttons[0].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        });
        buttons[1].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.dispose();
                System.exit(0);
            }
        });
    }

    public void startGame(){
        for (int i = 0; i < buttons_num; i++) {
            buttons[i].clearActions();
            buttons[i].setVisible(false);
        }
        gametitle.setVisible(false);
        if(game.gameScreen.getState()==State.READY) game.setScreen(game.selectScreen);
        else game.setScreen(game.gameScreen);
    }

    @Override
    public void update(float v) {
    }

    @Override
    public void render(float v) {
        update(v);
        super.render(v);
        if(camera.zoom<1f)   camera.zoom+=0.005f;
        camera.update();
        stage.act(v);
        stage.draw();
    }

}

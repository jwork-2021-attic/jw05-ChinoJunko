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

    private AnimationActor player;

    private Image gametitle;
    private Image background;

    private ImageButton[] buttons;
    private Image[] buttonsFade;
    private int buttons_num;

    public MainMenuScreen(final MadMath game, final ResourceManager manager){
        super(game, manager);
        camera.zoom = 0.5f;

        gametitle = new Image(manager.gametitle200x100);
        gametitle.setPosition(240,250);
        stage.addActor(gametitle);
        gametitle.setZIndex(1);

        background = new Image(manager.background700x400);
        background.setPosition(0,0);
        stage.addActor(background);
        background.setZIndex(0);


        player = new AnimationActor(new CustomAnimation(0.1f,new Array<TextureRegion>(manager.player16x28[0])));
        player.setPlayMode(Animation.PlayMode.LOOP);
        player.setPosition(200,200);
        stage.addActor(player);

        buttons_num = 2;
        buttons = new ImageButton[buttons_num];
        buttonsFade = new Image[buttons_num];
        buttons[0] = new ImageButton(new TextureRegionDrawable(manager.emptybutton100x50),new TextureRegionDrawable(manager.startbutton100x50[0][0]));
        buttons[1] = new ImageButton(new TextureRegionDrawable(manager.emptybutton100x50),new TextureRegionDrawable(manager.exitbutton100x50[0][0]));
        buttonsFade[0] = new Image(new TextureRegionDrawable(manager.startbutton100x50[1][0]));
        buttonsFade[1] = new Image(new TextureRegionDrawable(manager.exitbutton100x50[1][0]));
        for (int i = 0; i < buttons_num; i++) {
            buttons[i].setSize(100,50);
            stage.addActor(buttonsFade[i]);
            stage.addActor(buttons[i]);
        }
    }

    @Override
    public void show() {
        //animationManager = new AnimationManager(manager.player16x28,4,0,1/4f);
        resetTitle();
    }

    public void resetTitle(){
        for (int i = 0; i < buttons_num; i++) {
            buttons[i].addAction(Actions.sequence(
                    Actions.moveTo(300,-100),Actions.moveTo(300,185-65*i,1.5f),
                    Actions.addAction(Actions.forever(Actions.sequence(
                            Actions.moveBy(0,5,1f),
                            Actions.moveBy(0,-10,2f),
                            Actions.moveBy(0,5,1f))))));
            buttonsFade[i].addAction(Actions.sequence(
                    Actions.moveTo(300,-100),Actions.moveTo(300,185-65*i,1.5f),
                    Actions.addAction(Actions.forever(Actions.sequence(
                            Actions.alpha(0.1f,1f),
                            Actions.alpha(1f,1f),
                            Actions.delay(1.5f)))),
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

    public void switchScreen(){
        for (int i = 0; i < buttons_num; i++) {
            buttons[i].clearActions();
            buttonsFade[i].clearActions();
        }
    }

    public void startGame(){
        switchScreen();
        game.setScreen(this);
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
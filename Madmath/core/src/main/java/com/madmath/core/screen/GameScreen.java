package com.madmath.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.madmath.core.entity.Player;
import com.madmath.core.main.MadMath;
import com.madmath.core.map.GameMap;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.thread.PlayerThread;
import com.madmath.core.ui.HUD;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class GameScreen extends AbstractScreen{

    static private GameScreen CurrencyGameScreen;

    HUD hud;

    GameMap map;

    InputMultiplexer multiplexer;

    ExecutorService executorService;

    public Semaphore playerSemaphore;

    public Player player;

    Label currencyMapMessage;

    State state;

    float stateTime;
    float currencyDelta;

    public State getState() {
        return state;
    }

    public enum State{
        PAUSE,
        RUNING,
        END,
    }

    public GameScreen(final MadMath game, final ResourceManager manager){
        super(game, manager);

        state = State.PAUSE;

        CurrencyGameScreen = this;

        map = new GameMap(this,"PRIMARY",1);
        initMapTitle();

        hud = new HUD(this, manager);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);

        playerSemaphore = new Semaphore(0);

        executorService = Executors.newCachedThreadPool();
        executorService.execute(new PlayerThread());
        executorService.shutdown();
    }

    @Override
    public void show() {
        super.show();
        hud.getStage().addAction(Actions.sequence(Actions.alpha(0),Actions.fadeIn(1f)));
        currencyMapMessage.setPosition(stage.getViewport().getWorldWidth()/2-50, MadMath.V_HEIGHT-20);
        stage.addActor(currencyMapMessage);
        currencyMapMessage.setZIndex(20);
        state = State.RUNING;
        CurrencyGameScreen = this;
        Gdx.input.setInputProcessor(multiplexer);
        stateTime = 0;
    }

    public void initMapTitle(){
        currencyMapMessage = new Label("LEVEL "+map.mapLevel+"  "+map.name, new Label.LabelStyle(manager.font, Color.YELLOW ));
        currencyMapMessage.setFontScale(0.5f);
        currencyMapMessage.setVisible(true);
        currencyMapMessage.addAction(Actions.sequence(Actions.delay(6f),Actions.run(new Runnable() {
            @Override
            public void run() {
                currencyMapMessage.setText("   GOOD LUCK!   ");
            }
        })));
    }

    public void updateCamera(){
        if(camera.zoom!=1f) {
            camera.position.x = Math.min(Math.max(player.getX()+(0.5f-0.382f)*camera.zoom*stage.getViewport().getWorldWidth(), 0.5f * camera.zoom * stage.getViewport().getWorldWidth()),map.playAreaSize.x-0.5f* camera.zoom*stage.getViewport().getWorldWidth());
            camera.position.y = Math.min(Math.max(player.getY(), 0.5f * camera.zoom * stage.getViewport().getWorldHeight()),(1-0.5f* camera.zoom)*stage.getViewport().getWorldHeight());
        }else {
            camera.position.x = Math.min(Math.max(player.getX()+(0.5f-0.382f)*stage.getViewport().getWorldWidth(), (float) stage.getViewport().getWorldWidth()/2),map.startPosition.x+map.playAreaSize.x-stage.getViewport().getWorldWidth()/2);
        }
        camera.update();
        //System.out.println("Playerx:"+player.getX()+"    viewportwith:"+camera.viewportWidth+"    playerArs:"+map.playAreaSize.x);
    }

    @Override
    public void update(float v) {
        super.update(v);
        updateCamera();
    }

    @Override
    public void render(float v) {
        super.render(v);
        stateTime += v;
        currencyDelta = v;
        playerSemaphore.release();
        update(v);
        map.render(v);
        stage.act(v);
        stage.draw();
        hud.render(v);
    }

    public GameMap getMap() {
        return map;
    }

    public float getCurrencyDelta() {
        return currencyDelta;
    }

    public void addInputProcessor(InputProcessor inputProcessor){
        multiplexer.addProcessor(0,inputProcessor);
    }

    public static GameScreen getCurrencyGameScreen() {
        return CurrencyGameScreen;
    }
}

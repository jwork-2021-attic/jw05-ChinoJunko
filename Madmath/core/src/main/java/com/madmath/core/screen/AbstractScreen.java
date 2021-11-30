package com.madmath.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.madmath.core.main.MadMath;
import com.madmath.core.resource.ResourceManager;

public abstract class AbstractScreen implements Screen {

    protected final MadMath game;
    protected final ResourceManager manager;

    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Stage stage;

    public AbstractScreen(final MadMath game, final ResourceManager manager){
        this.game = game;
        this.manager = manager;

        camera = new OrthographicCamera(MadMath.V_WIDTH, MadMath.V_HEIGHT);
        camera.setToOrtho(false);

        viewport = new FitViewport(MadMath.V_WIDTH, MadMath.V_HEIGHT,camera);

        stage = new Stage(viewport, game.batch);
    }

    @Override
    public void render(float v) {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void update(float v) {}

    @Override
    public void resize(int i, int i1) {
        stage.getViewport().update(i, i1);
    }

    @Override
    public void show() {
        game.fps.setPosition(MadMath.V_WIDTH-50, MadMath.V_HEIGHT-20);
        stage.addActor(game.fps);
        game.fps.setZIndex(10);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public MadMath getGame() {
        return game;
    }
}

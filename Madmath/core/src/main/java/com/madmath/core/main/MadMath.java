package com.madmath.core.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.AbstractScreen;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.screen.MainMenuScreen;

public class MadMath extends Game {

	public static final String VERSION = "0.1";
	public static final String TITLE = "MadMath v"+ VERSION;

	//Screen dimensions
	public static final int V_WIDTH = 700;
	public static final int V_HEIGHT = 400;
	public static final int V_SCALE = 1;

	public SpriteBatch batch;

	public ResourceManager manager;

	public Label fps;

	public MainMenuScreen mainMenuScreen;
	public GameScreen gameScreen;

	@Override
	public void create () {
		batch = new SpriteBatch();

		manager = new ResourceManager();
		fps = new Label("", new Label.LabelStyle(manager.font, Color.YELLOW ));
		fps.setFontScale(0.5f);
		fps.setVisible(true);

		mainMenuScreen = new MainMenuScreen(this, manager);
		gameScreen = new GameScreen(this, manager);

		setScreen(mainMenuScreen);
	}

	@Override
	public void resize (int width, int height) {
		mainMenuScreen.resize(width, height);
	}

	@Override
	public void render () {
		fps.setText(Gdx.graphics.getFramesPerSecond() + " fps");
		super.render();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}

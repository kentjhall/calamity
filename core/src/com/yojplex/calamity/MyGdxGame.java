package com.yojplex.calamity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yojplex.calamity.screens.GameScreen;

public class MyGdxGame extends Game {
	private SpriteBatch batch;
	public static float masterScale;
	private GameScreen gameScreen;

	@Override
	public void create () {
		batch = new SpriteBatch();

		masterScale=(float)Gdx.graphics.getWidth()/1440f;

		gameScreen=new GameScreen(batch);
		setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose(){
		batch.dispose();
		gameScreen.dispose();
	}
}

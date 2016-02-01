package com.yojplex.calamity.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.DropMenu;
import com.yojplex.calamity.MyGdxGame;
import com.yojplex.calamity.Player;
import com.yojplex.calamity.StrataBorder;

/**
 * Created by kenthall on 1/31/16.
 */
public class GameScreen implements Screen {
    private static SpriteBatch batch;
    private static boolean shiftStrata;
    private static boolean shiftDirection;
    private static float shiftSpeed;
    private Player player;
    private StrataBorder strataBorder1;
    private StrataBorder strataBorder2;
    private StrataBorder strataBorder3;
    private StrataBorder strataBorder4;
    private static DropMenu dropMenu;

    public GameScreen(SpriteBatch batch){
        this.batch=batch;

        player=new Player(new Vector2(0, Gdx.graphics.getHeight() * 0.75f));
        shiftSpeed=20*MyGdxGame.masterScale;

        strataBorder1=new StrataBorder(new Vector2(-Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 0.75f));
        strataBorder2=new StrataBorder(new Vector2(-Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*0.5f));
        strataBorder3=new StrataBorder(new Vector2(-Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*0.25f));
        strataBorder4=new StrataBorder(new Vector2(-Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()));

        dropMenu=new DropMenu();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        player.draw(batch);
        strataBorder1.draw(batch);
        strataBorder2.draw(batch);
        strataBorder3.draw(batch);
        strataBorder4.draw(batch);
        dropMenu.draw(batch);
        batch.end();

        if (player.getLoc().y<Gdx.graphics.getHeight() * 0.75f - 20 * MyGdxGame.masterScale || player.getLoc().y>Gdx.graphics.getHeight() * 0.75f + 20 * MyGdxGame.masterScale){
            shiftStrata=true;
        }
        else{
            shiftStrata=false;
        }

        if (player.getLoc().y<Gdx.graphics.getHeight() * 0.75f && shiftStrata){
            shiftDirection=true;
        }
        else if (player.getLoc().y>Gdx.graphics.getHeight() * 0.75f && shiftStrata){
            shiftDirection=false;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        player.dispose();
        dropMenu.dispose();
        strataBorder1.dispose();
        strataBorder2.dispose();
        strataBorder3.dispose();
        strataBorder4.dispose();
    }

    public static boolean getShiftStrata(){
        return shiftStrata;
    }

    public static boolean getShiftDirection(){
        return shiftDirection;
    }

    public static float getShiftSpeed(){
        return shiftSpeed;
    }

    public static DropMenu getDropMenu(){
        return dropMenu;
    }
}

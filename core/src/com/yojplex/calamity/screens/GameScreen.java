package com.yojplex.calamity.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.Background;
import com.yojplex.calamity.DropMenu;
import com.yojplex.calamity.Foreground;
import com.yojplex.calamity.Monster;
import com.yojplex.calamity.MyGdxGame;
import com.yojplex.calamity.Player;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by kenthall on 1/31/16.
 */
public class GameScreen implements Screen {
    private static SpriteBatch batch;
    private static boolean shiftStrata;
    private static boolean shiftDirection;
    private static float shiftSpeed;
    private static Player player;
//    private StrataBorder strataBorder1;
//    private StrataBorder strataBorder2;
//    private StrataBorder strataBorder3;
//    private StrataBorder strataBorder4;
    private static DropMenu dropMenu;
    private Background bg;
    private Foreground fg1;
    private Foreground fg2;
    private Foreground fg3;
    private Foreground fg4;
    private Foreground fg5;
    private Foreground fg6;
    private static ArrayList<Monster> monsters;

    public GameScreen(SpriteBatch batch){
        this.batch=batch;

        player=new Player(new Vector2(0, Gdx.graphics.getHeight() * 0.75f));
        shiftSpeed=20*MyGdxGame.masterScale;

//        strataBorder1=new StrataBorder(new Vector2(-Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 0.75f));
//        strataBorder2=new StrataBorder(new Vector2(-Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*0.5f));
//        strataBorder3=new StrataBorder(new Vector2(-Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*0.25f));
//        strataBorder4=new StrataBorder(new Vector2(-Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()));

        dropMenu=new DropMenu();
        fg1=new Foreground(new Vector2(0, Gdx.graphics.getHeight() * 0.75f));
        fg2=new Foreground(new Vector2(0, Gdx.graphics.getHeight() * 0.5f));
        fg3=new Foreground(new Vector2(0, Gdx.graphics.getHeight() * 0.25f));
        fg4=new Foreground(new Vector2(0, -1*Gdx.graphics.getHeight()*0.25f));
        fg5=new Foreground(new Vector2(0, Gdx.graphics.getHeight()));
        fg6=new Foreground(new Vector2(0, 0));
        bg=new Background();

        monsters=new ArrayList<Monster>();
        monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(1, 2 + 1), new Vector2(500*MyGdxGame.masterScale, Gdx.graphics.getHeight()*0.5f)));
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        bg.draw(batch);
        player.draw(batch);

        for (Monster monster:monsters){
            monster.draw(batch);
        }

        fg1.draw(batch);
        fg2.draw(batch);
        fg3.draw(batch);
        fg4.draw(batch);
        fg5.draw(batch);
        fg6.draw(batch);

//        strataBorder1.draw(batch);
//        strataBorder2.draw(batch);
//        strataBorder3.draw(batch);
//        strataBorder4.draw(batch);
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
//        strataBorder1.dispose();
//        strataBorder2.dispose();
//        strataBorder3.dispose();
//        strataBorder4.dispose();
        fg1.dispose();
        fg2.dispose();
        fg3.dispose();
        fg4.dispose();
        fg5.dispose();
        fg6.dispose();

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

    public static Player getPlayer(){
        return player;
    }

    public static ArrayList<Monster> getMonsters() {
        return monsters;
    }
}

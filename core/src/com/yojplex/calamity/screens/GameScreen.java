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
    private static DropMenu dropMenu;
    private Background bg;
    private Foreground fg1;
    private Foreground fg2;
    private Foreground fg3;
    private Foreground fg4;
    private Foreground fg5;
    private Foreground fg6;
    private static ArrayList<Monster> monsters;
    private ArrayList<Integer> monstersToRemove;
    private boolean generated;
    private ArrayList<Integer> genStrata;
    private ArrayList<Integer> genStrataToRemove;
    private static boolean monsOnStrata;

    public GameScreen(SpriteBatch batch){
        this.batch=batch;

        player=new Player(new Vector2(0, Gdx.graphics.getHeight() * 0.75f));
        shiftSpeed=30*MyGdxGame.masterScale;

        dropMenu=new DropMenu();
        fg1=new Foreground(new Vector2(0, Gdx.graphics.getHeight() * 0.75f));
        fg2=new Foreground(new Vector2(0, Gdx.graphics.getHeight() * 0.5f));
        fg3=new Foreground(new Vector2(0, Gdx.graphics.getHeight() * 0.25f));
        fg4=new Foreground(new Vector2(0, -1*Gdx.graphics.getHeight()*0.25f));
        fg5=new Foreground(new Vector2(0, Gdx.graphics.getHeight()));
        fg6=new Foreground(new Vector2(0, 0));
        bg=new Background();

        monsters=new ArrayList<Monster>();
        monstersToRemove=new ArrayList<Integer>();
        genStrataMonsters(Gdx.graphics.getHeight()*0.5f, 1, 1);
        genStrataMonsters(Gdx.graphics.getHeight()*0.25f, 1, 2);
        genStrataMonsters(0, 2, 2);
        generated=false;
        genStrata=new ArrayList<Integer>();
        genStrataToRemove=new ArrayList<Integer>();
        monsOnStrata=false;
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

        for (Monster monster:monsters){
            monster.draw(batch);
            if (monster.getCurHp()<=0){
                Integer integer = monsters.indexOf(monster);
                monstersToRemove.add(integer);
                player.setInBattle(false);
            }

            if (monster.getLoc().y<player.getLoc().y+10*MyGdxGame.masterScale && monster.getLoc().y>player.getLoc().y-10*MyGdxGame.masterScale){
                monsOnStrata=true;
            }
        }
        for (Integer integer:monstersToRemove){
            monsters.remove(integer.intValue());
        }
        monstersToRemove.clear();

        if (!monsOnStrata && genStrata.contains(player.getStrataNum()+1)){
            Integer integer=genStrata.indexOf(player.getStrataNum()+1);
            genStrataToRemove.add(integer);
        }
        for (Integer integer:genStrataToRemove){
            genStrata.remove(integer.intValue());
        }
        genStrataToRemove.clear();

        player.draw(batch);

        fg1.draw(batch);
        fg2.draw(batch);
        fg3.draw(batch);
        fg4.draw(batch);
        fg5.draw(batch);
        fg6.draw(batch);

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

        if (!shiftStrata && !generated && !genStrata.contains(player.getStrataNum())){
            genStrataMonsters(-Gdx.graphics.getHeight()*0.25f, 2, 2);
            genStrataMonsters(Gdx.graphics.getHeight(), 2, 2);
            genStrata.add(player.getStrataNum());
            generated=true;
        }

        if (shiftStrata && generated){
            generated=false;
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

    public static void genStrataMonsters(float locY, int minLvl, int maxLvl){
        ArrayList<Float> places=new ArrayList<Float>();
        places.add(225*MyGdxGame.masterScale);
        places.add(400*MyGdxGame.masterScale);
        places.add(575*MyGdxGame.masterScale);
        places.add(750*MyGdxGame.masterScale);
        places.add(925*MyGdxGame.masterScale);
        places.add(1100*MyGdxGame.masterScale);
        float place;

        int numRand = (ThreadLocalRandom.current().nextInt(18));
        int numMons = 0;
        switch (numRand){
            case 0:
                numMons=0;
                break;
            case 1:
                numMons=1;
                break;
            case 2:
                numMons=1;
                break;
            case 3:
                numMons=2;
                break;
            case 4:
                numMons=2;
                break;
            case 5:
                numMons=2;
                break;
            case 6:
                numMons=3;
                break;
            case 7:
                numMons=3;
                break;
            case 8:
                numMons=3;
                break;
            case 9:
                numMons=3;
                break;
            case 10:
                numMons=3;
                break;
            case 11:
                numMons=4;
                break;
            case 12:
                numMons=4;
                break;
            case 13:
                numMons=4;
                break;
            case 14:
                numMons=4;
                break;
            case 15:
                numMons=5;
                break;
            case 16:
                numMons=5;
                break;
            case 17:
                numMons=6;
                break;
        }

        switch (numMons){
            case 1:
                place = places.get(ThreadLocalRandom.current().nextInt(6));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));
                break;
            case 2:
                place = places.get(ThreadLocalRandom.current().nextInt(6));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(5));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));
                break;
            case 3:
                place = places.get(ThreadLocalRandom.current().nextInt(6));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(5));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(4));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));
                break;
            case 4:
                place = places.get(ThreadLocalRandom.current().nextInt(6));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(5));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(4));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(3));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));
                break;
            case 5:
                place = places.get(ThreadLocalRandom.current().nextInt(6));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(5));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(4));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(3));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(2));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));
                break;
            case 6:
                place = places.get(ThreadLocalRandom.current().nextInt(6));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(5));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(4));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(3));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(2));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));

                place = places.get(ThreadLocalRandom.current().nextInt(1));
                places.remove(place);
                monsters.add(new Monster(Monster.Type.MUSHY, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));
                break;
        }
    }

    @Override
    public void dispose() {
        player.dispose();
        dropMenu.dispose();
        fg1.dispose();
        fg2.dispose();
        fg3.dispose();
        fg4.dispose();
        fg5.dispose();
        fg6.dispose();
        bg.dispose();
        for (Monster monster:monsters){
            monster.dispose();
        }
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

    public static void setMonsOnStrata(boolean monsOnStrata1){
        monsOnStrata=monsOnStrata1;
    }
}

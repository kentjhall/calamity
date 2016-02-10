package com.yojplex.calamity.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    private ArrayList<Integer> monsKillToRemove;
    private ArrayList<Integer> monsHeightToRemove;
    private boolean generated;
    private boolean changeFgSky;
    private boolean changeFgDirt;
    private ArrayList<Monster> monsNegStrata;
    private ArrayList<Monster> monsTopStrata;
    ArrayList<Monster.Type> monsTypes;
    private BitmapFont strataFont;
    private GlyphLayout strataFontLayout;

    public GameScreen(SpriteBatch batch){
        this.batch=batch;

        player=new Player(new Vector2(0, Gdx.graphics.getHeight() * 0.75f));
        shiftSpeed=30*MyGdxGame.masterScale;

        dropMenu=new DropMenu();
        fg1 = new Foreground(new Vector2(0, Gdx.graphics.getHeight() * 0.75f), Foreground.Type.SKY);
        fg2=new Foreground(new Vector2(0, Gdx.graphics.getHeight() * 0.5f), Foreground.Type.DIRT);
        fg3=new Foreground(new Vector2(0, Gdx.graphics.getHeight() * 0.25f), Foreground.Type.DIRT);
        fg4=new Foreground(new Vector2(0, -Gdx.graphics.getHeight()*0.25f), Foreground.Type.DIRT);
        fg5=new Foreground(new Vector2(0, Gdx.graphics.getHeight()), Foreground.Type.DIRT);
        fg6=new Foreground(new Vector2(0, 0), Foreground.Type.DIRT);
        bg=new Background();

        monsters=new ArrayList<Monster>();
        monsKillToRemove =new ArrayList<Integer>();
        monsHeightToRemove=new ArrayList<Integer>();
        monsTypes=new ArrayList<Monster.Type>();
        monsTypes.add(Monster.Type.MUSHY);
        genStrataMonsters(Gdx.graphics.getHeight()*0.5f, 1, 1, monsTypes);
        genStrataMonsters(Gdx.graphics.getHeight() * 0.25f, 1, 2, monsTypes);
        genStrataMonsters(0, 2, 2, monsTypes);
        generated=false;
        monsNegStrata=new ArrayList<Monster>();
        monsTopStrata=new ArrayList<Monster>();

        strataFont=new BitmapFont(Gdx.files.internal("fonts/dmgFont/font.fnt"), Gdx.files.internal("fonts/dmgFont/font.png"), false);
        strataFontLayout=new GlyphLayout();
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

        strataFontLayout.setText(strataFont, "STRATA: " + player.getStrataNum());

        for (Monster monster:monsters){
            monster.draw(batch);
            if (monster.getCurHp()<=0){
                Integer integer = monsters.indexOf(monster);
                monsKillToRemove.add(integer);
                player.setInBattle(false);
            }
            if (monster.getLoc().y>=Gdx.graphics.getHeight()*1.5){
                Integer integer = monsters.indexOf(monster);
                monsHeightToRemove.add(integer);
            }
            checkMonsOnStrata(monster);
        }
        for (Integer integer: monsKillToRemove){
            monsters.remove(integer.intValue());
        }
        for (Integer integer: monsHeightToRemove){
            if (monsters.size()>integer.intValue()) {
                monsters.remove(integer.intValue());
            }
        }
        monsKillToRemove.clear();
        monsHeightToRemove.clear();

        player.draw(batch);
        if (player.getStrataNum()==1 && shiftStrata && !shiftDirection && changeFgSky){
            fg1.changeType(Foreground.Type.SKY);
            changeFgSky=false;
        }
        else if (fg1.getType()==Foreground.Type.SKY && player.getStrataNum()!=1 && player.getStrataNum()!=0 && !shiftStrata && changeFgDirt){
            fg1.changeType(Foreground.Type.DIRT);
            changeFgDirt=false;
        }
        if (shiftStrata && !changeFgDirt){
            changeFgDirt=true;
        }
        if (!shiftStrata && !changeFgSky){
            changeFgSky=true;
        }

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

        if (!shiftStrata && !generated){
            monsTypes.add(Monster.Type.SLIMELICK);
            if (monsNegStrata.size()==0) {
                genStrataMonsters(-Gdx.graphics.getHeight() * 0.25f, 2, 2, monsTypes);
            }
            if (monsTopStrata.size()==0) {
                if (!shiftDirection && player.getStrataNum()>2) {
                    genStrataMonsters(Gdx.graphics.getHeight(), 2, 2, monsTypes);
                }
                if (shiftDirection && player.getStrataNum()>1) {
                    genStrataMonsters(Gdx.graphics.getHeight(), 2, 2, monsTypes);
                }
            }
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

    public static void genStrataMonsters(float locY, int minLvl, int maxLvl, ArrayList<Monster.Type> monsTypes){
        ArrayList<Float> places=new ArrayList<Float>();
        places.add(225 * MyGdxGame.masterScale);
        places.add(400 * MyGdxGame.masterScale);
        places.add(575 * MyGdxGame.masterScale);
        places.add(750 * MyGdxGame.masterScale);
        places.add(925 * MyGdxGame.masterScale);
        places.add(1100 * MyGdxGame.masterScale);
        float place;
        Monster.Type monsType;

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

        for (int i=1; i<=numMons; i++){
            place = places.get(ThreadLocalRandom.current().nextInt(6-(i-1)));
            monsType=monsTypes.get(ThreadLocalRandom.current().nextInt(monsTypes.size()));
            places.remove(place);
            monsters.add(new Monster(monsType, ThreadLocalRandom.current().nextInt(minLvl, maxLvl + 1), new Vector2(place, locY)));
        }
    }

    public void checkMonsOnStrata(Monster monster){
        if (Math.abs(monster.getLoc().y - Gdx.graphics.getHeight())<10*MyGdxGame.masterScale){
            if (!monsTopStrata.contains(monster)) {
                monsTopStrata.add(monster);
            }
        }
        else if (monsTopStrata.contains(monster)){
            monsTopStrata.remove(monster);
        }

        if (Math.abs(monster.getLoc().y - -Gdx.graphics.getHeight()*0.25)<10*MyGdxGame.masterScale){
            if (!monsNegStrata.contains(monster)) {
                monsNegStrata.add(monster);
            }
        }
        else if (monsNegStrata.contains(monster)){
            monsNegStrata.remove(monster);
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
}

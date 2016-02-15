package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.screens.GameScreen;

/**
 * Created by kenthall on 1/31/16.
 */
public class DropMenu {
    private Texture dropMenu;
    private float width;
    private float height;
    private Vector2 loc;
    private float velY;
    private boolean drawerUp;
    private Texture barFill;
    private Texture expBar;
    private float hsWidth;
    private float hsHeight;
    private float esWidth;
    private float esHeight;
    private float healthPerc;
    private float expPerc;
    private BitmapFont hpFont;
    private BitmapFont menuFont1;
    private BitmapFont menuFont2;
    private GlyphLayout curHpLayout;
    private GlyphLayout expLayout;
    private BitmapFont menuFont3;
    private BitmapFont menuFont4;
    private GlyphLayout strataFontLayout;
    private GlyphLayout lvlFontLayout;
    private GestureDetector.GestureListener gestureListener;
    private float amtLvlGreen;
    private boolean turnLvlGreen;
    private int stageLvlGreen;
    private Texture addButton;

    public DropMenu(){
        dropMenu=new Texture("dropMenu1.png");
        barFill =new Texture("barFill.png");
        expBar=new Texture("expBar.png");

        width=dropMenu.getTextureData().getWidth()*10*MyGdxGame.masterScale;
        height=dropMenu.getTextureData().getHeight()*10*MyGdxGame.masterScale;
        loc=new Vector2(0, Gdx.graphics.getHeight()-height);
        hsWidth=width;
        hsHeight= barFill.getTextureData().getHeight()*MyGdxGame.masterScale;
        esWidth=width;
        esHeight= barFill.getTextureData().getHeight()*MyGdxGame.masterScale;
        drawerUp=true;
        addButton=new Texture("addButton.png");

        gestureListener=new GestureListener();
        Gdx.input.setInputProcessor(new GestureDetector(gestureListener));

        hpFont =new BitmapFont(Gdx.files.internal("fonts/menuFont/font.fnt"), Gdx.files.internal("fonts/menuFont/font.png"), false);
        curHpLayout=new GlyphLayout();
        expLayout=new GlyphLayout();
        menuFont1 =new BitmapFont(Gdx.files.internal("fonts/dmgFont/font.fnt"), Gdx.files.internal("fonts/dmgFont/font.png"), false);
        menuFont1.getData().setScale(0.75f * MyGdxGame.masterScale);
        menuFont2 =new BitmapFont(Gdx.files.internal("fonts/menuFont/font.fnt"), Gdx.files.internal("fonts/menuFont/font.png"), false);
        menuFont2.getData().setScale(0.8f * MyGdxGame.masterScale);
        menuFont3=new BitmapFont(Gdx.files.internal("fonts/dmgFont/font.fnt"), Gdx.files.internal("fonts/dmgFont/font.png"), false);
        menuFont3.getData().setScale(1.2f * MyGdxGame.masterScale);
        menuFont4 =new BitmapFont(Gdx.files.internal("fonts/gFillStrokeFont/font.fnt"), Gdx.files.internal("fonts/gFillStrokeFont/font.png"), false);
        menuFont4.getData().setScale(0.8f * MyGdxGame.masterScale);

        strataFontLayout=new GlyphLayout();
        lvlFontLayout=new GlyphLayout();

        amtLvlGreen=0;
        turnLvlGreen=false;
        stageLvlGreen=1;
    }

    public void draw(SpriteBatch batch){
        batch.draw(dropMenu,loc.x, loc.y, width, height);
        loc.y+=velY;

        if (velY<0 && loc.y<=-1690*MyGdxGame.masterScale){
            velY=0;
            loc.y=-1690*MyGdxGame.masterScale;
            drawerUp=false;
            dropMenu=new Texture("dropMenu2.png");
        }
        if (velY>0 && loc.y>=Gdx.graphics.getHeight()-height){
            velY=0;
            loc.y=Gdx.graphics.getHeight()-height;
            drawerUp=true;
            dropMenu=new Texture("dropMenu1.png");
        }

        if (turnLvlGreen){
            if (amtLvlGreen<1 && stageLvlGreen==1) {
                amtLvlGreen += 0.05;
            }
            else if (amtLvlGreen>=1 && stageLvlGreen==1){
                stageLvlGreen=2;
            }

            if (amtLvlGreen>0 && stageLvlGreen==2){
                amtLvlGreen-=0.05;
            }
            else if (amtLvlGreen<=0 && stageLvlGreen==2){
                amtLvlGreen=0;
                stageLvlGreen=3;
                turnLvlGreen=false;
            }
        }

        strataFontLayout.setText(menuFont3, "S" + GameScreen.getPlayer().getStrataNum());
        lvlFontLayout.setText(menuFont3, "LV" + GameScreen.getPlayer().getLvl(), new Color(1-amtLvlGreen, 1, 1-amtLvlGreen, 1), new GlyphLayout(menuFont3, "LV" + GameScreen.getPlayer().getLvl()).width, 0, false);
        menuFont3.draw(batch, strataFontLayout, 170 * MyGdxGame.masterScale - strataFontLayout.width / 2, loc.y + 1830 * MyGdxGame.masterScale);
        menuFont3.draw(batch, lvlFontLayout, 1270*MyGdxGame.masterScale - lvlFontLayout.width/2, loc.y + 1830 * MyGdxGame.masterScale);

        healthPerc=Math.round((float)GameScreen.getPlayer().getCurHp()/(float)GameScreen.getPlayer().getMaxHp()*100)/100f;
        hsWidth=healthPerc*770*MyGdxGame.masterScale;

        expPerc=Math.round((float) GameScreen.getPlayer().getExp() / (float) GameScreen.getPlayer().getExpReqLevel() * 100)/100f;
        esWidth=expPerc*Gdx.graphics.getWidth();

        batch.draw(expBar, 0, loc.y + 167 * MyGdxGame.masterScale, Gdx.graphics.getWidth(), expBar.getTextureData().getHeight() * 10 * MyGdxGame.masterScale);

        //hp bar bill
        if (GameScreen.getPlayer().getCurHp()>=0) {
            batch.draw(barFill, loc.x + 340 * MyGdxGame.masterScale, loc.y + 1700 * MyGdxGame.masterScale, hsWidth, hsHeight);
        }
        //exp bar fill
        batch.draw(barFill, 0, loc.y + 177 * MyGdxGame.masterScale, esWidth, esHeight);

        //draw font text
        hpFont.getData().setScale(0.8f * MyGdxGame.masterScale);
        curHpLayout.setText(hpFont, GameScreen.getPlayer().getCurHp() + "/" + GameScreen.getPlayer().getMaxHp());
        hpFont.draw(batch, curHpLayout, Gdx.graphics.getWidth() / 2 - curHpLayout.width / 2, loc.y + 1770 * MyGdxGame.masterScale);

        //draw exp text
        expLayout.setText(hpFont, GameScreen.getPlayer().getExp() + "/" + GameScreen.getPlayer().getExpReqLevel());
        hpFont.draw(batch, expLayout, Gdx.graphics.getWidth() / 2 - expLayout.width / 2, loc.y + 250 * MyGdxGame.masterScale);
        hpFont.getData().setScale(0.6f * MyGdxGame.masterScale);
        hpFont.draw(batch, "EXP", 30 * MyGdxGame.masterScale, loc.y + 245 * MyGdxGame.masterScale);

        menuFont2.draw(batch, "STATS", Gdx.graphics.getWidth() * 0.55f, loc.y + 1625 * MyGdxGame.masterScale);
        if (GameScreen.getPlayer().getUpPoints()>0){
            menuFont4.draw(batch, "" + GameScreen.getPlayer().getUpPoints(), Gdx.graphics.getWidth() * 0.853f, loc.y + 1625 * MyGdxGame.masterScale);
        }
        else{
            menuFont2.draw(batch, "" + GameScreen.getPlayer().getUpPoints(), Gdx.graphics.getWidth() * 0.853f, loc.y + 1625 * MyGdxGame.masterScale);
        }
        menuFont1.draw(batch, "ATK:" + GameScreen.getPlayer().getAtk(), Gdx.graphics.getWidth() * 0.55f, loc.y + 1470 * MyGdxGame.masterScale);
        menuFont1.draw(batch, "DEF:" + GameScreen.getPlayer().getDef(), Gdx.graphics.getWidth() * 0.55f, loc.y + 1325 * MyGdxGame.masterScale);
        menuFont1.draw(batch, "SPD:" + GameScreen.getPlayer().getSpd(), Gdx.graphics.getWidth() * 0.55f, loc.y + 1180 * MyGdxGame.masterScale);

        batch.draw(addButton, Gdx.graphics.getWidth() * 0.787f, loc.y + 1400 * MyGdxGame.masterScale, addButton.getTextureData().getWidth()*10, addButton.getTextureData().getHeight()*10);
        batch.draw(addButton, Gdx.graphics.getWidth() * 0.787f, loc.y + 1255 * MyGdxGame.masterScale, addButton.getTextureData().getWidth()*10, addButton.getTextureData().getHeight()*10);
        batch.draw(addButton, Gdx.graphics.getWidth() * 0.787f, loc.y + 1110 * MyGdxGame.masterScale, addButton.getTextureData().getWidth()*10, addButton.getTextureData().getHeight()*10);
    }

    public void dispose(){
        dropMenu.dispose();
    }

    public void setVelY(float velY){
        this.velY=velY;
    }

    public Vector2 getLoc(){
        return loc;
    }

    public boolean getDrawerUp(){
        return drawerUp;
    }

    public void setTurnLvlGreen(boolean turnLvlGreen){
        this.turnLvlGreen=turnLvlGreen;
    }

    public void setStageLvlGreen(int stageLvlGreen){
        this.stageLvlGreen=stageLvlGreen;
    }
}

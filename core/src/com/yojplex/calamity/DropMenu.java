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
    private float healthPerc;
    private BitmapFont hpFont;
    private BitmapFont menuFont1;
    private BitmapFont menuFont2;
    private GlyphLayout curHpLayout;
    private BitmapFont menuFont3;
    private GlyphLayout strataFontLayout;
    private GlyphLayout lvlFontLayout;
    private int amtLvlGreen;
    private boolean fadeLvlGreen;
    private GestureDetector.GestureListener gestureListener;

    public DropMenu(){
        dropMenu=new Texture("dropMenu1.png");
        barFill =new Texture("barFill.png");
        expBar=new Texture("expBar.png");

        loc=new Vector2(0, -125*MyGdxGame.masterScale);
        width=dropMenu.getTextureData().getWidth()*10*MyGdxGame.masterScale;
        height=dropMenu.getTextureData().getHeight()*10*MyGdxGame.masterScale;
        hsWidth=width;
        hsHeight= barFill.getTextureData().getHeight()*MyGdxGame.masterScale;
        drawerUp=true;

        gestureListener=new GestureListener();
        Gdx.input.setInputProcessor(new GestureDetector(gestureListener));

        hpFont =new BitmapFont(Gdx.files.internal("fonts/menuFont/font.fnt"), Gdx.files.internal("fonts/menuFont/font.png"), false);
        curHpLayout=new GlyphLayout();
        hpFont.getData().setScale(0.8f * MyGdxGame.masterScale);
        menuFont1 =new BitmapFont(Gdx.files.internal("fonts/dmgFont/font.fnt"), Gdx.files.internal("fonts/dmgFont/font.png"), false);
        menuFont1.getData().setScale(0.75f * MyGdxGame.masterScale);
        menuFont2 =new BitmapFont(Gdx.files.internal("fonts/menuFont/font.fnt"), Gdx.files.internal("fonts/menuFont/font.png"), false);
        menuFont2.getData().setScale(0.8f * MyGdxGame.masterScale);

        menuFont3=new BitmapFont(Gdx.files.internal("fonts/dmgFont/font.fnt"), Gdx.files.internal("fonts/dmgFont/font.png"), false);
        menuFont3.getData().setScale(1.2f*MyGdxGame.masterScale);
        strataFontLayout=new GlyphLayout();
        lvlFontLayout=new GlyphLayout();
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
        if (velY>0 && loc.y>=-125*MyGdxGame.masterScale){
            velY=0;
            loc.y=-125*MyGdxGame.masterScale;
            drawerUp=true;
            dropMenu=new Texture("dropMenu1.png");
        }

        strataFontLayout.setText(menuFont3, "S" + GameScreen.getPlayer().getStrataNum());
        lvlFontLayout.setText(menuFont3, "LV" + GameScreen.getPlayer().getLvl(), new Color(1, 1, 1, 1), new GlyphLayout(menuFont3, "LV" + GameScreen.getPlayer().getLvl()).width, 0, false);
        menuFont3.draw(batch, strataFontLayout, 170 * MyGdxGame.masterScale - strataFontLayout.width / 2, loc.y + 1830 * MyGdxGame.masterScale);
        menuFont3.draw(batch, lvlFontLayout, 1270*MyGdxGame.masterScale - lvlFontLayout.width/2, loc.y + 1830 * MyGdxGame.masterScale);

        healthPerc=Math.round((float)GameScreen.getPlayer().getCurHp()/(float)GameScreen.getPlayer().getMaxHp()*100)/100f;
        hsWidth=healthPerc*770*MyGdxGame.masterScale;

        batch.draw(expBar, 0, loc.y+125*MyGdxGame.masterScale, Gdx.graphics.getWidth(), expBar.getTextureData().getHeight()*10*MyGdxGame.masterScale);

        if (GameScreen.getPlayer().getCurHp()>=0) {
            batch.draw(barFill, loc.x + 340 * MyGdxGame.masterScale, loc.y + 1700 * MyGdxGame.masterScale, hsWidth, hsHeight);
        }

        curHpLayout.setText(hpFont, GameScreen.getPlayer().getCurHp() + "/" + GameScreen.getPlayer().getMaxHp());
        hpFont.draw(batch, curHpLayout, Gdx.graphics.getWidth() / 2 - curHpLayout.width / 2, loc.y + 1770 * MyGdxGame.masterScale);

        menuFont2.draw(batch, "STATS", Gdx.graphics.getWidth() * 0.55f, loc.y + 1625 * MyGdxGame.masterScale);
        menuFont1.draw(batch, "ATK:" + GameScreen.getPlayer().getAtk(), Gdx.graphics.getWidth() * 0.55f, loc.y + 1500 * MyGdxGame.masterScale);
        menuFont1.draw(batch, "DEF:" + GameScreen.getPlayer().getDef(), Gdx.graphics.getWidth() * 0.55f, loc.y + 1400 * MyGdxGame.masterScale);
        menuFont1.draw(batch, "SPD:" + GameScreen.getPlayer().getSpd(), Gdx.graphics.getWidth() * 0.55f, loc.y + 1300 * MyGdxGame.masterScale);
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
}

package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.screens.GameScreen;

import java.util.ArrayList;

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
    private Texture healthSeg;
    private float hsWidth;
    private float hsHeight;
    private float healthPerc;
    private BitmapFont font;
    private GlyphLayout curHpLayout;
    private GestureDetector.GestureListener gestureListener;

    public DropMenu(){
        dropMenu=new Texture("dropMenu1.png");
        healthSeg=new Texture("healthSeg.png");

        loc=new Vector2(0, -115*MyGdxGame.masterScale);
        width=dropMenu.getTextureData().getWidth()*10*MyGdxGame.masterScale;
        height=dropMenu.getTextureData().getHeight()*10*MyGdxGame.masterScale;
        hsWidth=width;
        hsHeight=healthSeg.getTextureData().getHeight()*MyGdxGame.masterScale;
        drawerUp=true;

        gestureListener=new GestureListener();
        Gdx.input.setInputProcessor(new GestureDetector(gestureListener));

        font=new BitmapFont(Gdx.files.internal("fonts/menuFont/font.fnt"), Gdx.files.internal("fonts/menuFont/font.png"), false);
        curHpLayout=new GlyphLayout();
    }

    public void draw(SpriteBatch batch){
        batch.draw(dropMenu,loc.x, loc.y, width, height);
        loc.y+=velY;

        if (velY<0 && loc.y<=-1650*MyGdxGame.masterScale){
            velY=0;
            loc.y=-1650*MyGdxGame.masterScale;
            drawerUp=false;
            dropMenu=new Texture("dropMenu2.png");
        }
        if (velY>0 && loc.y>=-115*MyGdxGame.masterScale){
            velY=0;
            loc.y=-115*MyGdxGame.masterScale;
            drawerUp=true;
            dropMenu=new Texture("dropMenu1.png");
        }

        healthPerc=Math.round((float)GameScreen.getPlayer().getCurHp()/(float)GameScreen.getPlayer().getMaxHp()*100)/100f;
        hsWidth=healthPerc*770*MyGdxGame.masterScale;

        if (GameScreen.getPlayer().getCurHp()>=0) {
            batch.draw(healthSeg, loc.x + 340 * MyGdxGame.masterScale, loc.y + 1700 * MyGdxGame.masterScale, hsWidth, hsHeight);
        }

        font.getData().setScale(0.8f);
        curHpLayout.setText(font, GameScreen.getPlayer().getCurHp() + "/" + GameScreen.getPlayer().getMaxHp());
        font.draw(batch, curHpLayout, Gdx.graphics.getWidth()/2-curHpLayout.width/2, loc.y + 1770 * MyGdxGame.masterScale);
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

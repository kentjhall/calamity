package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by kenthall on 1/31/16.
 */
public class DropMenu {
    private Texture dropMenu;
    private Rectangle dropButton;
    private float width;
    private float height;
    private Vector2 loc;
    private float velY;
    private float dropButWidth;
    private float dropButHeight;
    private boolean drawerUp;
    private GestureDetector.GestureListener gestureListener;

    public DropMenu(){
        dropMenu=new Texture("dropMenu1.png");

        loc=new Vector2(0, -115*MyGdxGame.masterScale);
        width=dropMenu.getTextureData().getWidth()*10*MyGdxGame.masterScale;
        height=dropMenu.getTextureData().getHeight()*10*MyGdxGame.masterScale;
        drawerUp=true;

        dropButWidth=230*MyGdxGame.masterScale;
        dropButHeight=80*MyGdxGame.masterScale;
        dropButton=new Rectangle(Gdx.graphics.getWidth()/2 - dropButWidth/2, Gdx.graphics.getHeight()*0.25f - dropButHeight/2, dropButWidth, dropButHeight);

        gestureListener=new GestureListener();
        Gdx.input.setInputProcessor(new GestureDetector(gestureListener));
    }

    public void draw(SpriteBatch batch){
        batch.draw(dropMenu,loc.x, loc.y, width, height);
        loc.y+=velY;

        if (dropButton.contains(Gdx.input.getX(), Gdx.input.getY()) && Gdx.input.isTouched()){
            velY=-25;
        }

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
    }

    public void setVelY(float velY){
        this.velY=velY;
    }

    public boolean getDrawerUp(){
        return drawerUp;
    }

    public void dispose(){
        dropMenu.dispose();
    }
}

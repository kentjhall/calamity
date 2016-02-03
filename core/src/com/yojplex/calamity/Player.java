package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.screens.GameScreen;

/**
 * Created by kenthall on 1/31/16.
 */
public class Player {
    private Texture pTexture;
    private float width;
    private float height;
    private Vector2 loc;
    private Vector2 vel;
    private int strataNum;
    private boolean strataChange;
    private Rectangle hitBox;
    private boolean inBattle;
    private int atk;
    private int def;
    private int acc;
    private int spd;
    private int luk;

    public Player(Vector2 loc){
        pTexture=new Texture("player/dGodR_0.png");
        inBattle=false;

        width=150*MyGdxGame.masterScale;
        height=220*MyGdxGame.masterScale;

        this.loc=loc;
        vel=new Vector2(0, 0);
        hitBox=new Rectangle(loc.x, loc.y, width, height);
    }

    public void draw(SpriteBatch batch){
        batch.draw(pTexture, loc.x, loc.y, width, height);
        loc.x+=vel.x;
        loc.y+=vel.y;
        hitBox.set(loc.x, loc.y, width, height);

        System.out.println(inBattle);
        if (Gdx.input.getX()<Gdx.graphics.getWidth()/2 && Gdx.input.isTouched() && !inBattle){
            if (strataNum>0) {
                vel.x = -10;
                pTexture=new Texture("player/dGodL_0.png");
            }
            else if (loc.x>0){
                vel.x = -10;
                pTexture=new Texture("player/dGodL_0.png");
            }
            else{
                vel.x=0;
            }
        }
        else if (Gdx.input.getX()>Gdx.graphics.getWidth()/2 && Gdx.input.isTouched() && !inBattle){
            vel.x=10;
            pTexture=new Texture("player/dGodR_0.png");
        }
        else{
            vel.x=0;
        }

        if (loc.x>Gdx.graphics.getWidth()){
            loc.x=-width;
            loc.y=Gdx.graphics.getHeight()*0.5f;
        }

        if (loc.x<-width){
            loc.x = Gdx.graphics.getWidth();
            loc.y = Gdx.graphics.getHeight();
        }

        if (GameScreen.getShiftStrata()){
            if (GameScreen.getShiftDirection()) {
                vel.y=GameScreen.getShiftSpeed();
                strataChange=true;
            }
            else{
                vel.y=-GameScreen.getShiftSpeed();
                strataChange=true;
            }
        }
        else if (vel.y!=0){
            vel.y=0;
            loc.y=Gdx.graphics.getHeight()*0.75f;
        }

        if (strataChange && vel.y==0){
            if (GameScreen.getShiftDirection()) {
                strataNum++;
            }
            else{
                strataNum--;
            }
            strataChange=false;
        }
    }

    public void dispose(){
        pTexture.dispose();
    }

    public Vector2 getLoc(){
        return loc;
    }

    public int getStrataNum(){
        return strataNum;
    }

    public Rectangle getHitBox(){
        return hitBox;
    }

    public void setInBattle(boolean inBattle){
        this.inBattle=inBattle;
    }

    public boolean getInBattle(){
        return inBattle;
    }

    public float getWidth(){
        return width;
    }
}

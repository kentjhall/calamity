package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.yojplex.calamity.screens.GameScreen;

/**
 * Created by kenthall on 1/31/16.
 */
public class Player {
    private Texture pTexture;
    private TextureRegion wTexture;
    private int wRotAngle;
    private float width;
    private float height;
    private float wWidth;
    private float wHeight;
    private Vector2 loc;
    private Vector2 wLoc;
    private Vector2 vel;
    private int strataNum;
    private boolean strataChange;
    private Rectangle hitBox;
    private boolean inBattle;
    private int atk;
    private int def;
    private int spd;
    private int maxHp;
    private int curHp;
    private int lvl;
    private int exp;
    private long attackTime;
    private boolean doAttack;

    public Player(Vector2 loc){
        pTexture=new Texture("player/heroR_0.png");
        wTexture=new TextureRegion(new Texture("weapons/hEdgeR.png"));
        wRotAngle = 90;

        width=150*MyGdxGame.masterScale;
        height=220*MyGdxGame.masterScale;

        wWidth=wTexture.getTexture().getTextureData().getWidth()*10*MyGdxGame.masterScale;
        wHeight=wTexture.getTexture().getTextureData().getHeight()*10*MyGdxGame.masterScale;

        this.loc=loc;
        wLoc=new Vector2(loc.x+130*MyGdxGame.masterScale, loc.y+80*MyGdxGame.masterScale);
        vel=new Vector2(0, 0);
        hitBox=new Rectangle(loc.x, loc.y, width, height);
        lvl = 1;
        maxHp=lvl*5;
        curHp=maxHp;
        atk=lvl*5;
        spd=lvl*1;

        attackTime= TimeUtils.nanoTime();
    }

    public void draw(SpriteBatch batch){
        batch.draw(pTexture, loc.x, loc.y, width, height);
        batch.draw(wTexture, wLoc.x, wLoc.y-120*MyGdxGame.masterScale, 0, wHeight, wWidth, wHeight, 1, 1, wRotAngle, true);
        //update pos
        loc.x+=vel.x;
        loc.y+=vel.y;
        //update hitbox
        hitBox.set(loc.x, loc.y, width, height);
        //update sword pos y
        wLoc.y=loc.y+80*MyGdxGame.masterScale;

        //when input is to move left and not in battle
        if (Gdx.input.getX()<Gdx.graphics.getWidth()/2 && Gdx.input.isTouched() && !inBattle){
            //make sure player does not go behind strata 0
            if (strataNum>0) {
                //player go left
                //change texture of player and sword to facing left
                //update sword pos relative to player
                vel.x = -10;
                pTexture=new Texture("player/heroL_0.png");
                wTexture=new TextureRegion(new Texture("weapons/hEdgeL.png"));
                wLoc.x=loc.x-100*MyGdxGame.masterScale;
            }
            else if (loc.x>0){
                //player go left
                //change texture of player and sword to facing left
                //update sword pos relative to player
                vel.x = -10;
                pTexture=new Texture("player/heroL_0.png");
                wTexture=new TextureRegion(new Texture("weapons/hEdgeL.png"));
                wLoc.x=loc.x-100*MyGdxGame.masterScale;
            }
            else{
                //stop player when conditions not met
                vel.x=0;
            }
        }
        //when input is to move right and not in battle
        else if (Gdx.input.getX()>Gdx.graphics.getWidth()/2 && Gdx.input.isTouched() && !inBattle){
            //player go right
            //change texture of player and sword to facing left
            //update sword pos relative to player
            vel.x=10;
            pTexture=new Texture("player/heroR_0.png");
            wTexture=new TextureRegion(new Texture("weapons/hEdgeR.png"));
            wLoc.x=loc.x+130*MyGdxGame.masterScale;
        }
        else{
            //stop player when conditions not met
            vel.x=0;
        }

        //wraps player to other side and down strata if move right off screen
        if (loc.x>Gdx.graphics.getWidth()){
            loc.x=-width;
            loc.y=Gdx.graphics.getHeight()*0.5f;
        }

        //wraps player to other side and up strata if move left off screen
        if (loc.x<-width){
            loc.x = Gdx.graphics.getWidth();
            loc.y = Gdx.graphics.getHeight();
        }

        //changes player velocity to up or down, depending on which way strata is shifting
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
            //stops motion when GameScreen.shiftStrata is no longer true, accounts for any change in position
            vel.y=0;
            loc.y=Gdx.graphics.getHeight()*0.75f;
        }

        //checks if strata was changing, but stopped
        if (strataChange && vel.y==0){
            //update number of strata
            if (GameScreen.getShiftDirection()) {
                strataNum++;
            }
            else{
                strataNum--;
            }
            strataChange=false;
        }

        //battle sequence in progress *update*
        if (inBattle){
            if (TimeUtils.timeSinceNanos(attackTime)>TimeUtils.millisToNanos(2000/spd)){

            }
        }

        if (doAttack){
            attack();
        }
    }

    public void attack(){

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

    public int getCurHp(){
        return curHp;
    }

    public int getMaxHp(){
        return maxHp;
    }

    public void setCurHp(int curHp){
        this.curHp=curHp;
    }
}

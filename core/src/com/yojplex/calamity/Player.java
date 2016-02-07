package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.yojplex.calamity.screens.GameScreen;

import java.util.ArrayList;

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
    private boolean doAttack;
    private int attackStage;
    private boolean sigAttackHit;
    private boolean monsToRight;
    private boolean facingRight;
    private float initAtkLocX;
    private boolean monsAtk;
    private boolean recoiling;

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
        atk=1;
        spd=1;
        def=1;
        facingRight=true;
    }

    public void draw(SpriteBatch batch){
        batch.draw(pTexture, loc.x, loc.y, width, height);
        if (facingRight) {
            batch.draw(wTexture, wLoc.x, wLoc.y - 120 * MyGdxGame.masterScale, 0, wHeight, wWidth, wHeight, 1, 1, wRotAngle, true);
        }
        else{
            batch.draw(wTexture, wLoc.x, wLoc.y, 0, 0, wWidth, wHeight, 1, 1, wRotAngle, true);
        }
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
            if (strataNum>0 && !inBattle) {
                //player go left
                //change texture of player and sword to facing left
                //update sword pos relative to player
                vel.x = -10;
                pTexture=new Texture("player/heroL_0.png");
                wTexture=new TextureRegion(new Texture("weapons/hEdgeL.png"));
                facingRight=false;
            }
            else if (loc.x>0){
                //player go left
                //change texture of player and sword to facing left
                //update sword pos relative to player
                vel.x = -10;
                pTexture=new Texture("player/heroL_0.png");
                wTexture=new TextureRegion(new Texture("weapons/hEdgeL.png"));
                facingRight=false;
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
            facingRight=true;
        }
        else{
            //stop player when conditions not met
            vel.x=0;
        }

        //constrain sword position to player based on direction
        if (facingRight){
            wLoc.x=loc.x+130*MyGdxGame.masterScale;
        }
        else{
            wLoc.x=loc.x+20*MyGdxGame.masterScale;
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
            GameScreen.setMonsOnStrata(false);
            strataChange=false;
        }

        //when in battle, every specified number of seconds (based on spd stat)
        if (inBattle){
            //if player touches right and monster is to the right, initiate attack
            if (Gdx.input.getX()>Gdx.graphics.getWidth()/2 && Gdx.input.isTouched() && !doAttack && monsToRight){
                //initiate attack sequence for one hit
                //reset time until next hit
                //attackStage is for regulating each part of the hit
                doAttack=true;
                attackStage=1;
            }

            //if player touches left and monster is to the left, initiate attack
            if (Gdx.input.getX()<Gdx.graphics.getWidth()/2 && Gdx.input.isTouched() && !doAttack && !monsToRight){
                //initiate attack sequence for one hit
                //reset time until next hit
                //attackStage is for regulating each part of the hit
                doAttack=true;
                attackStage=1;
            }
        }

        //initiate attack method when doAttack is true, allows control for when attack() is running
        //if monster is attacking, do not attack
        if (doAttack && !monsAtk){
            attack();
        }
        else if (monsAtk){
            //reset to default values if monster is attacking
            wRotAngle = 90;
            attackStage = 0;
            doAttack = false;
        }
    }

    public void attack(){
        int hitSpeed=18;

        if (facingRight) {
            //go up to 140 degree angle to start strike
            if (wRotAngle < 140 && attackStage == 1) {
                wRotAngle = 140;
                initAtkLocX =loc.x;
            } else if (wRotAngle == 140 && attackStage == 1) {
                attackStage = 2;
            }

            //strike through to 45 degrees
            if (wRotAngle > 45 && attackStage == 2) {
                wRotAngle -= hitSpeed;
                vel.x=4;
            } else if (wRotAngle <= 45 && attackStage == 2) {
                sigAttackHit = true;
                attackStage = 3;
            }

            //bring sword back up to 90 degrees
            if (wRotAngle < 90 && attackStage == 3) {
                wRotAngle += hitSpeed;
                vel.x=-4;
            } else if (wRotAngle >= 90 && attackStage == 3) {
                //reset angle and attackStage
                //stop attack() loop through doAttack
                wRotAngle = 90;
                attackStage = 0;
                doAttack = false;
                vel.x=0;
                loc.x= initAtkLocX;
            }
        }
        else{
            //go down to 50 degree angle to start strike (turns sword up, since sword is facing left)
            if (wRotAngle > 50 && attackStage == 1) {
                wRotAngle=50;
                initAtkLocX =loc.x;
            } else if (wRotAngle == 50 && attackStage == 1) {
                attackStage = 2;
            }

            //strike through to 135 degrees
            if (wRotAngle < 135 && attackStage == 2) {
                wRotAngle += hitSpeed;
                vel.x=-4;
            } else if (wRotAngle >= 135 && attackStage == 2) {
                sigAttackHit = true;
                attackStage = 3;
            }

            //bring sword back down to 90 degrees (turns sword back up, since sword is facing left)
            if (wRotAngle > 90 && attackStage == 3) {
                wRotAngle -= hitSpeed;
                vel.x=4;
            } else if (wRotAngle <= 90 && attackStage == 3) {
                //reset angle and attackStage
                //stop attack() loop through doAttack
                wRotAngle = 90;
                attackStage = 0;
                doAttack = false;
                vel.x=0;
                loc.x= initAtkLocX;
            }
        }
    }

    public void recoil(long startTime, float initLocX, int recoilTime){
        if (TimeUtils.timeSinceNanos(startTime)<TimeUtils.millisToNanos(recoilTime/2)){
            if (monsToRight){
                vel.x=-8;
            }
            else{
                vel.x=8;
            }
        }
        else if (TimeUtils.timeSinceNanos(startTime)<TimeUtils.millisToNanos(recoilTime)){
            if (monsToRight){
                vel.x=8;
            }
            else{
                vel.x=-8;
            }
        }
        else{
            vel.x=0;
            loc.x=initLocX;
            recoiling=false;
        }
    }

    public void takeDmg(int dmg){
        curHp-=(dmg-def);
        if (curHp<0){
            curHp=0;
        }
        GameScreen.getDropMenu().getPDmgNums().add(dmg - def);
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

    public boolean getSigAttackHit(){
        return sigAttackHit;
    }

    public void setSigAttackHit(boolean sigAttackHit){
        this.sigAttackHit=sigAttackHit;
    }

    public int getAtk(){
        return atk;
    }

    public void setMonsToRight(boolean monsToRight){
        this.monsToRight=monsToRight;
    }

    public void setLocX(float locX){
        this.loc.x=locX;
    }

    public void setVelX(float velX){
        this.vel.x=velX;
    }

    public float getVelX(){
        return vel.x;
    }

    public void setMonsAtk(boolean monsAtk){
        this.monsAtk=monsAtk;
    }

    public boolean getDoAttack(){
        return doAttack;
    }

    public boolean getRecoiling(){
        return recoiling;
    }

    public void setRecoiling(boolean recoiling){
        this.recoiling=recoiling;
    }
}

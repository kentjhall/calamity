package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.yojplex.calamity.screens.GameScreen;

import java.lang.reflect.Array;
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
    private int moveSpd;
    private int lvl;
    private int exp;
    private int expReqLevel;
    private boolean doAttack;
    private int attackStage;
    private boolean sigAttackHit;
    private boolean monsToRight;
    private boolean facingRight;
    private float initAtkLocX;
    private boolean monsAtk;
    private boolean recoiling;
    private BitmapFont font;
    private ArrayList<GlyphLayout> dmgLayout;
    private ArrayList<String> dmgNums;
    private ArrayList<Integer> dmgNumsToRemove;
    private ArrayList<Float> dmgAlphaNums;
    private ArrayList<Integer> dmgFadeStage;
    private long healTime;
    private boolean drawLvlUp;
    private int upPoints;
    private boolean butPressed;

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
        exp=0;
        maxHp=lvl*5;
        curHp=maxHp;
        atk=1;
        spd=1;
        def=1;
        facingRight=true;
        healTime=TimeUtils.nanoTime();
        upPoints=0;

        font=new BitmapFont(Gdx.files.internal("fonts/dmgFont/font.fnt"), Gdx.files.internal("fonts/dmgFont/font.png"), false);
        font.getData().setScale(MyGdxGame.masterScale);

        dmgLayout=new ArrayList<GlyphLayout>();
        dmgNums =new ArrayList<String>();
        dmgNumsToRemove =new ArrayList<Integer>();
        dmgAlphaNums =new ArrayList<Float>();
        dmgFadeStage=new ArrayList<Integer>();
    }

    public void draw(SpriteBatch batch){
        batch.draw(pTexture, loc.x, loc.y, width, height);

        maxHp=lvl*5;
        moveSpd=spd+4;
        expReqLevel=lvl*20;
        if (exp>=expReqLevel){
            lvl++;
            upPoints+=2;
            drawLvlUp=true;
            exp=0;
        }
        if (drawLvlUp && dmgNums.size()==0){
            dmgNums.add("LVL UP");
            GameScreen.getDropMenu().setTurnLvlGreen(true);
            GameScreen.getDropMenu().setStageLvlGreen(1);
            drawLvlUp=false;
        }

        if (TimeUtils.timeSinceNanos(healTime)>TimeUtils.millisToNanos(2000)){
            if (curHp<maxHp){
                curHp++;
            }
            healTime=TimeUtils.nanoTime();
        }

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

        //when input is to move left and not in battle and no button is pressed on screen
        if (Gdx.input.getX()<Gdx.graphics.getWidth()/2 && Gdx.input.isTouched() && !inBattle && !butPressed){
            //make sure player does not go behind strata 0
            if (strataNum>0) {
                //player go left
                //change texture of player and sword to facing left
                //update sword pos relative to player
                vel.x = -moveSpd;
                pTexture=new Texture("player/heroL_0.png");
                wTexture=new TextureRegion(new Texture("weapons/hEdgeL.png"));
                facingRight=false;
            }
            else if (loc.x>0){
                //player go left
                //change texture of player and sword to facing left
                //update sword pos relative to player
                vel.x = -moveSpd;
                pTexture=new Texture("player/heroL_0.png");
                wTexture=new TextureRegion(new Texture("weapons/hEdgeL.png"));
                facingRight=false;
            }
            else{
                //stop player when conditions not met
                vel.x=0;
            }

            //stops player from moving back when strata is shifting
            if (GameScreen.getShiftStrata() && GameScreen.getShiftDirection()){
                vel.x=0;
            }
        }
        //when input is to move right and not in battle and no button is pressed on screen
        else if (Gdx.input.getX()>Gdx.graphics.getWidth()/2 && Gdx.input.isTouched() && !inBattle && !butPressed){
            //player go right
            //change texture of player and sword to facing left
            //update sword pos relative to player
            vel.x=moveSpd;
            pTexture=new Texture("player/heroR_0.png");
            wTexture=new TextureRegion(new Texture("weapons/hEdgeR.png"));
            facingRight=true;

            //stops player from moving back when strata is shifting
            if (GameScreen.getShiftStrata() && !GameScreen.getShiftDirection()){
                vel.x=0;
            }
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
        if (loc.x>Gdx.graphics.getWidth() && !monsAtk){
            loc.x=-width;
            loc.y=Gdx.graphics.getHeight()*0.5f;
        }

        //wraps player to other side and up strata if move left off screen
        if (loc.x<-width && !monsAtk){
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
        printDmgNums(batch);
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
        if (!drawLvlUp) {
            dmgNums.add("" + (dmg - def));
        }
    }

    public void printDmgNums(SpriteBatch batch){
        for (int i=0; i<dmgNums.size(); i++){
            if (dmgAlphaNums.size()< dmgNums.size()) {
                dmgAlphaNums.add(i, 0f);
                dmgFadeStage.add(i, 1);
                dmgLayout.add(i, new GlyphLayout());
            }
            dmgLayout.get(i).setText(font, dmgNums.get(i), new Color(1, 1, 1, dmgAlphaNums.get(i)), new GlyphLayout(font, dmgNums.get(i)).width, 0, false);
            if (dmgAlphaNums.get(i)<1 && dmgFadeStage.get(i)==1) {
                dmgAlphaNums.set(i, dmgAlphaNums.get(i)+0.05f);
            }
            else if (dmgAlphaNums.get(i)>=1){
                dmgFadeStage.set(i, 2);
            }

            if (dmgAlphaNums.get(i)>0 && dmgFadeStage.get(i)==2) {
                dmgAlphaNums.set(i, dmgAlphaNums.get(i)-0.05f);
            }
            else if (dmgAlphaNums.get(i)<=0){
                dmgFadeStage.set(i, 3);
            }

            if (dmgFadeStage.get(i)==3){
                dmgAlphaNums.set(i, 0f);
                dmgNumsToRemove.add(i);
            }

            if (dmgFadeStage.get(i)==1) {
                font.draw(batch, dmgLayout.get(i), (loc.x + width/2) - dmgLayout.get(i).width/2, loc.y + 225 * MyGdxGame.masterScale + dmgAlphaNums.get(i) * 100f * MyGdxGame.masterScale);
            }
            else{
                font.draw(batch, dmgLayout.get(i), (loc.x + width / 2) - dmgLayout.get(i).width / 2, loc.y + 225 * MyGdxGame.masterScale + 100f * MyGdxGame.masterScale);
            }
        }
        for (Integer integer: dmgNumsToRemove){
            dmgNums.remove(integer.intValue());
            dmgAlphaNums.remove(integer.intValue());
            dmgFadeStage.remove(integer.intValue());
            dmgLayout.remove(integer.intValue());
        }
        dmgNumsToRemove.clear();
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

    public void setExp(int exp){
        this.exp=exp;
    }

    public int getExp(){
        return exp;
    }

    public int getDef(){
        return def;
    }

    public int getSpd(){
        return spd;
    }

    public int getLvl(){
        return lvl;
    }

    public int getExpReqLevel(){
        return expReqLevel;
    }

    public int getUpPoints(){
        return upPoints;
    }

    public void setUpPoints(int upPoints){
        this.upPoints=upPoints;
    }

    public void setAtk(int atk){
        this.atk=atk;
    }

    public void setDef(int def){
        this.def=def;
    }

    public void setSpd(int spd){
        this.spd=spd;
    }

    public void setButPressed(boolean butPressed){
        this.butPressed=butPressed;
    }
}

package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.yojplex.calamity.screens.GameScreen;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by kenthall on 2/1/16.
 */
public class Monster {
    public enum Type {
        MUSHY, SLIMELICK
    }
    private Animation monsAnimation;
    private TextureRegion[] monsTexture;
    private TextureRegion monsFrame;
    private float monsStateTime;
    private int atk;
    private int dur;
    private int maxHp;
    private int curHp;
    private int spd;
    private Vector2 loc;
    private Vector2 vel;
    private float width;
    private float height;
    private float initLocX;
    private Rectangle hitBox;
    private boolean inBattle;
    private long attackTime;
    private boolean doAttackR;
    private boolean doAttackL;
    private int attackStage;
    private boolean attackHit;
    private boolean upAtkTime1;
    private boolean upAtkTime2;
    private boolean doPlayerRecoil;
    private long pRStartTime;
    private float pRInitLocX;
    private boolean setPRInitLocX;
    private boolean setPRStartTime;
    private Texture healthBar;
    private Texture healthSeg;
    private float healthPerc;
    private float initLocY;

    public Monster(Type type, int lvl, Vector2 loc){
        switch (type){
            case MUSHY:
                monsTexture=new TextureRegion[2];
                for (int i=0; i<monsTexture.length; i++){
                    monsTexture[i]=new TextureRegion(new Texture("monsters/mushy/mushy_"+ (i+1) +".png"));
                }
                monsAnimation=new Animation(1f, monsTexture[0], monsTexture[1]);
                break;
            case SLIMELICK:
                break;
        }
        monsStateTime=0f;
        attackTime= TimeUtils.nanoTime();
        upAtkTime1=false;
        upAtkTime2=true;

        atk = ThreadLocalRandom.current().nextInt(1, (lvl * 3 - 2) + 1);
        dur = ThreadLocalRandom.current().nextInt(1, (lvl * 3 - 1 - atk) + 1);
        spd = lvl*3 - (dur + atk);

        maxHp = dur*3;
        curHp=maxHp;
        healthBar=new Texture("healthBar.png");
        healthSeg=new Texture("healthSeg.png");

        this.loc=loc;
        initLocX=loc.x;
        width=150*MyGdxGame.masterScale;
        height=150*MyGdxGame.masterScale;
        vel=new Vector2(0, 0);
        hitBox=new Rectangle(loc.x-width/2f, loc.y, width*2f, height);
        setPRInitLocX=true;
        initLocY=loc.y;
    }

    public void draw(SpriteBatch batch){
        monsStateTime+= Gdx.graphics.getDeltaTime();
        monsFrame=monsAnimation.getKeyFrame(monsStateTime, true);
        batch.draw(monsFrame, loc.x, loc.y, width, height);
        loc.x+=vel.x;
        loc.y+=vel.y;
        hitBox.set(loc.x - width / 2f, loc.y, width * 2f, height);
        healthPerc=Math.round((float)curHp/(float)maxHp*100)/100f;

        if (GameScreen.getShiftStrata()){
            if (GameScreen.getShiftDirection()) {
                vel.y=GameScreen.getShiftSpeed();
            }
            else{
                vel.y=-GameScreen.getShiftSpeed();
            }
        }
        else if (vel.y!=0) {
            vel.y = 0;
            if (GameScreen.getShiftDirection()) {
                loc.y = initLocY + Gdx.graphics.getHeight() * 0.25f;
                initLocY = loc.y;
            }
            else {
                loc.y = initLocY - Gdx.graphics.getHeight() * 0.25f;
                initLocY = loc.y;
            }
        }

        if (hitBox.overlaps(GameScreen.getPlayer().getHitBox())){
            batch.draw(healthBar, loc.x, loc.y+200*MyGdxGame.masterScale, width, healthBar.getTextureData().getHeight()*2*MyGdxGame.masterScale);
            batch.draw(healthSeg, loc.x, loc.y+200*MyGdxGame.masterScale, healthPerc*width, healthBar.getTextureData().getHeight()*2*MyGdxGame.masterScale);
            GameScreen.getPlayer().setInBattle(true);
            inBattle=true;
            upAtkTime1=true;
            if (setPRInitLocX){
                pRInitLocX=GameScreen.getPlayer().getLoc().x;
                setPRInitLocX=false;
            }

            if (loc.x>GameScreen.getPlayer().getLoc().x){
                GameScreen.getPlayer().setMonsToRight(true);
            }
            else if (loc.x<GameScreen.getPlayer().getLoc().x){
                GameScreen.getPlayer().setMonsToRight(false);
            }
        }

        if (loc.x>GameScreen.getPlayer().getLoc().x && Gdx.input.getX()<Gdx.graphics.getWidth()/2 && loc.y>GameScreen.getPlayer().getLoc().y-10*MyGdxGame.masterScale && loc.y<GameScreen.getPlayer().getLoc().y+10*MyGdxGame.masterScale){
            GameScreen.getPlayer().setInBattle(false);
            if (inBattle){
                inBattle=false;
                upAtkTime2=true;
            }
        }
        if (loc.x<GameScreen.getPlayer().getLoc().x && Gdx.input.getX()>Gdx.graphics.getWidth()/2 && loc.y>GameScreen.getPlayer().getLoc().y-10*MyGdxGame.masterScale && loc.y<GameScreen.getPlayer().getLoc().y+10*MyGdxGame.masterScale){
            GameScreen.getPlayer().setInBattle(false);
            if (inBattle){
                inBattle=false;
                upAtkTime2=true;
            }
        }

        if (upAtkTime1 && upAtkTime2){
            attackTime=TimeUtils.nanoTime();
            upAtkTime2=false;
        }

        if (inBattle){
            if (TimeUtils.timeSinceNanos(attackTime)>TimeUtils.millisToNanos(2000/spd) && !GameScreen.getPlayer().getDoAttack()){
                if (loc.x>GameScreen.getPlayer().getLoc().x) {
                    doAttackR = true;
                    doAttackL=false;
                }
                else if (loc.x<GameScreen.getPlayer().getLoc().x){
                    doAttackL=true;
                    doAttackR=false;
                }
                attackStage=1;
                attackHit=true;
                attackTime=TimeUtils.nanoTime();
                GameScreen.getPlayer().setMonsAtk(true);
                GameScreen.getPlayer().setVelX(0);
                setPRStartTime=true;
            }

            if (GameScreen.getPlayer().getSigAttackHit()){
                curHp=curHp-GameScreen.getPlayer().getAtk();
                GameScreen.getPlayer().setSigAttackHit(false);
            }
        }
        if (doAttackR){
            attack(true);
        }
        if (doAttackL){
            attack(false);
        }
        if (doPlayerRecoil){
            GameScreen.getPlayer().setRecoiling(true);
            GameScreen.getPlayer().recoil(pRStartTime, pRInitLocX, 150);
            if (!GameScreen.getPlayer().getRecoiling()){
                doPlayerRecoil=false;
            }
        }

    }

    public void attack(boolean directionR){
        if (directionR) {
            if (loc.x > pRInitLocX + GameScreen.getPlayer().getWidth() / 2 && attackStage == 1) {
                vel.x = -10;
            } else if (loc.x < initLocX && attackHit) {
                attackStage = 2;
                vel.x = 10;
                GameScreen.getPlayer().takeDmg(atk);
                attackHit = false;
                if (setPRStartTime) {
                    pRStartTime = TimeUtils.nanoTime();
                    setPRStartTime=false;
                }
                doPlayerRecoil=true;
            } else if (loc.x >= initLocX) {
                vel.x = 0;
                doAttackR = false;
                GameScreen.getPlayer().setMonsAtk(false);
            }
        }
        else if (!directionR){
            if (loc.x < pRInitLocX - GameScreen.getPlayer().getWidth() / 2 && attackStage == 1) {
                vel.x = 10;
            } else if (loc.x > initLocX && attackHit) {
                attackStage = 2;
                vel.x = -10;
                GameScreen.getPlayer().takeDmg(atk);
                attackHit = false;
                if (setPRStartTime) {
                    pRStartTime = TimeUtils.nanoTime();
                    setPRStartTime=false;
                }
                doPlayerRecoil=true;
            } else if (loc.x <= initLocX) {
                vel.x = 0;
                doAttackL = false;
                GameScreen.getPlayer().setMonsAtk(false);
            }
        }
    }

    public void dispose(){
        for (int i=0; i<monsTexture.length; i++){
            monsTexture[i].getTexture().dispose();
        }
        monsFrame.getTexture().dispose();
        healthBar.dispose();
        healthSeg.dispose();
    }

    public int getCurHp(){
        return curHp;
    }

    public Vector2 getLoc(){
        return loc;
    }

    public Rectangle getHitBox(){
        return hitBox;
    }
}

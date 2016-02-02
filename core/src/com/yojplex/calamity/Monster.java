package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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
    private int def;
    private int spd;
    private Vector2 loc;
    private Vector2 vel;
    private float width;
    private float height;
    private Rectangle hitBox;
    private boolean inBattle;
    private long attackTime;

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

        atk = ThreadLocalRandom.current().nextInt(1, (lvl * 3 - 2) + 1);
        def = ThreadLocalRandom.current().nextInt(1, (lvl * 3 - 1 - atk) + 1);
        spd = lvl*3 - (def + atk);

        this.loc=loc;
        width=250*MyGdxGame.masterScale;
        height=250*MyGdxGame.masterScale;
        vel=new Vector2(0, 0);
        hitBox=new Rectangle(loc.x, loc.y, width, height);
    }

    public void draw(SpriteBatch batch){
        monsStateTime+= Gdx.graphics.getDeltaTime();
        monsFrame=monsAnimation.getKeyFrame(monsStateTime, true);
        batch.draw(monsFrame, loc.x, loc.y, width, height);
        loc.y+=vel.y;
        hitBox.set(loc.x, loc.y, width, height);

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
        }

        if (hitBox.overlaps(GameScreen.getPlayer().getHitBox())){
            GameScreen.getPlayer().setInBattle(true);
            inBattle=true;
        }

        if (loc.x>GameScreen.getPlayer().getLoc().x && Gdx.input.getX()<Gdx.graphics.getWidth()/2){
            GameScreen.getPlayer().setInBattle(false);
            if (inBattle){
                inBattle=false;
            }
        }
        if (loc.x<GameScreen.getPlayer().getLoc().x && Gdx.input.getX()>Gdx.graphics.getWidth()/2){
            GameScreen.getPlayer().setInBattle(false);
            if (inBattle){
                inBattle=false;
            }
        }

        if (inBattle){
            if (TimeUtils.timeSinceNanos(attackTime)>TimeUtils.millisToNanos(3000/spd)){
                attack();
                attackTime=TimeUtils.nanoTime();
            }
        }
    }

    public void attack(){
    }

    public void dispose(){

    }
}

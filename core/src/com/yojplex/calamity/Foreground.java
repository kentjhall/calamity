package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.screens.GameScreen;

import java.util.Random;

/**
 * Created by kenthall on 2/1/16.
 */
public class Foreground {
    private Texture[] caveTexture;
    private Texture curCaveTexture;
    private Random generator;
    private Vector2 loc;
    private float width;
    private float height;
    private float velY;
    private float initLocY;

    public Foreground(Vector2 loc){
        this.loc=loc;
        width=Gdx.graphics.getWidth()*0.4f;
        height=Gdx.graphics.getHeight()*0.26f;
        velY=0;
        initLocY=loc.y;

        caveTexture=new Texture[6];
        for (int i=0; i<caveTexture.length; i++){
            caveTexture[i]=new Texture("fg/fg_" + (i+1) + ".png");
        }

        generator=new Random();
        curCaveTexture=caveTexture[generator.nextInt(caveTexture.length)];
    }

    public void draw(SpriteBatch batch){
        batch.draw(curCaveTexture, loc.x, loc.y, width, height);
        batch.draw(curCaveTexture, loc.x+width, loc.y, width, height);
        batch.draw(curCaveTexture, loc.x+width*2, loc.y, width, height);
        loc.y+=velY;

        if (GameScreen.getShiftStrata()){
            if (GameScreen.getShiftDirection()) {
                velY=GameScreen.getShiftSpeed();
            }
            else{
                velY=-GameScreen.getShiftSpeed();
            }
        }
        else if (velY!=0){
            velY=0;
            if (GameScreen.getShiftDirection()) {
                loc.y = initLocY + Gdx.graphics.getHeight() * 0.25f;
                if (loc.y<=Gdx.graphics.getHeight()) {
                    initLocY = loc.y;
                }
                else{
                    initLocY=-Gdx.graphics.getHeight() * 0.25f;
                    loc.y=-Gdx.graphics.getHeight() * 0.25f;
                }
            }
            else {
                loc.y = initLocY - Gdx.graphics.getHeight() * 0.25f;
                if (loc.y>=-Gdx.graphics.getHeight() * 0.25f) {
                    initLocY = loc.y;
                }
                else{
                    initLocY=Gdx.graphics.getHeight();
                    loc.y=Gdx.graphics.getHeight();
                }
            }
        }
    }

    public void dispose(){
        for (int i=0; i<caveTexture.length; i++){
            caveTexture[i].dispose();
        }
    }

    public Vector2 getLoc(){
        return loc;
    }
}

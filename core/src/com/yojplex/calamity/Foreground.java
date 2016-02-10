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
    public enum Type{
        SKY, DIRT
    }
    private Type type;
    private Texture[] fgTexture;
    private Texture curFgTexture;
    private Texture curFgTexture2;
    private Texture curFgTexture3;
    private Random generator;
    private Vector2 loc;
    private float width;
    private float height;
    private float velY;
    private float initLocY;

    public Foreground(Vector2 loc, Type type){
        this.loc=loc;
        width=Gdx.graphics.getWidth()*0.4f;
        height=Gdx.graphics.getHeight()*0.25f;
        velY=0;
        initLocY=loc.y;

        fgTexture =new Texture[6];

        generator=new Random();
        changeType(type);
    }

    public void draw(SpriteBatch batch){
        batch.draw(curFgTexture, loc.x, loc.y, width, height);
        batch.draw(curFgTexture2, loc.x+width, loc.y, width, height);
        batch.draw(curFgTexture3, loc.x+width*2, loc.y, width, height);
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

    public void changeType(Type type){
        this.type=type;
        switch(type){
            case SKY:
                for (int i=0; i<fgTexture.length; i++){
                    fgTexture[i]=new Texture("fg/grass/fg_" + (i+1) + ".png");
                }
                break;
            case DIRT:
                for (int i = 0; i < fgTexture.length; i++) {
                    fgTexture[i] = new Texture("fg/dirtGrass/fg_" + (i + 1) + ".png");
                }
                break;
        }
        curFgTexture = fgTexture[generator.nextInt(fgTexture.length)];
        curFgTexture2 = fgTexture[generator.nextInt(fgTexture.length)];
        curFgTexture3 = fgTexture[generator.nextInt(fgTexture.length)];
    }

    public void dispose(){
        for (int i=0; i< fgTexture.length; i++){
            fgTexture[i].dispose();
        }
    }

    public Vector2 getLoc(){
        return loc;
    }

    public Type getType(){
        return type;
    }

    public float getInitLocY(){
        return initLocY;
    }
}

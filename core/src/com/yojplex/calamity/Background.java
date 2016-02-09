package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.screens.GameScreen;

/**
 * Created by kenthall on 2/1/16.
 */
public class Background {
    private Texture caveTexture;
    private Texture caveTexture2;
    private Vector2 loc;
    private Vector2 loc2;
    private float velY;
    private int width;
    private int height;

    public Background(){
        caveTexture=new Texture("bgDirt.png");
        caveTexture2=new Texture("bgDirt.png");
        caveTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        caveTexture2.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        loc=new Vector2(0, -Gdx.graphics.getHeight()*0.25f);
        loc2=new Vector2(0, -Gdx.graphics.getHeight()*1.25f);
        velY=0;
        width=Gdx.graphics.getWidth();
        height=Gdx.graphics.getHeight();
    }

    public void draw(SpriteBatch batch){
        batch.draw(caveTexture, loc.x, loc.y, 1, 1, width, height);
        batch.draw(caveTexture2, loc2.x, loc2.y, 1, 1, width, height);
        loc.y+=velY;
        loc2.y+=velY;

        if (loc.y>Gdx.graphics.getHeight()){
            loc.y=-Gdx.graphics.getHeight();
        }

        if (loc.y<-Gdx.graphics.getHeight()){
            loc.y=Gdx.graphics.getHeight();
        }

        if (loc2.y>Gdx.graphics.getHeight()){
            loc2.y=-Gdx.graphics.getHeight();
        }

        if (loc2.y<-Gdx.graphics.getHeight()){
            loc2.y=Gdx.graphics.getHeight();
        }

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
        }
    }

    public void dispose(){
        caveTexture.dispose();
    }
}

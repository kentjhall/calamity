package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.screens.GameScreen;

/**
 * Created by kenthall on 2/15/16.
 */
public class Button {
    private int butPressStage;
    private Texture textureU;
    private Texture textureD;
    private float width;
    private float height;
    private Rectangle hitBox;

    public Button(Texture textureU, Texture textureD, int width, int height){
        this.textureU=textureU;
        this.textureD=textureD;
        this.width=width;
        this.height=height;
        hitBox=new Rectangle();
        butPressStage=0;
    }
    public void draw(SpriteBatch batch, Vector2 loc, Vector2 hitLoc){
        if (GameScreen.getPlayer().getUpPoints()>0){
            if (butPressStage==1){
                batch.draw(textureD, loc.x, loc.y, width, height);
            }
            else{
                batch.draw(textureU, loc.x, loc.y, width, height);
            }
        }
        else {
            batch.draw(textureD, loc.x, loc.y, width, height);
        }

        hitBox.set(hitLoc.x, hitLoc.y, width, height);
        if (hitBox.contains(Gdx.input.getX(), Gdx.input.getY())){
            if (Gdx.input.isTouched()) {
                butPressStage = 1;
                GameScreen.getPlayer().setButPressed(true);
            }
            else if (butPressStage==1){
                butPressStage = 2;
                GameScreen.getPlayer().setButPressed(false);
            }
        }
        else{
            butPressStage=0;
        }
    }
    public void dispose(){
        textureU.dispose();
        textureD.dispose();
    }
    public int getButPressStage(){
        return butPressStage;
    }

    public void setButPressStage(int butPressStage){
        this.butPressStage=butPressStage;
    }
}

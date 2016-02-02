package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.screens.GameScreen;

/**
 * Created by kenthall on 1/31/16.
 */
public class StrataBorder {
    private Texture lineB;
    private float width;
    private float height;
    private Vector2 loc;
    private Vector2 vel;
    private int initLocY;

    public StrataBorder(Vector2 loc){
        lineB=new Texture("lineB.png");

        width=Gdx.graphics.getWidth()*2;
        height=10*MyGdxGame.masterScale;

        this.loc=loc;
        this.vel=new Vector2(0, 0);
        initLocY=(int)loc.y;
    }

    public void draw(SpriteBatch batch){
        batch.draw(lineB, loc.x, loc.y, width, height);
        loc.x+=vel.x;
        loc.y+=vel.y;

        if (loc.y>Gdx.graphics.getHeight()){
            loc.y=-height;
        }

        if (loc.y<-height){
            loc.y=Gdx.graphics.getHeight();
        }

        if (GameScreen.getShiftStrata()){
            if (GameScreen.getShiftDirection()) {
                vel.y=GameScreen.getShiftSpeed();
            }
            else{
                vel.y=-GameScreen.getShiftSpeed();
            }
        }
        else if (vel.y!=0){
            vel.y=0;
            loc.y=initLocY;
        }
    }

    public void dispose(){
        lineB.dispose();
    }
}

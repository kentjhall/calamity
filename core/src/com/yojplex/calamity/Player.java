package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.screens.GameScreen;

/**
 * Created by kenthall on 1/31/16.
 */
public class Player {
    private Texture squareB;
    private float width;
    private float height;
    private Vector2 loc;
    private Vector2 vel;
    private int strataNum;
    private boolean strataChange;

    public Player(Vector2 loc){
        squareB=new Texture("squareB.jpg");

        width=175*MyGdxGame.masterScale;
        height=175*MyGdxGame.masterScale;

        this.loc=loc;
        vel=new Vector2(0, 0);
    }

    public void draw(SpriteBatch batch){
        batch.draw(squareB, loc.x, loc.y, width, height);
        loc.x+=vel.x;
        loc.y+=vel.y;

        if (Gdx.input.getX()<Gdx.graphics.getWidth()/2 && Gdx.input.isTouched()){
            if (strataNum>0) {
                vel.x = -10;
            }
            else if (loc.x>0){
                vel.x = -10;
            }
            else{
                vel.x=0;
            }
        }
        else if (Gdx.input.getX()>Gdx.graphics.getWidth()/2 && Gdx.input.isTouched()){
            vel.x=10;
        }
        else{
            vel.x=0;
        }

        if (loc.x>Gdx.graphics.getWidth()){
            loc.x=-width;
            loc.y=Gdx.graphics.getHeight()*0.5f;
        }

        if (loc.x<-width){
            loc.x = Gdx.graphics.getWidth();
            loc.y = Gdx.graphics.getHeight();
        }

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
            vel.y=0;
            loc.y=Gdx.graphics.getHeight()*0.75f;
        }

        if (strataChange && vel.y==0){
            if (GameScreen.getShiftDirection()) {
                strataNum++;
            }
            else{
                strataNum--;
            }
            strataChange=false;
        }
    }

    public void dispose(){
        squareB.dispose();
    }

    public Vector2 getLoc(){
        return loc;
    }
}

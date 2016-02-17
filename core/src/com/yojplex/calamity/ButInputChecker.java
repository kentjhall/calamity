package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.yojplex.calamity.screens.GameScreen;

import java.util.ArrayList;

/**
 * Created by kenthall on 2/16/16.
 */
public class ButInputChecker {
    private ArrayList<Rectangle> hitBoxes;
    public ButInputChecker(){
        hitBoxes=new ArrayList<Rectangle>();
    }
    public void draw(){
        for (Rectangle hitBox: hitBoxes){
            if (hitBox.contains(Gdx.input.getX(), Gdx.input.getY())){
                if (Gdx.input.isTouched()) {
                    GameScreen.getPlayer().setButPressed(true);
                }
                else{
                    GameScreen.getPlayer().setButPressed(false);
                }
            }
        }
    }

    public ArrayList<Rectangle> getHitBoxes(){
        return hitBoxes;
    }
}

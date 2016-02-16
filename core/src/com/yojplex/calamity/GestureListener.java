package com.yojplex.calamity;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.yojplex.calamity.screens.GameScreen;

/**
 * Created by kenthall on 2/1/16.
 */
public class GestureListener implements GestureDetector.GestureListener{
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (GameScreen.getDropMenu().getDrawerUp() && velocityY>4000){
            GameScreen.getDropMenu().setVelY(-velocityY*0.02f*MyGdxGame.masterScale);
        }
        else if (!GameScreen.getDropMenu().getDrawerUp() && velocityY<-4000){
            GameScreen.getDropMenu().setVelY(-velocityY*0.02f*MyGdxGame.masterScale);
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}

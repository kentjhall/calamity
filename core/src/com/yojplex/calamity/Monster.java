package com.yojplex.calamity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by kenthall on 2/1/16.
 */
public class Monster {
    public enum Type {
        MUSHY
    }
    private Texture monsTexture;
    private int atk;
    private int def;
    private int eva;
    private Vector2 loc;
    private float width;
    private float height;

    public Monster(Type type, int lvl, Vector2 loc){
        switch (type){
            case MUSHY:
                monsTexture=new Texture("squareB.jpg");
                break;
        }

        atk = ThreadLocalRandom.current().nextInt(1, (lvl*3 - 2) + 1);
        def = ThreadLocalRandom.current().nextInt(1, (lvl*3 - 1 - atk) + 1);
        eva = lvl*3 - (def + atk);

        this.loc=loc;
        width=250*MyGdxGame.masterScale;
        height=250*MyGdxGame.masterScale;
    }

    public void draw(SpriteBatch batch){
        batch.draw(monsTexture, loc.x, loc.y, width, height);
    }

    public void dispose(){

    }
}

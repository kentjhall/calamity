package com.yojplex.calamity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by kenthall on 2/1/16.
 */
public class Background {
    private Texture caveTexture;

    public Background(){
        caveTexture=new Texture("bg.png");
        caveTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    public void draw(SpriteBatch batch){
        batch.draw(caveTexture, 0, 0, 1, 1, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void dispose(){

    }
}

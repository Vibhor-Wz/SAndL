package com.mygdx.game.Helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ImageResize {

    public static TextureRegionDrawable getTextureInRespectOfWidth
            (TextureRegion textureRegion, float reqWidth) {

        textureRegion.getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        TextureRegionDrawable drawable = new TextureRegionDrawable(textureRegion);
        drawable.setMinSize(reqWidth,
                reqWidth * textureRegion.getRegionHeight() / textureRegion.getRegionWidth());
        return drawable;
    }
    public static TextureRegionDrawable getTextureInRespectOfHeight(TextureRegion textureRegion
            , float reqHeight) {

        textureRegion.getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        TextureRegionDrawable drawable = new TextureRegionDrawable(textureRegion);
        drawable.setMinSize(reqHeight * textureRegion.getRegionWidth()
                /textureRegion.getRegionHeight(), reqHeight);
        return drawable;
    }
}

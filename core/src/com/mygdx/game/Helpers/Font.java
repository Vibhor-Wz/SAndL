package com.mygdx.game.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.DistanceFieldFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Font extends DistanceFieldFont {
    private final ShaderProgram distanceFieldShader;
    private final float smoothing;

    public Font(FileHandle fontFile, TextureRegion imageRegion, float size) {
        super(fontFile, imageRegion, false);
        distanceFieldShader = new ShaderProgram
                (Gdx.files.internal("shaders/distance.vert")
                        , Gdx.files.internal("shaders/distance.frag"));

        if (!distanceFieldShader.isCompiled()) {
            throw new IllegalArgumentException("Error compiling distance field shader: " + distanceFieldShader.getLog());
        }
        getData().setScale(size / 16f);
        smoothing = getSmoothing(size);
    }

    private float getSmoothing(float size) {
        float scale = size / 16f;
        return 0.25f / (4 * scale);
    }

    @Override
    public BitmapFontCache newFontCache() {

        return new MyFontCache(this);
    }

    class MyFontCache extends BitmapFontCache {

        private ShaderProgram defaultShader;

        public MyFontCache(BitmapFont font) {
            super(font);
        }

        @Override
        public void draw(Batch spriteBatch) {
            setShader(spriteBatch);
            super.draw(spriteBatch);
            removeShader(spriteBatch);
        }

        @Override
        public void draw(Batch spriteBatch, int start, int end) {
            setShader(spriteBatch);
            super.draw(spriteBatch, start, end);
            removeShader(spriteBatch);
        }

        @Override
        public void draw(Batch spriteBatch, float alphaModulation) {
            setShader(spriteBatch);
            super.draw(spriteBatch, alphaModulation);
            removeShader(spriteBatch);
        }

        private void setShader(Batch batch) {
            defaultShader = batch.getShader();
            batch.flush();
            batch.setShader(distanceFieldShader);
            distanceFieldShader.setUniformf("u_smoothing", smoothing);

//            distanceFieldShader.setUniformf("outlineDistance", 0.2f);
//            distanceFieldShader.setUniformf("outlineColor", Color.RED);
        }

        private void removeShader(Batch batch) {
            batch.flush();
            batch.setShader(defaultShader);

        }
    }
}

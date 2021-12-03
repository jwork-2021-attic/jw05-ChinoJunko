package com.madmath.core.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class CustomAnimation extends Animation {
    private TextureRegionDrawable[] keyFrames;
    private Array<? extends TextureRegion > textureRegions;

    public CustomAnimation(float frameDuration, Array<? extends TextureRegion > originKeyFrames){
        super(frameDuration,originKeyFrames);
        textureRegions = originKeyFrames;
        setPlayMode(PlayMode.LOOP);
        this.keyFrames = new TextureRegionDrawable[originKeyFrames.size];
        for (int i = 0; i < originKeyFrames.size; i++) {
            this.keyFrames[i] = new TextureRegionDrawable(originKeyFrames.get(i));
        }
    }

    public TextureRegionDrawable getKeyFrameDrawable(float stateTime) {
        return keyFrames[super.getKeyFrameIndex(stateTime)];
    }

    public TextureRegion getKeyFrame(float stateTime) {
        return super.getKeyFrame(stateTime);
    }

    public CustomAnimation getReverse(){
        Array<TextureRegion> tempTextureRegions = new Array<>();
        textureRegions.forEach(textureRegion -> {
            tempTextureRegions.add(new TextureRegion(textureRegion.getTexture(),textureRegion.getRegionX()+ textureRegion.getRegionWidth(), textureRegion.getRegionY(), -textureRegion.getRegionWidth(), textureRegion.getRegionHeight()));
        });
        return new CustomAnimation(getFrameDuration(),tempTextureRegions);
    }

}

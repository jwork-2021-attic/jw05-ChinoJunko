package com.madmath.core.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class CustomAnimation extends Animation {
    private TextureRegionDrawable[] keyFrames;

    public CustomAnimation(float frameDuration, Array<? extends TextureRegion > originKeyFrames){
        super(frameDuration,originKeyFrames);
        this.keyFrames = new TextureRegionDrawable[originKeyFrames.size];
        for (int i = 0; i < originKeyFrames.size; i++) {
            this.keyFrames[i] = new TextureRegionDrawable((TextureRegion) originKeyFrames.get(i));
        }
    }

    public TextureRegionDrawable getKeyFrameDrawable(float stateTime) {
        return keyFrames[super.getKeyFrameIndex(stateTime)];
    }
}

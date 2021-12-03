package com.madmath.core.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimationManager {
    CustomAnimation[] animations;
    PlayMode playMode;
    boolean reverse = false;

    public AnimationManager(CustomAnimation...customAnimations){
        animations = new CustomAnimation[customAnimations.length*2];
        for (int i = 0; i < customAnimations.length; i++) {
            animations[i] = customAnimations[i];
            animations[i+customAnimations.length] = customAnimations[i].getReverse();
        }
        playMode = PlayMode.Stand;
    }

    public TextureRegion getKeyFrame(float stateTime){
        return animations[reverse? playMode.ordinal() + PlayMode.values().length: playMode.ordinal()].getKeyFrame(stateTime);
    }

    public TextureRegionDrawable getKeyFrameDrawable(float stateTime){
        return animations[reverse? playMode.ordinal() + PlayMode.values().length:playMode.ordinal()].getKeyFrameDrawable(stateTime);
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public enum PlayMode{
        Stand,
        Moving,
    }
}

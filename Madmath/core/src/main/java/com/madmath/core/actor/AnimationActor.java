package com.madmath.core.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.madmath.core.animation.CustomAnimation;

public class AnimationActor extends Image {
    private CustomAnimation animation;
    private float stateTime;
    public AnimationActor(CustomAnimation animation){
        super(animation.getKeyFrame(0));
        stateTime = 0;
        this.animation = animation;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime+=delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.setDrawable(animation.getKeyFrameDrawable(stateTime));
        super.draw(batch,parentAlpha);
    }

    public void setPlayMode(CustomAnimation.PlayMode playMode){
        animation.setPlayMode(playMode);
    }
}

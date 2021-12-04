package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:07
 */
public class OddSword extends Equipment{

    static public String alias = "weapon_duel_sword";

    public OddSword(Integer id, TextureRegion region) {
        super(id, region);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 160;
        swingSpeed = 720;
        knockbackFactor = 1;
    }
}

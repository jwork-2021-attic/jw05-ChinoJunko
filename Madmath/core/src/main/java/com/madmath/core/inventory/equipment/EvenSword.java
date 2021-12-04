package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:07
 */
public class EvenSword extends Equipment{

    static public String alias = "weapon_knight_sword";

    public EvenSword(Integer id, TextureRegion region) {
        super(id, region);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 220;
        swingSpeed = 500;
        knockbackFactor = 6;
        damage = 100;
    }
}

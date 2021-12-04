package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:07
 */
public class PrimeHammer extends Equipment{

    static public String alias = "weapon_big_hammer";

    public PrimeHammer(Integer id, TextureRegion region) {
        super(id, region);
    }

    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 1200;
        swingSpeed = 900;
        knockbackFactor = 12;
        damage = 120;
    }
}

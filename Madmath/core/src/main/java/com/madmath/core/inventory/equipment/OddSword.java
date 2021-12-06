package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madmath.core.expression.Expression;

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
    public boolean canAttack(Expression expression) {
        return expression.getValue()%2==1;
    }

    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 190;
        swingSpeed = 600;
        knockbackFactor = 4;
    }
}

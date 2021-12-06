package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madmath.core.expression.Expression;

import java.util.HashSet;

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

    private HashSet<Integer> Prime;

    @Override
    public boolean canAttack(Expression expression) {
        return Prime.contains(expression.getValue());
    }

    @Override
    public void initSelf() {
        super.initSelf();
        swingRange = 1200;
        swingSpeed = 900;
        knockbackFactor = 12;
        damage = 120;
        initPrime();
    }

    private void initPrime(){
        Prime = new HashSet<>();
        for (int i = 1; i < 10000; i+=2) {
            boolean isPrime = true;
            for (int j = 3; j < i; j+=2) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if(isPrime) Prime.add(i);
        }
    }
}

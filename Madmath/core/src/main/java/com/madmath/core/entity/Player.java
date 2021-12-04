/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.animation.CustomAnimation;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;

public class Player extends Entity{

    Array<Equipment> weapon;
    Equipment activeWeapon=null;

    public float weaponAngle;

    static public String alias = "knight_f";

    static public AnimationManager initPlayerAnim(ResourceManager manager){
        return new AnimationManager(new CustomAnimation(0.34f,new Array<>(manager.knight_f_idle_anim16x28[0])),new CustomAnimation(0.17f,new Array<>(manager.knight_f_run_anim16x28[0])));
    }

    public Player(Integer id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position) {
        super(id, animationManager, gameScreen, position);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(activeWeapon!=null&& !activeWeapon.isSwinging()){
            activeWeapon.setRotation(weaponAngle);
        }
    }

    public void addWeapon(Equipment equipment){
        if(weapon.size>=3) return;
        weapon.add(equipment);
        if(activeWeapon != null)    activeWeapon.addAction(Actions.hide());
        activeWeapon = equipment;
        equipment.equippedBy(this);
        gameScreen.livingItem.removeValue(equipment,true);
    }

    public void swingWeapon(){
        if(activeWeapon!=null){
            activeWeapon.use();
        }
    }

    @Override
    public boolean move(float v) {
        boolean temp = super.move(v);
        Vector2 cPoisition = new Vector2();
        gameScreen.livingItem.forEach(item -> {
            if(item.canPickUp(box.getCenter(cPoisition))&& item instanceof Equipment){
                addWeapon((Equipment) item);
            }
        });
        return temp;
    }

    @Override
    public void initSelf() {
        super.initSelf();
        weapon = new Array<>(3);
        speed = 64f;
        maxHp = 10;
        hp = 10;
        box = new Rectangle(0,0,12,7);
        boxOffset = new Vector2(2,0);
        //lostSpeed = 1.8f;
    }

}


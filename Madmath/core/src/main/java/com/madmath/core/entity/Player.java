/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.animation.CustomAnimation;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.map.TrapTile;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.util.Utils;
import com.madmath.core.util.myPair;

public class Player extends Entity{

    public Array<Equipment> weapon;
    public Equipment activeWeapon=null;

    private boolean attackable = true;
    private boolean movable = true;

    private Vector2 subjectiveDirection = new Vector2();

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
        sufferFromTrap();
        if(movable) currentDirection.set(subjectiveDirection);
    }

    @Override
    public int getHurt(int damage, Vector2 sufferFrom, float knockbackFactor) {
        if(!attackable) return 0;
        attackable = false;
        knockbackFactor *= (1-toughness);
        sufferFrom.sub(getPosition());
        sufferFrom.x = sufferFrom.x>0?-knockbackFactor:knockbackFactor;
        sufferFrom.y = sufferFrom.y>0?-knockbackFactor:knockbackFactor;
        currentDirection.set(sufferFrom);
        movable = false;
        sufferFrom.scl(-0.08f,-0.08f);
        addAction(Actions.sequence(Actions.color(hurtColor.cpy()),Actions.addAction(Actions.color(color.cpy(),0.27f)),
                Actions.addAction(Actions.sequence(Actions.run(()->{
                    attackable = false;
                }),Actions.repeat(3,Actions.sequence(Actions.alpha(0.5f,0.3f),Actions.alpha(1f,0.3f))),
                Actions.run(()->{
                    attackable = true;
                }))),
                Actions.repeat(5,Actions.sequence(Actions.run(()->{
                    addAcceleration(sufferFrom);
                }),Actions.delay(0.05f))),
                Actions.run(()->{
                    setAcceleration(new Vector2(0,0));
                }),Actions.delay(0.1f),
                Actions.run(()->{
                    movable = true;
                })));
        hp -= damage;
        try {
            return damage;
        }finally {
            if(hp<=0)Die();
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

    public int getHurt(Monster monster) {
        return getHurt(monster.getDamage(),monster.getPosition().cpy(),monster.getKnockback());
    }

    public void switchWeapon(int offset){
        if(weapon.size<2 || activeWeapon.isSwinging())return;
        activeWeapon.addAction(Actions.hide());
        activeWeapon = weapon.get(((weapon.indexOf(activeWeapon,true)+offset)+weapon.size)%weapon.size);
        activeWeapon.addAction(Actions.show());
    }

    public void sufferFromTrap(){
        Array<myPair> tiledMapTileVector2Pair = getTileOnFoot(getPosition());
        tiledMapTileVector2Pair.forEach(pair ->{
            if(pair.A instanceof TrapTile && ((TrapTile) pair.A).isActive()){
                getHurt((TrapTile) pair.A,pair.B);
                return;
            }
        });
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

    public void addSubjectiveDirection(Vector2 Direction){
        subjectiveDirection.x = Math.min(Math.max(subjectiveDirection.x + Direction.x,-1),1);
        subjectiveDirection.y = Math.min(Math.max(subjectiveDirection.y + Direction.y,-1f),1f);
    }

    @Override
    public boolean isCanMove(Vector2 next){
        next.add(boxOffset);
        for (float i = next.x; i <= next.x+box.getWidth(); i += box.getWidth()) {
            for (float j = next.y; j <= next.y+box.getHeight(); j += box.getHeight()) {
                if(i< gameScreen.getMap().startPosition.x||i>= gameScreen.getMap().startPosition.x+ gameScreen.getMap().playAreaSize.x||j< gameScreen.getMap().startPosition.y||j>= gameScreen.getMap().startPosition.y+ gameScreen.getMap().playAreaSize.y)  return false;
                TiledMapTileLayer layer =(TiledMapTileLayer) gameScreen.getMap().getTiledMap().getLayers().get(0);
                TiledMapTile tile = layer.getCell((int)i/16,(int)j/16).getTile();
                if(!Utils.accessibleG.contains(tile.getId()))   return false;
            }
        }
        Rectangle nextBox = new Rectangle(box).setPosition(next);
        for (int i = 0; i < gameScreen.livingEntity.size; i++) {
            if(gameScreen.livingEntity.get(i)!=null&& gameScreen.livingEntity.get(i) != this && gameScreen.livingEntity.get(i).box.overlaps(nextBox))  {
                //System.out.println("false:"+entity);
                return false;
            }
        }
        return true;
    }

    @Override
    public void initSelf() {
        super.initSelf();
        weapon = new Array<>(3);
        speed = 64f;
        maxHp = 30;
        hp = 30;
        box = new Rectangle(0,0,12,7);
        boxOffset = new Vector2(2,0);
        //lostSpeed = 1.8f;
    }

}


/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.actor.AnimationActor;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.entity.Entity;
import com.madmath.core.entity.Player;
import com.madmath.core.inventory.Item;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.util.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class Equipment extends AnimationActor implements Item {

    public int id;

    protected float swingRange;
    protected float swingSpeed;
    protected float currencySwingRange = 0;

    public Player owner = null;

    public int damage;
    public int knockbackFactor;

    static public String alias;

    public static Array<Equipment> equipmentSort;

    public Array<Entity> attackedTargets = new Array<>();;

    Rectangle pickBox;
    Circle attackCircle;

    TextureRegion textureRegionForClone;


    public Equipment(Integer id, TextureRegion region){
        super(new AnimationManager(region,1f));
        initSelf();
        this.id = id;
        textureRegionForClone = region;
        setRotation(-45);
        addAction(Actions.forever(Actions.sequence(Actions.moveBy(0,8,1.8f),Actions.moveBy(0,-8,1.8f))));
    }

    public void initSelf(){
        pickBox = new Rectangle(0,0,100,100);
        attackCircle = new Circle(0,0,getHeight());
        swingRange = 220;
        swingSpeed = 500;
    }

    @Override
    public void setPosition(float x, float y) {
        if(owner==null){
            pickBox.setPosition(x, y);
        }
        attackCircle.setPosition(anim_dirt?x-owner.box.getWidth():x, y);
        super.setPosition(anim_dirt?x-owner.box.getWidth():x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(owner!=null){
            setPosition(owner.getCenterX(),owner.getCenterY());
        }
        if(currencySwingRange>0){
            float swing = swingSpeed * delta;
            currencySwingRange -= swing;
            rotateBy(anim_dirt?swing:-swing);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public boolean canPickUp(Vector2 position) {
        return owner==null&&pickBox.contains(position);
    }

    @Override
    public boolean use() {
        if(isSwinging())    return false;
        attackedTargets.clear();
        currencySwingRange = swingRange;
        float perAttackCheckInterval = 0.2f;
        int totalCheckNum = (int) (swingRange/swingSpeed/perAttackCheckInterval);
        addAction(Actions.repeat(totalCheckNum,Actions.sequence(Actions.delay(perAttackCheckInterval),Actions.run(()->{
            Vector2[] vector2s = new Vector2[4];
            for (int i = 0; i < 4; i++) {
                vector2s[i] = new Vector2();
            }
            owner.gameScreen.livingEntity.forEach(entity -> {
                if(entity!=owner&&!attackedTargets.contains(entity,true)){
                    attackCircle.radius+=50;
                    boolean temp = attackCircle.contains(entity.box.getCenter(vector2s[0]));
                    attackCircle.radius-=50;
                    if(!temp) return;
                    if(Math.abs(getRotation()%360f-vector2s[0].sub(owner.box.getCenter(vector2s[1])).angle())>swingRange) return;
                    for (int i = 0; i < 4; i++) {
                        entity.box.getPosition(vector2s[i]);
                    }
                    vector2s[1].add(entity.box.getWidth(),0);
                    vector2s[2].add(0,entity.box.getHeight());
                    vector2s[3].add(entity.box.getWidth(),entity.box.getHeight());
                    for (int i = 0; i < 4; i++) {
                        if(attackCircle.contains(vector2s[i])){
                            attackedTargets.add(entity);
                            entity.getHurt(this);
                            System.out.println("Attack!");
                            return;
                        }
                    }
                }
            });
        }))));
        return true;
    }

    public TextureRegion getTextureRegionForClone(){
        return textureRegionForClone;
    }

    public int getKnockbackFactor() {
        return knockbackFactor;
    }

    public void equippedBy(Player player){
        clearActions();
        owner = player;
    }

    public boolean isSwinging(){
        return currencySwingRange > 0;
    }

    @Override
    public void setRotation(float degrees) {
        if(!isSwinging()){
            int kAngle = Math.round(degrees) % 360;
            anim_dirt = kAngle > 90 && kAngle <= 270;
            setOrigin(anim_dirt?getWidth():0,0);
            super.setRotation(anim_dirt? degrees-180:degrees);
        }
    }

    static public void loadEquipment(){
        equipmentSort = new Array<>();
        for (String name: Utils.AllDefaultEquipmentSort) {
            try {
                Class<?> c = Class.forName("com.madmath.core.inventory.equipment."+name);
                Constructor<?> con = c.getConstructor(Integer.class, TextureRegion.class);
                equipmentSort.add((Equipment) con.newInstance(300+Equipment.equipmentSort.size,ResourceManager.defaultManager.LoadEquipmentAssetsByName((String) c.getField("alias").get(null))));
            } catch (ClassNotFoundException e) {
                System.out.println("Not Found A Equipment Named '" +name+'\'');
            } catch (NoSuchFieldException e){
                System.out.println("Not Such Field :'" +e.getMessage()+'\'');
            } catch (IllegalAccessException e){
                System.out.println("IllegalAccessField :'" +e.getMessage()+'\'');
            } catch (NoSuchMethodException e){
                System.out.println("Not Such Method :'" +e.getMessage()+'\'');
                e.printStackTrace();
            }
            catch (InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

}
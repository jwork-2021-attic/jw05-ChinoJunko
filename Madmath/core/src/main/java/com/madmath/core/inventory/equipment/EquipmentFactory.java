package com.madmath.core.inventory.equipment;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.entity.Monster;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 4/12/2021 下午4:24
 */
public class EquipmentFactory {
    ResourceManager manager;

    GameScreen gameScreen;

    static private int equipmentNextId = 4000;

    public EquipmentFactory(ResourceManager manager, GameScreen gameScreen){
        this.manager = manager;
        this.gameScreen = gameScreen;
    }

    @Nullable
    public Equipment generateEquipmentByName(String name) {
        Iterable<Equipment> equipmentIter = Equipment.equipmentSort.select(equipment -> {
            try {
                return equipment.getClass().getField("alias").get(equipment).equals(name)
                        || equipment.getClass().getName().substring(equipment.getClass().getName().lastIndexOf(".")+1).equals(name);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return false;
        });
        if(equipmentIter.iterator().hasNext()){
            Equipment equipment = equipmentIter.iterator().next();
            try {
                return equipment.getClass().getConstructor(Integer.class,TextureRegion.class).newInstance(equipmentNextId++,equipment.getTextureRegionForClone());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return Equipment.equipmentSort.get(0);
    }
}

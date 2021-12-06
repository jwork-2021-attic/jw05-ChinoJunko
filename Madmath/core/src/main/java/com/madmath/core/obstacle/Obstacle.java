package com.madmath.core.obstacle;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.actor.AnimationActor;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.util.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author: Junko
 * @Email: imaizumikagerouzi@foxmail.com
 * @Date: 6/12/2021 下午2:23
 */
public class Obstacle extends AnimationActor {

    public static Array<Obstacle> obstacleSort;

    public Obstacle(AnimationManager animationManager) {
        super(animationManager.clone());
        initSelf();
    }

    public void initSelf(){

    }

    public Obstacle copy(){
        return GameScreen.getCurrencyGameScreen().getObstacleFactory().generateObstacleByName(getClass().getName());
    }

    static public void loadObstacle(){
        obstacleSort = new Array<>();
        for (String name: Utils.AllDefaultObstacleSort) {
            try {
                Class<?> c = Class.forName("com.madmath.core.inventory.obstacle."+name);
                Constructor<?> con = c.getConstructor(AnimationManager.class);
                obstacleSort.add((Obstacle) con.newInstance(new AnimationManager(ResourceManager.defaultManager.LoadObstacleAssetsByName((String) c.getField("alias").get(null),(int)c.getField("oWidth").get(null),(int)c.getField("oHeight").get(null)), new float[]{0.25f})));
            } catch (ClassNotFoundException e) {
                System.out.println("Not Found A Obstacle Named '" +name+'\'');
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

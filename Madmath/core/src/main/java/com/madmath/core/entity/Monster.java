/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.animation.CustomAnimation;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;
import com.madmath.core.util.Utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;

public abstract class Monster extends Entity{

    static public String alias;

    public static Array<Monster> monsterSort;

    public int level;



    static public int TextureWidth;
    static public int TextureHeight;
    static public float[] FrameIntervals = {
            0.34f,          //Idle
            0.17f,           //Run
    };

    protected Monster(AnimationManager animationManager){
        super(animationManager);
    }

    protected Monster(Integer id, AnimationManager animationManager){
        super(id, animationManager);
    }

    public Monster(Integer id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position) {
        super(id, animationManager, gameScreen, position);
    }

    public Monster(Integer id, AnimationManager animationManager, GameScreen gameScreen) {
        super(id, animationManager, gameScreen);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void monsterAct(float delta){
        Player player = gameScreen.player;
        Vector2 direction = new Vector2(player.getX()-getX(),player.getY()-getY());
        direction.x = Math.abs(direction.x)<player.getWidth()/2?0:direction.x>0?inertia:-inertia;
        direction.y = Math.abs(direction.y)<player.getHeight()/2?0:direction.y>0?inertia:-inertia;
        addAcceleration(direction);
        move(delta);
    }

    @Override
    public void Die() {
        super.Die();
        clear();
        gameScreen.livingEntity.removeValue(this,true);
        gameScreen.monsterManager.removeMonster(this);
        setAcceleration(new Vector2(0,0));
        addAction(Actions.sequence(Actions.color(Color.RED.cpy()),Actions.addAction(Actions.color(Color.WHITE.cpy(),0.5f)),Actions.fadeOut(1f),Actions.run(()->{
            gameScreen.getStage().getActors().removeValue(this,true);
        })));
    }

    static public void loadMonsters(){
        searchModMonster();
        monsterSort = new Array<>();
        for (String name: Utils.AllDefaultMonsterSort) {
            try {
                Class<?> c = Class.forName("com.madmath.core.entity."+name);
                Constructor<?> con = c.getConstructor(Integer.class,AnimationManager.class);
                monsterSort.add((Monster) con.newInstance(500+ResourceManager.defaultManager.MonsterLoad.size,
                        new AnimationManager(
                        new CustomAnimation(((float[]) c.getField("FrameIntervals").get(null))[0],
                                new Array<>(ResourceManager.defaultManager.LoadMonsterAssetsByName(c.getField("alias").get(null) +"_idle_anim",
                                        (int)c.getField("TextureWidth").get(null),
                                        (int)c.getField("TextureHeight").get(null))[0])),
                        new CustomAnimation(((float[]) c.getField("FrameIntervals").get(null))[1],
                                new Array<>(ResourceManager.defaultManager.LoadMonsterAssetsByName(c.getField("alias").get(null) +"_run_anim",
                                        (int)c.getField("TextureWidth").get(null),
                                        (int)c.getField("TextureHeight").get(null))[0])))));
            } catch (ClassNotFoundException e) {
                System.out.println("Not Found A Monster Named '" +name+'\'');
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
        for (int i = 0; i < Utils.ModLoadMonsterPath.size(); i++) {
            try {
                Class<?> c = Class.forName(Utils.ModLoadMonsterPath.get(i));
                Constructor<?> con = c.getConstructor(Integer.class,AnimationManager.class);
                monsterSort.add((Monster) con.newInstance(500+ResourceManager.defaultManager.MonsterLoad.size,
                        new AnimationManager(ResourceManager.defaultManager.LoadMonsterAssetsByPath(Utils.ModLoadMonsterTexture.get(i),
                                                (int)c.getField("TextureWidth").get(null),
                                                (int)c.getField("TextureHeight").get(null)),
                                (float[]) c.getField("FrameIntervals").get(null))));
                //monsterSort.add();
            } catch (ClassNotFoundException e) {
                System.out.println("Not Found A Monster Path '" +Utils.ModLoadMonsterPath.get(i)+'\'');
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
        monsterSort.sort((monster1,monster2)-> monster2.level - monster1.level);
    }

    public static void searchModMonster(){
        LinkedList<File> list = new LinkedList<>();
        list.add(new File("Madmath/core/src/main/java/com/madmath/core/mod"));
        while (!list.isEmpty()){
            File[] flist = list.pollFirst().listFiles();
            if(flist!=null){
                Arrays.stream(flist).forEach(file -> {
                    if(file.isDirectory())  list.add(file);
                    else {
                        if(file.getParent().substring(file.getParent().lastIndexOf("\\")+1).equals("Monster")){
                            String s = file.getPath().substring(file.getPath().indexOf("com"),file.getPath().lastIndexOf(".java")).replaceAll("\\\\",".");
                            System.out.println(s);

                            Utils.ModLoadMonsterPath.add(s);
                        }
                        else if(file.getParent().substring(file.getParent().lastIndexOf("\\")+1).equals("Texture")){
                            Utils.ModLoadMonsterTexture.add(file.getPath());
                            //System.out.println(Utils.ModLoadMonsterTexture.getLast().getHeight());
                        }
                    }
                });
            }
        }

        for (String path: Utils.ModLoadMonsterPath
        ) {
            System.out.println("ModLoadMonsterPath:"+path);
        }
        for (String path: Utils.ModLoadMonsterTexture
        ) {
            System.out.println("ModLoadMonsterTexture:"+path);
        }
    }


}

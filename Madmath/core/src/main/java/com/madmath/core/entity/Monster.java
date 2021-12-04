package com.madmath.core.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
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
    static public float FrameIdleInterval = 0.34f;
    static public float FrameRunInterval = 0.17f;

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
        Player player = gameScreen.player;
        Vector2 direction = new Vector2(player.getX()-getX(),player.getY()-getY());
        addAcceleration(direction);
        move(delta);
    }


    static public void loadMonsters(){
        searchModMonster();
        monsterSort = new Array<>();
        for (String name: Utils.AllDefaultMonsterSort) {
            try {
                Class<?> c = Class.forName("com.madmath.core.entity."+name);
                System.out.println(1);
                Constructor<?> con = c.getConstructor(Integer.class,AnimationManager.class);
                monsterSort.add((Monster) con.newInstance(500+ResourceManager.defaultManager.MonsterLoad.size,
                        new AnimationManager(
                        new CustomAnimation((float) c.getField("FrameIdleInterval").get(null),
                                new Array<>(ResourceManager.defaultManager.LoadMonsterAssetsByName(c.getField("alias").get(null) +"_idle_anim",
                                        (int)c.getField("TextureWidth").get(null),
                                        (int)c.getField("TextureHeight").get(null))[0])),
                        new CustomAnimation((float) c.getField("FrameRunInterval").get(null),
                                new Array<>(ResourceManager.defaultManager.LoadMonsterAssetsByName(c.getField("alias").get(null) +"_run_anim",
                                        (int)c.getField("TextureWidth").get(null),
                                        (int)c.getField("TextureHeight").get(null))[0])))));
                //monsterSort.add();
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
        monsterSort.sort((monster1,monster2)->{
            return monster2.level - monster1.level;
        });
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
                            Utils.ModLoadMonsterPath.add(file.getAbsolutePath());
                        }
                        else if(file.getParent().substring(file.getParent().lastIndexOf("\\")+1).equals("Texture")){
                            Utils.ModLoadMonsterTexture.add(new Texture(file.getPath()));
                            //System.out.println(Utils.ModLoadMonsterTexture.getLast().getHeight());
                        }
                    }
                });
            }
        }

        for (String path: Utils.ModLoadMonsterPath
        ) {
            System.out.println(path);
        }
    }

    public enum Sort{
        BigDemon
    }


}

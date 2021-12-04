/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:17
*/
package com.madmath.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.madmath.core.actor.AnimationActor;
import com.madmath.core.entity.Player;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;

public class HUD extends UI{

    Image[] fullHeart;
    Image[] halfHeart;
    Image[] emptyHeart;

    Container[] weaponBoxs;
    Equipment[] weapons;

    int maxHeart = 30;
    Label Health;

    Label MonsterCount;

    Table table;

    Player player;

    public HUD(GameScreen gameScreen, ResourceManager manager){
        super(gameScreen, manager);

        table = new Table(manager.skin);
        table.setBackground(new TextureRegionDrawable(manager.flzgbackground700x128));
        table.setPosition(0,0);
        table.setSize(700,128);

        weaponBoxs = new Container[3];
        weapons = new Equipment[3];
        for (int i = 0; i < 3; i++) {
            weaponBoxs[i] =new Container<Actor>(new Image(manager.contain_box_144x144));
            weaponBoxs[i].setBackground(new TextureRegionDrawable(manager.contain_box_144x144));
            table.add(weaponBoxs[i]).space(48);
        }

        fullHeart = new Image[maxHeart];
        halfHeart = new Image[maxHeart];
        emptyHeart = new Image[maxHeart];
        for (int i = 0; i < maxHeart; i++) {
            fullHeart[i] = new Image(manager.ui_heart_full16x16);
            halfHeart[i] = new Image(manager.ui_heart_half16x16);
            emptyHeart[i] = new Image(manager.ui_heart_empty16x16);
            fullHeart[i].setPosition(1f * 16 * i,128);
            halfHeart[i].setPosition(1f * 16 * i,128);
            emptyHeart[i].setPosition(1f * 16 * i,128);
            fullHeart[i].setScale(1f);
            halfHeart[i].setScale(1f);
            emptyHeart[i].setScale(1f);
            stage.addActor(emptyHeart[i]);
            stage.addActor(halfHeart[i]);
            stage.addActor(fullHeart[i]);
        }
        for (int i = 0; i < maxHeart; i++) {
            emptyHeart[i].setZIndex(1);
            halfHeart[i].setZIndex(2);
            fullHeart[i].setZIndex(3);
        }
        player = gameScreen.player;

        stage.addActor(table);

        MonsterCount = new Label("",new Label.LabelStyle(manager.font, Color.WHITE));
        table.add(MonsterCount).row();
        //dialog.setSize();
        //Health = new Label("",new Label.LabelStyle(manager.font, Color.WHITE ));
        //Health.setFontScale(0.5f);
        //Health.setPosition(0,120);
        //stage.addActor(Health);
    }

    @Override
    public void update(float dt) {
        //Health.setText(Integer.toString(player.getHp()));
        MonsterCount.setText("Monster number: "+(gameScreen.livingEntity.size-1));
        for (int i = 0; i < maxHeart; i++) {
            emptyHeart[i].setVisible((player.getMaxHp() + 1) / 2 > i);
            halfHeart[i].setVisible((player.getHp() + 1) / 2 > i);
            fullHeart[i].setVisible(player.getHp() / 2 > i);
        }
        for (int i = 0; i < player.weapon.size; i++) {
            try {
                if(!(weaponBoxs[i].getActor() instanceof Equipment) || weaponBoxs[i].getActor().getClass().getField("alias").get(null)!=player.weapon.get(i).getClass().getField("alias").get(null)){
                    weapons[i] = player.weapon.get(i).copy();
                    weaponBoxs[i].setActor(weapons[i]);
                    System.out.println("cc:"+i);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if(i== player.weapon.indexOf(player.activeWeapon,true)){
                weaponBoxs[i].setColor(player.activeWeapon.color.cpy());
            }else {
                weaponBoxs[i].setColor(Color.LIGHT_GRAY.cpy());
            }


        }
    }

    @Override
    public void render(float dt) {
        update(dt);
        stage.act();
        stage.draw();
    }


}

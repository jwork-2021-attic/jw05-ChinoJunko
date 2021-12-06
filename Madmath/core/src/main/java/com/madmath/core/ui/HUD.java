/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午4:17
*/
package com.madmath.core.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.madmath.core.entity.creature.Player;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.screen.GameScreen;

public class HUD extends UI{

    Image[] fullHeart;
    Image[] halfHeart;
    Image[] emptyHeart;

    ImageButton exitButton;

    Container[] weaponBoxs;
    Equipment[] weapons;

    int maxHeart = 70;
    Label Health;

    Label monsterCount;
    Label currentScore;

    VerticalGroup verticalGroup;

    Table table;

    Player player;

    public HUD(GameScreen gameScreen, ResourceManager manager){
        super(gameScreen, manager);

        table = new Table(manager.skin);
        table.setBackground(new TextureRegionDrawable(manager.flzgbackground700x128));
        table.setPosition(0,0);
        table.setSize(700,128);

        exitButton = new ImageButton(manager.exitButtonStyle);
        exitButton.setSize(100,50);
        exitButton.setPosition(viewport.getViewportWidth()-exitButton.getWidth(), viewport.getViewportHeight()-exitButton.getHeight() );
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.switchScreen(game.mainMenuScreen);
            }
        });
        stage.addActor(exitButton);

        weaponBoxs = new Container[3];
        weapons = new Equipment[3];
        for (int i = 0; i < 3; i++) {
            weaponBoxs[i] =new Container<Actor>(new Image(manager.contain_box_144x144));
            weaponBoxs[i].setBackground(new TextureRegionDrawable(manager.contain_box_144x144));
            table.add(weaponBoxs[i]).space(30);
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

        stage.addActor(table);

        monsterCount = new Label("",new Label.LabelStyle(manager.font, new Color(1f,0.3f,1f,1f)));
        monsterCount.setFontScale(0.8f);
        currentScore = new Label("",new Label.LabelStyle(manager.font,new Color(1f,0.3f,1f,1f)));
        currentScore.setFontScale(0.8f);
        verticalGroup = new VerticalGroup();
        verticalGroup.addActor(monsterCount);
        verticalGroup.addActor(currentScore);
        table.add(verticalGroup).expand().left();
        //dialog.setSize();
        //Health = new Label("",new Label.LabelStyle(manager.font, Color.WHITE ));
        //Health.setFontScale(0.5f);
        //Health.setPosition(0,120);
        //stage.addActor(Health);
    }

    public void show(){
        player = gameScreen.player;
        getStage().addAction(Actions.sequence(Actions.alpha(0),Actions.fadeIn(1f)));
    }

    @Override
    public void update(float dt) {
        //Health.setText(Integer.toString(player.getHp()));
        monsterCount.setText("Monster number: "+(gameScreen.livingEntity.size-1));
        currentScore.setText("Current Score: "+(player.score));
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
                    //System.out.println("cc:"+i);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if(i== player.weapon.indexOf(player.activeWeapon,true)){
                weaponBoxs[i].setColor(player.activeWeapon.color.cpy());
                weaponBoxs[i].getActor().setColor(player.activeWeapon.color.cpy());
            }else {
                weaponBoxs[i].setColor(Color.GRAY.cpy());
                weaponBoxs[i].getActor().setColor(Color.GRAY.cpy());
            }
        }
        //stage.act();
    }

    public void reset(){
        for (int i = 0; i < 3; i++) {
            if(weaponBoxs[i].getActor()!=null){
                weaponBoxs[i].removeActor(weaponBoxs[i].getActor());
            }
        }
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        update(dt);
        stage.act();
        //viewport.update();
        stage.draw();
    }


}

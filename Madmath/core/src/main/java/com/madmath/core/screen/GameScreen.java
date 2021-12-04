/**
*   @Author: Junko
*   @Email: imaizumikagerouzi@foxmail.com
*   @Date: 4/12/2021 下午3:57
*/
package com.madmath.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.madmath.core.entity.Entity;
import com.madmath.core.entity.Monster;
import com.madmath.core.entity.MonsterFactory;
import com.madmath.core.entity.Player;
import com.madmath.core.inventory.Item;
import com.madmath.core.inventory.equipment.Equipment;
import com.madmath.core.inventory.equipment.EquipmentFactory;
import com.madmath.core.main.MadMath;
import com.madmath.core.map.GameMap;
import com.madmath.core.resource.ResourceManager;
import com.madmath.core.thread.MonsterThread;
import com.madmath.core.thread.PlayerThread;
import com.madmath.core.ui.HUD;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;

public class GameScreen extends AbstractScreen{

    static private GameScreen CurrencyGameScreen;

    HUD hud;

    GameMap map;

    InputMultiplexer multiplexer;

    ExecutorService executorService;

    public Semaphore playerSemaphore;
    public Semaphore monsterSemaphore;

    public Player player;
    public MonsterThread monsterManager;

    private MonsterFactory monsterFactory;
    private EquipmentFactory equipmentFactory;

    Label currencyMapMessage;

    State state;

    //collision detection
    public Array<Entity> livingEntity;
    public Array<Item> livingItem;

    float stateTime;
    float currencyDelta;

    public State getState() {
        return state;
    }

    public enum State{
        PAUSE,
        RUNING,
        END,
    }

    public GameScreen(final MadMath game, final ResourceManager manager){
        super(game, manager);

        state = State.PAUSE;

        CurrencyGameScreen = this;

        map = new GameMap(this,"PRIMARY",1);
        initMapTitle();

        livingEntity = new Array<>();
        livingItem = new Array<>();

        hud = new HUD(this, manager);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);

        monsterFactory = new MonsterFactory(manager,this);
        monsterManager = new MonsterThread();

        equipmentFactory = new EquipmentFactory(manager, this);

        playerSemaphore = new Semaphore(0);
        monsterSemaphore = new Semaphore(0);

        executorService = Executors.newCachedThreadPool();
        executorService.execute(new PlayerThread());
        executorService.execute(monsterManager);
        executorService.shutdown();
    }

    @Override
    public void show() {
        super.show();
        hud.getStage().addAction(Actions.sequence(Actions.alpha(0),Actions.fadeIn(1f)));
        currencyMapMessage.setPosition(stage.getViewport().getWorldWidth()/2-50, MadMath.V_HEIGHT-20);
        stage.addActor(currencyMapMessage);
        currencyMapMessage.setZIndex(1000);
        state = State.RUNING;
        CurrencyGameScreen = this;
        Gdx.input.setInputProcessor(multiplexer);
        stateTime = 0;
        createMonsters(1);
        createEquipment();
    }

    public void initMapTitle(){
        currencyMapMessage = new Label("LEVEL "+map.mapLevel+"  "+map.name, new Label.LabelStyle(manager.font, Color.YELLOW ));
        currencyMapMessage.setFontScale(0.5f);
        currencyMapMessage.setVisible(true);
        currencyMapMessage.addAction(Actions.sequence(Actions.delay(5f),Actions.run(() -> currencyMapMessage.setText("   GOOD LUCK!   "))));
    }

    public void updateCamera(){
        if(camera.zoom!=1f) {
            camera.position.x = Math.min(Math.max(player.getX()+(0.5f-0.382f)*camera.zoom*stage.getViewport().getWorldWidth(), 0.5f * camera.zoom * stage.getViewport().getWorldWidth()),map.playAreaSize.x-0.5f* camera.zoom*stage.getViewport().getWorldWidth());
            camera.position.y = Math.min(Math.max(player.getY(), 0.5f * camera.zoom * stage.getViewport().getWorldHeight()),(1-0.5f* camera.zoom)*stage.getViewport().getWorldHeight());
        }else {
            camera.position.x = Math.min(Math.max(player.getX()+(0.5f-0.382f)*stage.getViewport().getWorldWidth(), (float) stage.getViewport().getWorldWidth()/2),map.startPosition.x+map.playAreaSize.x-stage.getViewport().getWorldWidth()/2);
        }
        camera.update();
        //System.out.println("Playerx:"+player.getX()+"    viewportwith:"+camera.viewportWidth+"    playerArs:"+map.playAreaSize.x);
    }

    @Override
    public void update(float v) {
        super.update(v);
        updateCamera();
    }

    @Override
    public void render(float v) {
        super.render(v);
        stateTime += v;
        currencyDelta = v;
        playerSemaphore.release();
        update(v);
        map.render(v);
        Sort.instance().sort(stage.getRoot().getChildren(), (o1, o2) -> (int) (o2.getY() - o1.getY()));
        stage.act(v);
        stage.draw();
        hud.render(v);
    }

    public void createMonsters(float factor) {
        if(map==null)   return;
        int totalLevel = Math.round((map.mapLevel*16 + 32) * factor);
        int capacity = 100;
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < capacity && totalLevel>0; i++) {
            try{
                totalLevel -= Objects.requireNonNull(generateMonster((String) Monster.monsterSort.get(random.nextInt(Monster.monsterSort.size)).getClass().getField("alias").get(null))).level;
            } catch (NoSuchFieldException | IllegalAccessException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    public Monster generateMonster(String name) throws TimeoutException {//alias
        Monster monster = monsterFactory.generateMonsterByName(name);
        monster.setPosition(map.getAvailablePosition(monster));
        monsterManager.addMonster(monster);
        stage.addActor(monster);
        //monster.setZIndex((int) monster.getY());
        livingEntity.add(monster);
        return monster;
    }

    public void createEquipment(){
        Equipment equipment = equipmentFactory.generateEquipmentByName("EvenSword");
        equipment.setPosition(player.getX()+100,player.getY()+50);
        stage.addActor(equipment);
        livingItem.add(equipment);
        equipment = equipmentFactory.generateEquipmentByName("OddSword");
        equipment.setPosition(player.getX()+100,player.getY()-50);
        stage.addActor(equipment);
        livingItem.add(equipment);
    }

    public GameMap getMap() {
        return map;
    }

    public float getCurrencyDelta() {
        return currencyDelta;
    }

    public void addInputProcessor(InputProcessor inputProcessor){
        multiplexer.addProcessor(0,inputProcessor);
    }

    public static GameScreen getCurrencyGameScreen() {
        return CurrencyGameScreen;
    }
}

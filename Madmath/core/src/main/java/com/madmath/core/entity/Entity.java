package com.madmath.core.entity;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.madmath.core.actor.AnimationActor;
import com.madmath.core.util.Utils;
import com.madmath.core.animation.AnimationManager;
import com.madmath.core.screen.GameScreen;

import java.lang.reflect.Array;

public abstract class Entity extends AnimationActor {

    protected int id;
    protected String name;

    protected boolean pauseAnim = false;

    public Rectangle box;
    public Vector2 boxOffset;

    public GameScreen gameScreen;

    //positive right and up
    protected Vector2 currentDirection = new Vector2(0,0);

    public float inertia;

    //if move set it to 16, reduce by half per frame
    //public int lastMove;

    public boolean canMove = true;

    protected float speed;
    //protected float lostSpeed;
    //protected Vector2 acceleration=new Vector2(0,0);


    //RPG
    protected int maxHp;
    protected int hp;

    protected Entity(AnimationManager animationManager){
        this(-1,animationManager);
    }

    protected Entity(Integer id, AnimationManager animationManager){
        this(id,animationManager,GameScreen.getCurrencyGameScreen());
    }

    public Entity(Integer id, AnimationManager animationManager, GameScreen gameScreen, Vector2 position){
        super(animationManager);
        this.id = id;
        this.gameScreen = gameScreen;
        initSelf();
        setPosition(position);
    }

    public Entity(Integer id, AnimationManager animationManager, GameScreen gameScreen){
        this(id,animationManager,gameScreen,new Vector2(0,0));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        //lastMove >>= 1;
        /*
        float dx=(currentDirection.x>0?1:-1),dy=(currentDirection.y>0?1:-1);
        currentDirection.x -= dx * Math.min(Math.abs(currentDirection.x),lostSpeed*delta);
        currentDirection.y -= dy * Math.min(Math.abs(currentDirection.y),lostSpeed*delta);
        currentDirection.x = Math.min(Math.max(currentDirection.x + 3*acceleration.x*delta,-1),1);
        currentDirection.y = Math.min(Math.max(currentDirection.y + 3*acceleration.y*delta,-1),1);
         */
        if(getState()==State.Move){
            anim_dirt = currentDirection.x < 0 || (currentDirection.x == 0 && anim_dirt);
            setPlayMode(AnimationManager.PlayMode.Moving);
        }else {
            setPlayMode(AnimationManager.PlayMode.Stand);
        }
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean move(float v){
        if(getState()!=State.Move) return false;
        float moveDistance = speed * v;
        Vector2 next1 = new Vector2(getPosition());
        Vector2 next2 = new Vector2(getPosition());
        Vector2 next3 = new Vector2(getPosition());
        next1.x += currentDirection.x*moveDistance;
        next1.y += currentDirection.y*moveDistance;
        next2.x = next1.x;
        next3.y = next1.y;
        if(isCanMove(next1)){
            setPosition(next1);
            return true;
        }else if(isCanMove(next2)){
            setPosition(next2);
            return true;
        }else if(isCanMove(next3)){
            setPosition(next3);
            return true;
        }
        return false;
    }

    public void initSelf() {
        speed = 16f;
        maxHp = 1;
        hp = 1;
        box = new Rectangle(0,0,12,7);
        boxOffset = new Vector2(2,0);
        inertia = 0.1f;
    }

    @Override
    public void setPosition(Vector2 position) {
        box.setPosition(position);
        super.setPosition(position.sub(boxOffset));
    }

    public State getState(){
        if(currentDirection.len2()>0.1) return State.Move;
        return State.Stand;
    }

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
        for (Entity entity: gameScreen.livingBox
        ) {
            if(entity != this && entity.box.overlaps(nextBox))  {
                return false;
            }
        }
        return true;
    }

    public void setAnimDirection(boolean direction){
        anim_dirt = direction;
    }

    public void addAcceleration(Vector2 Direction) {
        currentDirection.x = Math.min(Math.max(currentDirection.x + Direction.x,-1),1);
        currentDirection.y = Math.min(Math.max(currentDirection.y + Direction.y,-1f),1f);
    }

    public void setAcceleration(Vector2 Direction) {
        currentDirection.set(Direction);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public enum State{
        Stand,
        Move,
    }
}
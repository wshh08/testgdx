package com.mygdx.game.controller;

import com.mygdx.game.model.World;
import com.mygdx.game.model.Bob;
import com.mygdx.game.view.WorldRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wshh08 on 15-10-21.
 */
public class BobController {
    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private static final long LONG_JUMP_PRESS   = 150l;
    private static final float ACCELERATION     = 20f;
    private static final float GRAVITY          = -20f;
    private static final float MAX_JMUP_SPEED   = 7f;
    private static final float DAMP             = 0.90f;
    private static final float MAX_VEL          = 4f;

    //these are temporary
    private static final float WIDTH            = 10f;

    private World world;
    private Bob bob;
    private long jumpPressedTime;
    private boolean jumpingPressed;

    static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
    static{
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    }
    public BobController(World world) {
        this.world = world;
        this.bob = world.getBob();
    }
    public void leftPressed() {
        keys.put(Keys.LEFT, true);
    }
    public void rightPressed() {
        keys.put(Keys.RIGHT, true);
    }
    public void jumpPressed() {
        keys.put(Keys.JUMP, true);
    }
    public void firePressed() {
        keys.put(Keys.FIRE, true);
    }
    public void leftReleased() {
        keys.put(Keys.LEFT, false);
    }
    public void rightReleased() {
        keys.put(Keys.RIGHT, false);
    }
    public void jumpReleased() {
        keys.put(Keys.JUMP, false);
    }
    public void fireReleased() {
        keys.put(Keys.FIRE, false);
    }
    public void update(float delta){
        processInput();
        bob.getAcceleration().y = GRAVITY;
        bob.getVelocity().add(bob.getAcceleration().x, bob.getAcceleration().y);
        bob.getAcceleration().scl(delta);
        bob.update(delta);
    }
    private void processInput() {
        if (keys.get(Keys.LEFT)){
            bob.setFacingLeft(true);
            bob.setStatue(Bob.State.WALKING);
            bob.getVelocity().x = -Bob.SPEED;
        }
        if(keys.get(Keys.RIGHT)) {
            bob.setFacingLeft(false);
            bob.setStatue(Bob.State.WALKING);
            bob.getVelocity().x = Bob.SPEED;
        }
        if((keys.get(Keys.LEFT)&&keys.get(Keys.RIGHT))||
                (!keys.get(Keys.LEFT)&&!keys.get(Keys.RIGHT))){
            bob.setStatue(Bob.State.IDLE);
            bob.getVelocity().x = 0;
            bob.getAcceleration().x = 0;
        }
    }
}













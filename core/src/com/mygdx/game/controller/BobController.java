package com.mygdx.game.controller;

import com.mygdx.game.model.World;
import com.mygdx.game.model.Bob;
//import com.mygdx.game.view.WorldRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wshh08 on 15-10-21.
 */
public class BobController {
    enum Keys {
        LEFT, RIGHT, JUMP, FIRE
    }

    private static final long LONG_JUMP_PRESS   = 200l; /*最大的连续按Jump时间millisecond */
    private static final float ACCELERATION     = 10f;  /*水平方向运动的启动加速度*/
    private static final float GRAVITY          = -10f; /*重力加速度，竖直方向减速的速度Units/Sec^2 */
    private static final float MAX_JUMP_SPEED   = 7f;   /*跳跃速度最大值，Jump键按住后维持此Jump速度 Units/Sec */
    private static final float DAMP             = 0.90f;/*不按键时每循环水平速度的衰减系数*/
    private static final float MAX_VEL          = 4f;   /*水平速度最大值（walk状态加速到此值后维持匀速）*/

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
//        滞空时间小于LONG_JUMP_PRESS时，竖直方向速度由processInput()在每个循环初置为MAX_JUMP_SPEED（空中加油）;
//        超过LONG_JUMP_PRESS后processInput()将jumpPressed置false，bob竖直速度根据y轴加速度随时间变化。
        bob.getAcceleration().y = GRAVITY;
        bob.getAcceleration().scl(delta);       /* Velocity increase in a cycle equals Acc * delta */
        bob.getVelocity().add(bob.getAcceleration().x, bob.getAcceleration().y);    /*根据Acceleration累加delta秒得到循环末的Velocity *|* final velocity of cycle V1 equals V0 + Acc * delta*/
        if (bob.getAcceleration().x == 0) bob.getVelocity().x *= DAMP;      /*没有按左、右键时每个循环水平方向速度乘以0.9以实现平滑停止Walk的效果*/
        if (bob.getVelocity().x > MAX_VEL) bob.getVelocity().x = MAX_VEL;
        if (bob.getVelocity().x < -MAX_VEL) bob.getVelocity().x = -MAX_VEL;
        bob.update(delta);
        if (bob.getPosition().y < 0) {
            bob.getPosition().y = 0f;
            bob.setPosition(bob.getPosition());
            if (bob.getState().equals(Bob.State.JUMPING))
                bob.setState(Bob.State.IDLE);
        }
        if (bob.getPosition().x < 0) {
            bob.getPosition().x = 0;
            bob.setPosition(bob.getPosition());
            if (!bob.getState().equals(Bob.State.JUMPING)) {
                bob.setState(Bob.State.IDLE);
            }
        }
        if (bob.getPosition().x > WIDTH - bob.getBounds().width) {
            bob.getPosition().x = WIDTH - bob.getBounds().width;
            bob.setPosition(bob.getPosition());
            if (!bob.getState().equals(Bob.State.JUMPING))
                bob.setState(Bob.State.IDLE);
        }
    }
    private boolean processInput() {
        if (keys.get(Keys.JUMP)) {
            if (!bob.getState().equals(Bob.State.JUMPING)) {    /*确定Bob在地面状态(不在JUMPING态)下才能jump*/
                jumpingPressed = true;
                jumpPressedTime = System.currentTimeMillis();
                bob.setState(Bob.State.JUMPING);
                bob.getVelocity().y = MAX_JMUP_SPEED;
            } else {
                if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime >= LONG_JUMP_PRESS))) {
                    jumpingPressed = false;
                } else {
                    if (jumpingPressed) {
//                        在bob的Jump过程中按住Jump键，bob会一直保持MAX_JUMP_SPEED直到超过LONG_JUMP_PRESS时间。
                        bob.getVelocity().y = MAX_JUMP_SPEED;           /*if jumpingPressed when bob in the air keep flying until LONG_JUMP_PRESS*/
                    }
                }
            }
        }
        if (keys.get(Keys.LEFT)){
            bob.setFacingLeft(true);
            if (!bob.getState().equals(Bob.State.JUMPING)) {
                bob.setState(Bob.State.WALKING);
            }
            bob.getAcceleration().x = -ACCELERATION;
        }else if(keys.get(Keys.RIGHT)) {
            bob.setFacingLeft(false);
            if (!bob.getState().equals(Bob.State.JUMPING)) {
                bob.setState(Bob.State.WALKING);
            }
            bob.getAcceleration().x = ACCELERATION;
        } else {
            if (!bob.getState().equals(Bob.State.JUMPING))
                bob.setState(Bob.State.IDLE);
            bob.getAcceleration().x = 0;
        }
//        if((keys.get(Keys.LEFT)&&keys.get(Keys.RIGHT))||
//                (!keys.get(Keys.LEFT)&&!keys.get(Keys.RIGHT))){
//            bob.setStatue(Bob.State.IDLE);
//            bob.getVelocity().x = 0;
//            bob.getAcceleration().x = 0;
//        }
        return false;
    }
}













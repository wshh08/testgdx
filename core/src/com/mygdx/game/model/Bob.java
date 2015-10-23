package com.mygdx.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by wshh08 on 15-10-19.
 */
public class Bob {
    float stateTime = 0;
    public enum State {
        IDLE, WALKING, JUMPING, DYING
    }
    public static final float SPEED = 2f;
//    unit per second
    static final float JUMP_VELOCITY = 1f;
    public static final float SIZE = 0.5f;
//    half a unit
    Vector2 position = new Vector2();
    Vector2 acceleration = new Vector2();
    Vector2 velocity = new Vector2();
    Rectangle   bounds = new Rectangle();
    State   state = State.IDLE;
    boolean facingLeft = true;

    public Bob(Vector2 position) {
        this.position = position;
        this.bounds.height = SIZE;
        this.bounds.width = SIZE;
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public Vector2 getPosition() {
        return position;
    }
    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }
    public void setStatue(State newState) {
        this.state = newState;
    }
    public Vector2 getVelocity() {
        return this.velocity;
    }
    public Vector2 getAcceleration() {
        return this.acceleration;
    }
    public boolean isFacingLeft() {
        if(facingLeft)
            return true;
        else
            return false;
    }
    public State getState() {
        return this.state;
    }
    public float getStateTime() {
        return stateTime;
    }
    public void update(float delta) {
        stateTime += delta;
        position.add(velocity.cpy().scl(delta));
    }
}

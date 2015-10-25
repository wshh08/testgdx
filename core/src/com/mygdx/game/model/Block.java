package com.mygdx.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by wshh08 on 15-10-19.
 */
public class Block {
    public static final float SIZE = 1f;
    Vector2     position = new Vector2();
    Rectangle   bounds = new Rectangle();

    public Block(Vector2 pos) {
        this.position = pos;
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
        this.bounds.x = pos.x;
        this.bounds.y = pos.y;
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public Vector2 getPosition() {
        return position;
    }
}

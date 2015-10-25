package com.mygdx.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.controller.BobController;
import com.mygdx.game.model.World;
import com.mygdx.game.view.WorldRenderer;

/**
 * Created by wshh08 on 15-10-20.
 */
public class GameScreen implements Screen, InputProcessor {

//    private World world;
    private WorldRenderer renderer;
    private BobController controller;

    private int width, height;

    @Override
    public void show() {
        World world = new World();
        renderer = new WorldRenderer(world, true);
        controller = new BobController(world);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        controller.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height){
        renderer.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {
        //
    }

    @Override
    public void resume() {
        //
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }
//    InputProcessor methods
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(Application.ApplicationType.Android))
            return false;
        if (x < width / 2 && y > height / 2)
            controller.leftPressed();
        if (x > width / 2 && y > height / 2)
            controller.rightPressed();
        if (y < height / 2)
            controller.jumpPressed();
        return true;
    }
    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(Application.ApplicationType.Android))
            return false;
        if (x < width / 2 && y > height / 2)
            controller.leftReleased();
        if (x > width / 2 && y > height / 2)
            controller.rightReleased();
        if (y < height / 2)
            controller.jumpReleased();
        return true;
    }
    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean mouseMoved(int x, int y) {
        return false;
    }
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}










package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.model.Block;
import com.mygdx.game.model.Bob;
import com.mygdx.game.model.World;

/**
 * Created by wshh08 on 15-10-20.
 */
public class WorldRenderer {

    private static final float RUNNING_FRAME_DURATION = 0.06f;

    private TextureRegion bobIdleLeft;
    private TextureRegion bobIdleRight;
    private TextureRegion blockTexture;
    private TextureRegion bobJumpLeft;
    private TextureRegion bobJumpRight;
    private TextureRegion bobFallLeft;
    private TextureRegion bobFallRight;

//    private TextureRegion bobFrame;
    private Animation walkLeftAnimation;
    private Animation walkRightAnimation;

    private static final float CAMERA_WIDTH = 10f;
    private static final float CAMERA_HEIGHT = 7f;

    private World world;
    private OrthographicCamera cam;

    /*for debug rendering*/
    ShapeRenderer debugRenderer = new ShapeRenderer();

//    private Texture bobTexture;
//    private Texture blockTexture;

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
//  pixes per unit on the X or Y axis
    private float ppuX;
    private float ppuY;

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
        ppuX = (float)width / CAMERA_WIDTH;
        ppuY = (float)height / CAMERA_HEIGHT;
    }


    public WorldRenderer(World world, boolean debug) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    public void loadTextures() {
//        bobTexture = new Texture(Gdx.files.internal("images/bob_01.png"));
//        blockTexture = new Texture(Gdx.files.internal("images/block.png"));
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/textures.atlas"));
        bobJumpLeft = atlas.findRegion("bob-up");
        bobJumpRight = new TextureRegion(bobJumpLeft);
        bobJumpRight.flip(true, false);
        bobFallLeft = atlas.findRegion("bob-down");
        bobFallRight = new TextureRegion(bobFallLeft);
        bobFallRight.flip(true, false);
        bobIdleLeft = atlas.findRegion("bob-01");
        bobIdleRight = new TextureRegion(bobIdleLeft);
        bobIdleRight.flip(true, false);
        blockTexture = atlas.findRegion("block");
        TextureRegion[] walkLeftFrames = new TextureRegion[5];
        for(int i=0; i<5; i++) {
            walkLeftFrames[i] = atlas.findRegion("bob-0" + (i + 2));
        }
        walkLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkLeftFrames);
        TextureRegion[] walkRightFrames = new TextureRegion[5];
        for (int i=0; i<5; i++){
            walkRightFrames[i] = new TextureRegion(walkLeftFrames[i]);
            walkRightFrames[i].flip(true, false);
        }
        walkRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkRightFrames);
    }

    public void render() {
        spriteBatch.begin();
//        drawBlocks();
        drawBob();
        spriteBatch.end();
        if(debug)
            drawDebug();
    }

    private void drawBlocks() {
        for(Object block : world.getBlocks()) {
            spriteBatch.draw(blockTexture, ((Block)block).getPosition().x*ppuX, ((Block)block).getPosition().y*ppuY,Block.SIZE*ppuX,Block.SIZE*ppuY);
        }
    }

    private void drawBob() {
        Bob bob = world.getBob();
//        spriteBatch.draw(bobTexture, (bob.getPosition().x+0.25f)*ppuX, bob.getPosition().y*ppuY, Bob.SIZE*ppuX, Bob.SIZE*ppuY);
        TextureRegion bobFrame = bob.isFacingLeft() ? bobIdleLeft : bobIdleRight;
        if (bob.getState().equals(Bob.State.WALKING))
            bobFrame = bob.isFacingLeft() ? walkLeftAnimation.getKeyFrame(bob.getStateTime(),
                    true) : walkRightAnimation.getKeyFrame(bob.getStateTime(), true);
        else if (bob.getState().equals(Bob.State.JUMPING)) {
            if (bob.getVelocity().y > 0) {
                bobFrame = bob.isFacingLeft() ? bobJumpLeft : bobJumpRight;
            } else {
                bobFrame = bob.isFacingLeft() ? bobFallLeft : bobFallRight;
            }
        }
        spriteBatch.draw(bobFrame, (bob.getPosition().x /* + Bob.SIZE / 2 */ )*ppuX, bob.getPosition().y*ppuY,
                Bob.SIZE*ppuX, Bob.SIZE*ppuY);

    }

    public void drawDebug() {
//  render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(Object block : world.getBlocks())
        {
            Rectangle rect = ((Block)block).getBounds();
            float x = ((Block)block).getPosition().x/* + rect.x*/;
            float y = ((Block)block).getPosition().y/* + rect.y*/;
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(x, y, rect.width, rect.height);
        }
//  render bob
//        debugRenderer.end();
//        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Bob bob = world.getBob();
        Rectangle rect = bob.getBounds();
        float x = bob.getPosition().x + 0.25f;
        float y = bob.getPosition().y;
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(x, y, rect.width, rect.height);
        debugRenderer.end();
    }
}

package pl.dominik.hinc.bounceback.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.Light;
import box2dLight.PointLight;
import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.tools.Collidable;
import pl.dominik.hinc.bounceback.tools.InputListener;
import pl.dominik.hinc.bounceback.tools.RenderableEntity;
import pl.dominik.hinc.bounceback.tools.Updatable;

public class Player implements Collidable, Updatable, InputListener, RenderableEntity {
    private  BounceBack context;
    private Body playerBody;
    private Fixture playerFixture;
    private boolean isDead = false;
    private Vector2 playerVeloBeforeDead;
    private Sprite playerSprite;
    private float playerDiameter = 0.5f;
    private float texturePlusSize = 0.1f;
    private float degrees;

    public Player(BounceBack context){
        this.context = context;
        playerVeloBeforeDead = new Vector2();
    }

    public void createPlayerBody(){
        Vector2 startingPos = new Vector2(context.getScreenViewport().getWorldWidth()/2,context.getScreenViewport().getWorldHeight()/2);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.25f,0.25f);
        //CircleShape shape = new CircleShape();
        //shape.setRadius(playerDiameter /2);

        //Player Body
        BounceBack.BODY_DEF.position.set(startingPos);
        BounceBack.BODY_DEF.fixedRotation = true;
        BounceBack.BODY_DEF.gravityScale = 2;
        BounceBack.BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        //Player Fixture
        BounceBack.FIXTURE_DEF.shape =shape;
        BounceBack.FIXTURE_DEF.filter.categoryBits = BounceBack.PLAYER_BIT;
        //Create Body And Fixture
        playerBody = context.getWorld().createBody(BounceBack.BODY_DEF);
        playerFixture = playerBody.createFixture(BounceBack.FIXTURE_DEF);
        playerBody.setLinearVelocity(2.5f,0);
        playerFixture.setUserData(this);

        /*BounceBack.BODY_DEF.type = BodyDef.BodyType.KinematicBody;
        BounceBack.BODY_DEF.position.set(new Vector2(3,7));
        BounceBack.BODY_DEF.linearVelocity.set(1,0);

        context.getWorld().createBody(BounceBack.BODY_DEF).createFixture(BounceBack.FIXTURE_DEF);*/
        context.resetFixtureAndBodyDef();
        shape.dispose();
        context.getInputManager().addListener(this);
        prepareTexture();
        //Player Light
        context.getGameRenderer().createPlayerLight();
        //playerLight = new PointLight(context.getRayHandler(),2048,new Color(0.4f,0.4f,0.4f,1f),5,playerBody.getPosition().x,playerBody.getPosition().y);
        //playerLight.attachToBody(playerBody);


    }

    private void prepareTexture() {
        playerSprite = new Sprite(context.getCurrentBirdTexture());
        playerSprite.setSize(playerDiameter +texturePlusSize, playerDiameter +texturePlusSize);
        playerSprite.setOrigin(playerSprite.getWidth()/2,playerSprite.getHeight()/2);
        context.getGameRenderer().addRenderableEntity(this);
    }

    @Override
    public void handleCollision(Fixture fixture) {
        if(fixture.getUserData() instanceof Spike){
            Gdx.app.debug("Player","Game Over");
            isDead = true;
            playerVeloBeforeDead = playerBody.getLinearVelocity();
        }
    }
    private void createDeadPlayerRemainings(Vector2 playerPos){
        PolygonShape shape = new PolygonShape();
        BounceBack.BODY_DEF.fixedRotation = false;
        BounceBack.BODY_DEF.gravityScale = 2;
        BounceBack.BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        BounceBack.FIXTURE_DEF.filter.categoryBits = BounceBack.OTHER_OBJECT_BIT;
        BounceBack.FIXTURE_DEF.filter.maskBits = BounceBack.WALL_BIT | BounceBack.SPIKE_BIT;
        BounceBack.FIXTURE_DEF.restitution = 0.1f;
        BounceBack.FIXTURE_DEF.shape =shape;

        float remainConst = 0.2f;
        for(float i = -remainConst; i<=remainConst; i+=remainConst){
            for(float j = -remainConst; j<=remainConst; j+=remainConst){
                float posA = playerPos.x + i;
                float posB = playerPos.y + j;
                BounceBack.BODY_DEF.position.set(new Vector2(posA,posB));
                BounceBack.BODY_DEF.linearVelocity.set(playerVeloBeforeDead.x+i* MathUtils.random(10f),playerVeloBeforeDead.y+j* MathUtils.random(10f));
                shape.setAsBox(remainConst/3,remainConst/3,new Vector2(0,0),0);
                new PlayerRemains(playerPos,context,i,j,remainConst,degrees);

            }
        }
        context.resetFixtureAndBodyDef();
        shape.dispose();
    }
    public void render(SpriteBatch spriteBatch){
        if(playerSprite != null){
            playerSprite.setPosition(playerBody.getPosition().x - playerDiameter /2-texturePlusSize/2,playerBody.getPosition().y - playerDiameter /2-texturePlusSize/2);
            degrees = playerBody.getLinearVelocity().y * 5;
            if (playerBody.getLinearVelocity().y >= 0 && degrees > 50){
                degrees = 50;
            }else if (playerBody.getLinearVelocity().y < 0 && degrees < -50){
                degrees = -50;
            }
            if(context.getSpikeCreator().isGoRight() == false){
                degrees *= -1;
            }
            playerSprite.setRotation(degrees);
            playerSprite.draw(spriteBatch);
        }
    }
    @Override
    public void update() {
        if(isDead){
            createDeadPlayerRemainings(playerBody.getPosition());
            playerBody.setTransform(context.getScreenViewport().getWorldWidth()/2,context.getScreenViewport().getWorldHeight()/2,0);
            playerBody.setLinearVelocity(2.5f,0);
            int temp = context.getScore();
            context.setScore(-1);
            context.getSpikeCreator().updateSpikes();
            context.setScore(temp);
            //context.getWorld().destroyBody(playerBody);
            //playerBody = null;
            isDead = false;
        }
        if(playerBody != null){
            if (Gdx.input.isKeyPressed(Input.Keys.PLUS)){
                OrthographicCamera camera = (OrthographicCamera) context.getScreenViewport().getCamera();
                camera.zoom -= 0.05f;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.MINUS)){
                OrthographicCamera camera = (OrthographicCamera) context.getScreenViewport().getCamera();
                camera.zoom += 0.05f;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                context.getCamera().translate(-0.05f,0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                context.getCamera().translate(0.05f,0);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                context.getCamera().translate(0,0.05f);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                context.getCamera().translate(0,-0.05f);

            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                context.setPaused(true);
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.ALT_RIGHT)){
                context.setPaused(false);
            }
            if(context.getSpikeCreator().isGoRight()){
                playerBody.setLinearVelocity(2.5f,playerBody.getLinearVelocity().y);
                playerSprite.setFlip(false,false);
                playerBody.setTransform(playerBody.getPosition(),0);
                //playerLight.setDirection(0);
            }else if(context.getSpikeCreator().isGoRight() == false){
                playerBody.setLinearVelocity(-2.5f,playerBody.getLinearVelocity().y);
                playerSprite.setFlip(true,false);
                playerBody.setTransform(playerBody.getPosition(),MathUtils.PI);
                //playerLight.setDirection(0);
            }
            if(context.getGameScreen().isInMenu()){
                //playerBody.setTransform(context.getScreenViewport().getWorldWidth()/2,context.getScreenViewport().getWorldHeight()/2,0);
                playerBody.setActive(false);
            }
        }


    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button) {

        if(!context.isPaused()){
            if(!context.getGameScreen().isInMenu()){
                playerBody.applyLinearImpulse(new Vector2(0,2.5f),playerBody.getWorldCenter(),true);
            }
        }

    }

    public Body getPlayerBody() {
        return playerBody;
    }
}

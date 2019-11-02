package pl.dominik.hinc.bounceback.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.Light;
import box2dLight.PointLight;
import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.enums.SpikeOrientation;
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
    public final float playerDiameter = 0.5f;
    public final float texturePlusSize = 0.1f;
    private float jumpForce = 2.5f;
    private float degrees;
    private Array<PlayerRemains> playerRemainsArray;
    //test
    private Pixmap pixmap;
    boolean textureWasCreated = false;
    //PowerUp Bools
    private boolean isShielded = false;

    public Player(BounceBack context){
        this.context = context;
        playerVeloBeforeDead = new Vector2();
        playerRemainsArray = new Array<>();
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

        context.resetFixtureAndBodyDef();
        shape.dispose();
        context.getInputManager().addListener(this);
        prepareTexture(context.getCurrentBirdTexture());
        //Player Light
        context.getGameRenderer().createPlayerLight();


    }

    private void prepareTexture(Texture texture) {
        playerSprite = new Sprite(texture);
        playerSprite.setSize(playerDiameter +texturePlusSize, playerDiameter +texturePlusSize);
        playerSprite.setOrigin(playerSprite.getWidth()/2,playerSprite.getHeight()/2);
        context.getGameRenderer().addRenderableEntity(this);

    }

    @Override
    public void handleCollision(Fixture fixture) {
        if(fixture.getUserData() instanceof Spike){
            if(isShielded){
                //isShielded = false;
                context.getParticleManager().setShieldDestroyed(true);
                if(((Spike) fixture.getUserData()).getSpikeOrientation() == SpikeOrientation.LEFT || ((Spike) fixture.getUserData()).getSpikeOrientation() == SpikeOrientation.RIGHT ) {
                    ((Spike) fixture.getUserData()).setToDestroy(true);
                }
                context.getSpikeCreator().setUpdateOneSpike(true);
            }else{
                Gdx.app.debug("Player","Game Over");
                isDead = true;
                playerVeloBeforeDead = playerBody.getLinearVelocity();
            }

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
                playerRemainsArray.add(new PlayerRemains(playerPos,context,i,j,remainConst,degrees,playerSprite.getTexture()));

            }
        }
        context.resetFixtureAndBodyDef();
        shape.dispose();
    }
    public void render(SpriteBatch spriteBatch){
        if(playerSprite != null){
            if(context.getScore()%5 == 0 && textureWasCreated == false){
                if(!context.getCurrentBirdTexture().getTextureData().isPrepared()){
                    context.getCurrentBirdTexture().getTextureData().prepare();
                }
                pixmap = context.getCurrentBirdTexture().getTextureData().consumePixmap();
                for (int y = 0; y < pixmap.getHeight(); y++){
                    for (int x = 0; x < pixmap.getWidth(); x++){
                        Color c = context.getColorManager().currentColor;
                        Color color = new Color();
                        Color.rgba8888ToColor(color,pixmap.getPixel(x,y));
                        float addedToCol = 0;
                        if (color.r == 1 && color.g == 0 & color.b == 0){
                            addedToCol = 0.2f;
                            color.r = c.r + addedToCol;
                            color.g = c.g + addedToCol;
                            color.b = c.b + addedToCol;
                            //Gdx.app.debug("Red","Yes");

                        }else if(color.r == 0 && color.g == 0 & color.b == 1){
                            addedToCol = 0.1f;
                            color.r = c.r + addedToCol;
                            color.g = c.g + addedToCol;
                            color.b = c.b + addedToCol;
                            //Gdx.app.debug("Blue","Yes");
                        }else if(color.r == 0 && color.g == 1 & color.b == 0){
                            addedToCol = 0.005f;
                            color.r = c.r + addedToCol;
                            color.g = c.g + addedToCol;
                            color.b = c.b + addedToCol;
                            //Gdx.app.debug("Green","Yes");
                        }else if(color.r == 1 && color.g == 1 & color.b == 1){
                            color = Color.WHITE;
                            //Gdx.app.debug("White","Yes");
                        }
                        if(color.a != 0){
                            color.a = 1;
                            pixmap.setColor(color);
                            pixmap.fillRectangle(x,y,1,1);
                        }
                    }
                }

                playerSprite.setTexture(new Texture(pixmap));
                textureWasCreated = true;
                pixmap.dispose();
            }
            if(context.getScore()%5 != 0){
                textureWasCreated = false;
            }
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
            doThingAfterDeath();

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
                playerSprite.setFlip(false,context.isReverseSprites());
                playerBody.setTransform(playerBody.getPosition(),0);
                //playerLight.setDirection(0);
            }else if(context.getSpikeCreator().isGoRight() == false){
                playerBody.setLinearVelocity(-2.5f,playerBody.getLinearVelocity().y);
                playerSprite.setFlip(true,context.isReverseSprites());
                playerBody.setTransform(playerBody.getPosition(),MathUtils.PI);
                //playerLight.setDirection(0);
            }
            if(context.getGameScreen().isInMenu()){
                //playerBody.setTransform(context.getScreenViewport().getWorldWidth()/2,context.getScreenViewport().getWorldHeight()/2,0);
                playerBody.setActive(false);
            }
        }


    }

    private void doThingAfterDeath() {
        //TODO move it somewhere where it will be more suitable
        context.getParticleManager().playerDied(new Vector2(playerBody.getPosition()));
        createDeadPlayerRemainings(playerBody.getPosition());
        playerBody.setTransform(context.getScreenViewport().getWorldWidth()/2,context.getScreenViewport().getWorldHeight()/2,0);
        playerBody.setLinearVelocity(2.5f,0);
        int temp = context.getScore();
        context.setScore(-1);
        context.getSpikeCreator().updateSpikes();
        context.setScore(temp);
        //context.getWorld().destroyBody(playerBody);
        //playerBody = null;
        if (context.getUpsideDownViewManager().isUpsidedown()){
            context.getUpsideDownViewManager().setSpsideDownView(false);
        }
        context.getPowerUpManager().reset();
        isDead = false;
    }

    public boolean isShielded() {
        return isShielded;
    }

    public void setShielded(boolean shielded) {
        isShielded = shielded;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button) {

        if(!context.isPaused()){
            if(!context.getGameScreen().isInMenu()){
                playerBody.applyLinearImpulse(new Vector2(0,jumpForce),playerBody.getWorldCenter(),true);
                context.getParticleManager().playerJumpParticle(playerBody.getPosition());
            }
        }

    }

    public float getJumpForce() {
        return jumpForce;
    }

    public void setJumpForce(float jumpForce) {
        this.jumpForce = jumpForce;
    }

    public Body getPlayerBody() {
        return playerBody;
    }

    public Array<PlayerRemains> getPlayerRemainsArray() {
        return playerRemainsArray;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }
}

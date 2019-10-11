package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.entities.Player;
import pl.dominik.hinc.bounceback.powerups.PowerUp;
import pl.dominik.hinc.bounceback.tools.Collidable;
import pl.dominik.hinc.bounceback.tools.RenderableEntity;

public abstract class AbstractPowerUp implements PowerUp, Collidable, RenderableEntity {
    protected BounceBack context;
    protected Body body;
    protected Fixture fixture;
    public boolean isSpawned;
    protected Vector2 spawnPos;
    public boolean destroyBody = false;
    private Vector2 upMoveBorder;
    private Vector2 downMoveBorder;
    private boolean goUp = true;
    private Vector2 velocity;
    protected float boxSize = 0.4f;
    protected Sprite powerUpSprite;

    public AbstractPowerUp(BounceBack context){
        this.context = context;
        spawnPos = new Vector2(context.getScreenViewport().getWorldWidth()/2,context.getScreenViewport().getWorldHeight()/2);
        upMoveBorder = new Vector2(context.getScreenViewport().getWorldWidth()/2,context.getScreenViewport().getWorldHeight() - context.getScreenViewport().getWorldHeight()/4);
        downMoveBorder = new Vector2(context.getScreenViewport().getWorldWidth()/2,context.getScreenViewport().getWorldHeight()/4);
        velocity = new Vector2(0,0);

    }

    public void spawn(){
        isSpawned = true;
        goUp = MathUtils.randomBoolean();
        BounceBack.BODY_DEF.type = BodyDef.BodyType.KinematicBody;
        BounceBack.BODY_DEF.position.set(spawnPos);
        BounceBack.BODY_DEF.fixedRotation = true;


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boxSize/2,boxSize/2);

        BounceBack.FIXTURE_DEF.shape = shape;
        BounceBack.FIXTURE_DEF.isSensor = true;
        BounceBack.FIXTURE_DEF.filter.categoryBits = BounceBack.POWERUP_BIT;

        body = context.getWorld().createBody(BounceBack.BODY_DEF);
        fixture = body.createFixture(BounceBack.FIXTURE_DEF);
        fixture.setUserData(this);

        body.setAwake(true);

        shape.dispose();
        context.resetFixtureAndBodyDef();
    }
    protected void prepareTexture(Sprite sprite){
        powerUpSprite = sprite;
        powerUpSprite.setSize(boxSize,boxSize);
        powerUpSprite.setOrigin(powerUpSprite.getWidth()/2,powerUpSprite.getHeight()/2);
        context.getGameRenderer().addRenderableEntity(this);
    }

    @Override
    public void handleCollision(Fixture fixture) {
        if (fixture.getUserData() instanceof Player){
            deletePowerUp();
            use();
        }
    }
    public void deletePowerUp(){
        powerUpSprite = null;
        destroyBody = true;
        context.getPowerUpManager().isSpawned = false;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if(body != null && powerUpSprite != null){
            powerUpSprite.setPosition(body.getPosition().x - boxSize/2,body.getPosition().y - boxSize/2);
            powerUpSprite.setFlip(false,context.isReverseSprites());
            powerUpSprite.draw(spriteBatch);
        }
    }

    public boolean isToDestroy() {
        return destroyBody;
    }

    @Override
    public Body getBody() {
        return body;
    }

    public void move(){
        if (goUp){
            if(velocity.y < 3){
                velocity.y += 0.05f;
            }
            body.setLinearVelocity(velocity);
            if (body.getPosition().y > upMoveBorder.y){
                goUp = false;
            }
        }else if (goUp == false){
            if(velocity.y > -3){
                velocity.y -= 0.05f;
            }
            body.setLinearVelocity(velocity);
            if (body.getPosition().y < downMoveBorder.y){
                goUp = true;
            }
        }
    }
}

package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import pl.dominik.hinc.bounceback.BounceBack;

public class WallPowerUp extends AbstractPowerUp {
    private Fixture sensor;
    public WallPowerUp(BounceBack context) {
        super(context);
    }

    @Override
    public void spawn(){
        isSpawned = true;
        goUp = MathUtils.randomBoolean();
        BounceBack.BODY_DEF.type = BodyDef.BodyType.KinematicBody;
        BounceBack.BODY_DEF.position.set(spawnPos);
        BounceBack.BODY_DEF.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(boxSize/4,boxSize*2);

        BounceBack.FIXTURE_DEF.shape = shape;
        BounceBack.FIXTURE_DEF.filter.categoryBits = BounceBack.POWERUP_BIT;
        BounceBack.FIXTURE_DEF.restitution = 0.5f;
        BounceBack.FIXTURE_DEF.friction = 0;

        body = context.getWorld().createBody(BounceBack.BODY_DEF);
        fixture = body.createFixture(BounceBack.FIXTURE_DEF);
        //fixture.setUserData(this);

        shape.setAsBox(boxSize/3.6f,boxSize*1.9f);
        BounceBack.FIXTURE_DEF.shape = shape;
        BounceBack.FIXTURE_DEF.isSensor = true;

        sensor = body.createFixture(BounceBack.FIXTURE_DEF);
        sensor.setUserData(this);

        body.setAwake(true);

        shape.dispose();
        context.resetFixtureAndBodyDef();

        context.getGameRenderer().addRenderableEntity(this);

    }
    @Override
    public void render(SpriteBatch spriteBatch) {
        if(body != null) {
            spriteBatch.end();
            ShapeRenderer shapeRenderer = context.getGameRenderer().getShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            Color color = context.getColorManager().currentColor;
            shapeRenderer.setColor(color.r , color.g , color.b , color.a );
            shapeRenderer.rect(body.getPosition().x -boxSize/4,body.getPosition().y-boxSize*2,boxSize/2,boxSize*4);

            shapeRenderer.end();
            spriteBatch.begin();
        }
    }

    @Override
    public void use() {
        context.getBox2DWorldManager().wallTouched();
    }
}

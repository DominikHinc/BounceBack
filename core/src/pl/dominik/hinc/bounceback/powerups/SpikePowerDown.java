package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import pl.dominik.hinc.bounceback.BounceBack;

public class SpikePowerDown extends AbstractPowerUp {
    private float[] vertecies;
    public SpikePowerDown(BounceBack context) {
        super(context);
        boxSize *= 2;
    }


    @Override
    public void render(SpriteBatch spriteBatch) {
        if(body != null) {
            spriteBatch.end();
            ShapeRenderer shapeRenderer = context.getGameRenderer().getShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            Color color = context.getColorManager().currentColor;
            shapeRenderer.setColor(color.r + 0.35f, color.g + 0.35f, color.b + 0.35f, color.a + 0.15f);

            vertecies[0] = body.getPosition().x;
            vertecies[1] = body.getPosition().y + boxSize / 2;
            vertecies[2] = body.getPosition().x + boxSize;
            vertecies[3] = body.getPosition().y;
            vertecies[4] = body.getPosition().x;
            vertecies[5] = body.getPosition().y - boxSize / 2;
            vertecies[6] = body.getPosition().x - boxSize;
            vertecies[7] = body.getPosition().y;
            vertecies[8] = body.getPosition().x;
            vertecies[9] = body.getPosition().y + boxSize / 2;

            shapeRenderer.triangle(vertecies[0],vertecies[1],vertecies[2],vertecies[3],vertecies[4],vertecies[5]);
            shapeRenderer.triangle(vertecies[4],vertecies[5],vertecies[6],vertecies[7],vertecies[8],vertecies[9]);

            shapeRenderer.end();
            spriteBatch.begin();
        }
    }

    @Override
    public void spawn(){
        isSpawned = true;
        goUp = MathUtils.randomBoolean();
        BounceBack.BODY_DEF.type = BodyDef.BodyType.KinematicBody;
        BounceBack.BODY_DEF.position.set(spawnPos);
        BounceBack.BODY_DEF.fixedRotation = true;

        vertecies = new float[]{0,boxSize/2,+boxSize,0,0,-boxSize/2,-boxSize,0,0,boxSize/2};
        ChainShape shape = new ChainShape();
        shape.createChain(vertecies);

        BounceBack.FIXTURE_DEF.shape = shape;
        BounceBack.FIXTURE_DEF.isSensor = true;
        BounceBack.FIXTURE_DEF.filter.categoryBits = BounceBack.POWERUP_BIT;

        body = context.getWorld().createBody(BounceBack.BODY_DEF);
        fixture = body.createFixture(BounceBack.FIXTURE_DEF);
        fixture.setUserData(this);

        body.setAwake(true);

        shape.dispose();
        context.resetFixtureAndBodyDef();

        context.getGameRenderer().addRenderableEntity(this);
    }

    @Override
    public void use() {
        context.getPlayer().setDead(true);
    }

}

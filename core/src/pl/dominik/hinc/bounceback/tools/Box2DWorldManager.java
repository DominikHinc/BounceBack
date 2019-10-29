package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.entities.Player;

public class Box2DWorldManager implements Updatable {
    private BounceBack context;
    private FixtureDef fixtureDef;
    private BodyDef bodyDef;
    private World world;
    private Viewport viewport;
    private Body groundBody;
    private Fixture groundFixture;

    public Box2DWorldManager(BounceBack context){
        this.context = context;
        this.bodyDef = BounceBack.BODY_DEF;
        this.fixtureDef = BounceBack.FIXTURE_DEF;
        this.world = context.getWorld();
        this.viewport = context.getScreenViewport();
    }
    public void createWallSensors(){
        ChainShape shapeA = new ChainShape();

        shapeA.createChain(new float[]{BounceBack.RIGHT_LEDGE-0.01f,BounceBack.TOP_LEDGE,BounceBack.RIGHT_LEDGE-0.01f,BounceBack.BOTTOM_LEDGE});
        BounceBack.BODY_DEF.fixedRotation = true;
        BounceBack.BODY_DEF.type = BodyDef.BodyType.StaticBody;
        //BounceBack.BODY_DEF.position.set(new Vector2(BounceBack.RIGHT_LEDGE,context.getScreenViewport().getWorldHeight()/2));
        //shape.createChain(new float[]{0,context.getScreenViewport().getWorldHeight()/2,0,-context.getScreenViewport().getWorldHeight()/2});
        BounceBack.FIXTURE_DEF.shape = shapeA;
        BounceBack.FIXTURE_DEF.restitution = 0;
        BounceBack.FIXTURE_DEF.isSensor = true;
        world.createBody(BounceBack.BODY_DEF).createFixture(BounceBack.FIXTURE_DEF).setUserData(new Collidable() {
            @Override
            public void handleCollision(Fixture fixture) {
                if(fixture.getUserData() instanceof Player){
                    wallTouched();
                   // Gdx.app.debug("Sesor","Go right " + context.getSpikeCreator().isGoRight());
                }

            }
        });
        ChainShape shapeB = new ChainShape();
        shapeB.createChain(new float[]{BounceBack.LEFT_LEDGE+0.01f,BounceBack.TOP_LEDGE,BounceBack.LEFT_LEDGE+0.01f,BounceBack.BOTTOM_LEDGE});
        BounceBack.FIXTURE_DEF.shape = shapeB;
        world.createBody(BounceBack.BODY_DEF).createFixture(BounceBack.FIXTURE_DEF).setUserData(new Collidable() {
            @Override
            public void handleCollision(Fixture fixture) {
                if(fixture.getUserData() instanceof Player) {
                    wallTouched();
                   // Gdx.app.debug("Sesor", "Go right " + context.getSpikeCreator().isGoRight());
                }
            }
        });
        context.resetFixtureAndBodyDef();
        shapeA.dispose();
        shapeB.dispose();
    }

    public void createMainBox() {
        ChainShape shape = new ChainShape();
        float[] floats = new float[]{BounceBack.RIGHT_LEDGE,BounceBack.TOP_LEDGE ,BounceBack.RIGHT_LEDGE,BounceBack.BOTTOM_LEDGE , BounceBack.LEFT_LEDGE,BounceBack.BOTTOM_LEDGE, BounceBack.LEFT_LEDGE ,BounceBack.TOP_LEDGE ,BounceBack.RIGHT_LEDGE,BounceBack.TOP_LEDGE};
        shape.createChain(floats);

        BounceBack.BODY_DEF.position.set(new Vector2(0,0));
        BounceBack.BODY_DEF.fixedRotation = true;
        BounceBack.BODY_DEF.type = BodyDef.BodyType.StaticBody;

        BounceBack.FIXTURE_DEF.restitution = 0.5f;
        BounceBack.FIXTURE_DEF.shape = shape;
        BounceBack.FIXTURE_DEF.friction = 0;
        BounceBack.FIXTURE_DEF.isSensor = false;
        BounceBack.FIXTURE_DEF.density = 1;
        BounceBack.FIXTURE_DEF.filter.categoryBits = BounceBack.WALL_BIT;
        groundBody = world.createBody(BounceBack.BODY_DEF);
        groundFixture = groundBody.createFixture(BounceBack.FIXTURE_DEF);
        groundFixture.setUserData(new Collidable() {
            @Override
            public void handleCollision(Fixture fixture) {

            }
        });
        shape.dispose();
        context.resetFixtureAndBodyDef();
    }

    public void wallTouched(){
        context.getSpikeCreator().setGoRight(!context.getSpikeCreator().isGoRight());
        context.getSpikeCreator().updateSpikes();
        context.getPowerUpManager().pointAdded();
    }

    @Override
    public void update() {

    }
}

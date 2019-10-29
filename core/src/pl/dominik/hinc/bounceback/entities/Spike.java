package pl.dominik.hinc.bounceback.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.enums.SpikeOrientation;
import pl.dominik.hinc.bounceback.tools.Collidable;
import pl.dominik.hinc.bounceback.tools.RenderableEntity;

public class Spike implements Collidable {
    private int row;
    private ChainShape shape;
    private BounceBack context;
    private Body spikeBody;
    private Fixture spikeFixture;
    private float[] vertecies;
    private boolean toDestroy = false;
    private SpikeOrientation spikeOrientation;
    public float width = 0.43f;
    public float height = 0.25f;

    //15 rows
    public Spike(int row, BounceBack context, SpikeOrientation spikeOrientation){
        this.context = context;
        this.row = row;
        shape = new ChainShape();
        this.spikeOrientation = spikeOrientation;
        switch (spikeOrientation){
            case UP: vertecies = createBottomOrTop(false);break;
            case DOWN: vertecies = createBottomOrTop(true);break;
            case LEFT: vertecies = createLeftOrRight(false);break;
            case RIGHT: vertecies = createLeftOrRight(true);break;
        }
        shape.createChain(vertecies);
        //shape.createChain(new float[]{context.getScreenViewport().getWorldWidth()/2 + 4,row - 0.25f, context.getScreenViewport().getWorldWidth()/2 + 4,row + 0.25f, context.getScreenViewport().getWorldWidth()/2 + 4 - 0.43f,row,context.getScreenViewport().getWorldWidth()/2 + 4,row - 0.25f});
        BounceBack.BODY_DEF.type = BodyDef.BodyType.StaticBody;
        BounceBack.BODY_DEF.linearVelocity.set(new Vector2(0,0));
        BounceBack.FIXTURE_DEF.filter.categoryBits = BounceBack.SPIKE_BIT;
        BounceBack.FIXTURE_DEF.filter.maskBits = BounceBack.PLAYER_BIT | BounceBack.WALL_BIT | BounceBack.OTHER_OBJECT_BIT;
        BounceBack.FIXTURE_DEF.shape = shape;
        BounceBack.FIXTURE_DEF.density = 150;


        spikeBody = context.getWorld().createBody(BounceBack.BODY_DEF);
        spikeFixture = spikeBody.createFixture(BounceBack.FIXTURE_DEF);
        spikeFixture.setUserData(this);
        shape.dispose();
        context.resetFixtureAndBodyDef();
    }

    public void render(ShapeRenderer shapeRenderer) {
        if(toDestroy == false){
            Color color = context.getColorManager().currentColor;
            shapeRenderer.setColor(color.r+0.35f,color.g+0.35f,color.b+0.35f,color.a+0.15f);
            if (context.getSpikeCreator().isMovingSpikes() && context.getPowerUpManager().whilePlusFive == false && context.getPowerUpManager().afterLastPlusfive == false && (spikeOrientation == SpikeOrientation.LEFT || spikeOrientation == SpikeOrientation.RIGHT)){
                    float d = context.getSpikeCreator().getAccumulator();
                    float width = (context.getSpikeCreator().isGoRight()) ? this.width:-this.width;
                    shapeRenderer.triangle(vertecies[0]+d+width,vertecies[1],vertecies[2]+d+width,vertecies[3],vertecies[4]+d+width,vertecies[5]);
            }else{
                shapeRenderer.triangle(vertecies[0],vertecies[1],vertecies[2],vertecies[3],vertecies[4],vertecies[5]);
            }

            //float ledge = (context.getSpikeCreator().isGoRight()) ? BounceBack.RIGHT_LEDGE : BounceBack.LEFT_LEDGE;
           // Vector2 v = spikeBody.getPosition();
           // shapeRenderer.triangle(v.x, v.y - height,v.x,v.y + height,v.x+ width,v.y);
        }


    }
    private float[] createBottomOrTop(boolean bottom){
        float ledge;
        float height = 0.43f;
        float width = 0.25f;
        if(bottom){
            ledge = BounceBack.BOTTOM_LEDGE;
        }else {
            ledge = BounceBack.TOP_LEDGE;
            height = -height;
        }
        return new float[]{row - width,ledge,row + width,ledge, row, ledge + height,row - width,ledge};

    }
    private float[] createLeftOrRight(boolean right){

        float ledge;
        float width = this.width;

        if(right){
            ledge = BounceBack.RIGHT_LEDGE;
            width = -width;
        }else {
            ledge = BounceBack.LEFT_LEDGE;
        }
        return new float[]{ledge,row-height,ledge,row+height,ledge+width,row,ledge,row-height};
    }
    @Override
    public void handleCollision(Fixture fixture) {

    }

    public SpikeOrientation getSpikeOrientation() {
        return spikeOrientation;
    }

    public boolean isToDestroy() {
        return toDestroy;
    }

    public void setToDestroy(boolean toDestroy) {
        this.toDestroy = toDestroy;
    }

    public Body getSpikeBody() {
        return spikeBody;
    }

    public float[] getVertecies() {
        return vertecies;
    }



}

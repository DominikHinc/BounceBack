package pl.dominik.hinc.bounceback.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Transform;

import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.tools.Collidable;
import pl.dominik.hinc.bounceback.tools.RenderableEntity;

public class PlayerRemains implements Collidable, RenderableEntity {
    private Vector2 playerPos;
    private BounceBack context;
    private Body remainBody;
    private Sprite sprite;
    private float i;
    private float j;
    public PlayerRemains(Vector2 playerPos, BounceBack context, float i, float j, float remainConst,float degrees,Texture texture){
        this.context = context;
        this.playerPos = playerPos;
        this.i = i;
        this.j = j;
        context.getGameRenderer().addRenderableEntity(this);
        remainBody = context.getWorld().createBody(BounceBack.BODY_DEF);
        //remainBody.setTransform(remainBody.getPosition(),degrees/MathUtils.degRad);
        remainBody.createFixture(BounceBack.FIXTURE_DEF).setUserData(this);
        int sigoOfLeft = -1;
        if(context.getSpikeCreator().isGoRight()){
            sigoOfLeft = 1;
        }
        sprite = new Sprite(texture,(int)(171 + 171*i/2*10*sigoOfLeft),(int)(171 + 171*j/2*10*-1),126,120);
        if(context.getSpikeCreator().isGoRight() == false){
            sprite.setFlip(true,false);
        }
        sprite.setSize(remainConst,remainConst);
        sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
        sprite.setRotation(degrees);
    }
    @Override
    public void handleCollision(Fixture fixture) {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        sprite.setPosition(remainBody.getPosition().x-sprite.getWidth()/2,remainBody.getPosition().y-sprite.getHeight()/2);
        Transform t = remainBody.getTransform();
        sprite.setRotation(t.getRotation()*MathUtils.radiansToDegrees);
        //Gdx.app.debug("Player Remain:", Float.toString(t.getRotation()*MathUtils.radiansToDegrees));
        sprite.draw(spriteBatch);
    }

    public Body getRemainBody() {
        return remainBody;
    }
}

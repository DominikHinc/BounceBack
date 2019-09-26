package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import pl.dominik.hinc.bounceback.BounceBack;

import static pl.dominik.hinc.bounceback.BounceBack.PLAYER_BIT;

public class WorldContactListener implements ContactListener {
    BounceBack context;
    public WorldContactListener(BounceBack context){
        this.context = context;
    }

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().getUserData() instanceof Collidable && contact.getFixtureB().getUserData() instanceof Collidable){
            Collidable collidableA = (Collidable) contact.getFixtureA().getUserData();
            Collidable collidableB = (Collidable) contact.getFixtureB().getUserData();
            collidableA.handleCollision(contact.getFixtureB());
            collidableB.handleCollision(contact.getFixtureA());
        }


        /*final int catFixA = contact.getFixtureA().getFilterData().categoryBits;
        final int catFixB = contact.getFixtureB().getFilterData().categoryBits;

        if((int)(catFixA & PLAYER_BIT) == PLAYER_BIT){
            Gdx.app.debug("Contact",": Player Body A");
            context.reversePlayer = true;
        }else if ((int)(catFixB & PLAYER_BIT) == PLAYER_BIT){
            Gdx.app.debug("Contact",": Player Body B");
            context.reversePlayer = true;
        }else{
            return;
        }*/

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

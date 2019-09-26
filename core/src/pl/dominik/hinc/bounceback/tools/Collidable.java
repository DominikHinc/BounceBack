package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface Collidable {
    public void handleCollision(Fixture fixture);
}

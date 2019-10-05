package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.physics.box2d.Body;

public interface PowerUp {
    public void use();
    public void spawn();
    public boolean isToDestroy();
    public Body getBody();
    public void move();
}

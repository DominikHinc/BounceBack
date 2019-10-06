package pl.dominik.hinc.bounceback.enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Fixture;

import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.powerups.PowerUp;

public class ClonePowerUp extends AbstractPowerUp {

    public ClonePowerUp(BounceBack context) {
        super(context);
    }

    @Override
    public void use() {
        Gdx.app.debug("PoweUP Spawn","Used Clone");
    }

    @Override
    public void spawn(){
        super.spawn();
        Gdx.app.debug("PowerUp", "Clone");
    }

}

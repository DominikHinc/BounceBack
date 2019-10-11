package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import pl.dominik.hinc.bounceback.BounceBack;

public class PlusFivePowerUp extends AbstractPowerUp {

    public PlusFivePowerUp(BounceBack context) {
        super(context);
        prepareTexture(new Sprite(context.getAssetManager().get("Five.png", Texture.class)));
    }

    @Override
    public void use() {
        Gdx.app.debug("PoweUP Spawn","Used Clone");
        context.getPowerUpManager().usedPlusFive = true;
    }

    @Override
    public void spawn(){
        super.spawn();
        Gdx.app.debug("PowerUp", "Five");
    }


}

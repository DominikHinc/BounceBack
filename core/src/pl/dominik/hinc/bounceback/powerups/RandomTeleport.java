package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import pl.dominik.hinc.bounceback.BounceBack;

public class RandomTeleport extends AbstractPowerUp {
    public RandomTeleport(BounceBack context) {
        super(context);
        prepareTexture(new Sprite(context.getAssetManager().get("PowerUps/RandomTeleport.png", Texture.class)));
    }

    @Override
    public void use() {
        context.getPowerUpManager().toTeleport = true;
        context.getScoreBoardManager().addRandomTeleport();
    }
}

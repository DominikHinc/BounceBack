package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import pl.dominik.hinc.bounceback.BounceBack;

public class ShieldPowerUp extends AbstractPowerUp{

    public ShieldPowerUp(BounceBack context){
        super(context);
        prepareTexture(new Sprite(context.getAssetManager().get("PowerUps/Shield.png", Texture.class)));
        //powerUpSprite.setColor(context.getColorManager().currentColor);
    }


    @Override
    public void use() {
        context.getPlayer().setShielded(true);
        context.getScoreBoardManager().addShields();
        context.getPowerUpManager().playPowerUpGainSound();
    }

}

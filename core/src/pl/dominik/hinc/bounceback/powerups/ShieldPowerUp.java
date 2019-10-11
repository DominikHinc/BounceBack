package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import pl.dominik.hinc.bounceback.BounceBack;

public class ShieldPowerUp extends AbstractPowerUp{

    public ShieldPowerUp(BounceBack context){
        super(context);
        prepareTexture(new Sprite(context.getAssetManager().get("Shield.png", Texture.class)));

    }
    @Override
    public void spawn(){
        super.spawn();
        Gdx.app.debug("PowerUp Spawn", "Shield");
    }



    @Override
    public void use() {
        Gdx.app.debug("PoweUP","Used Shield");
        context.getPlayer().setShielded(true);
    }

}

package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.entities.Player;
import pl.dominik.hinc.bounceback.enums.AbstractPowerUp;
import pl.dominik.hinc.bounceback.tools.Collidable;

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

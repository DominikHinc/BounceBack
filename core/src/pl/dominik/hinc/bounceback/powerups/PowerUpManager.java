package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.enums.ClonePowerUp;
import pl.dominik.hinc.bounceback.enums.PowerUpType;
import pl.dominik.hinc.bounceback.tools.Updatable;

public class PowerUpManager implements Updatable {
    private BounceBack context;
    private float chance = 0;
    private PowerUp powerUp;
    public boolean isSpawned = false;
    private boolean toSpawn = false;
    private Array<PowerUpType> powerUpTypes;
    public PowerUpManager(BounceBack context){
        this.context = context;
        context.addToUpdatableArray(this);
        powerUpTypes = new Array<>();
        for(PowerUpType powerUpType: PowerUpType.values()){
            powerUpTypes.add(powerUpType);
        }

    }

    public void addChance(){
        if(isSpawned == false){
            Gdx.app.debug("PowerUpManager", "chance Added");
            chance += 0.1f;
            if(chance > MathUtils.random(1f)){
                toSpawn = true;
                chance = 0;
            }
        }
    }
    public void spawnPowerUp(){
        Gdx.app.debug("PowerUpManager", "Spawn");
        PowerUpType type = powerUpTypes.get(MathUtils.random(powerUpTypes.size-1));
        switch(type){
            case SHIELD:powerUp = new ShieldPowerUp(context);break;
            case CLONE:powerUp = new ClonePowerUp(context);break;
        }
        powerUp.spawn();
    }

    @Override
    public void update() {
        if(powerUp != null){
            if(powerUp.isToDestroy()){
                context.getWorld().destroyBody(powerUp.getBody());
                powerUp = null;
            }else {
                powerUp.move();
            }
        }
        if (toSpawn){
            spawnPowerUp();
            toSpawn = false;
            isSpawned = true;
        }
    }
}

package pl.dominik.hinc.bounceback.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.enums.PowerUpType;
import pl.dominik.hinc.bounceback.tools.Updatable;

public class PowerUpManager implements Updatable {
    private BounceBack context;
    private float chance = 0;
    private PowerUp powerUp;
    public boolean isSpawned = false;
    private boolean toSpawn = false;
    private Array<PowerUpType> powerUpTypes;
    private int pointsToDelete = 0;
    //Plus Five Power Up
    public boolean usedPlusFive = false;
    public boolean whilePlusFive = false;
    public int pointsToGo = 0;
    //Random Teleport
    public boolean toTeleport = false;


    public PowerUpManager(BounceBack context){
        this.context = context;
        context.addToUpdatableArray(this);
        powerUpTypes = new Array<>();
        for(PowerUpType powerUpType: PowerUpType.values()){
            powerUpTypes.add(powerUpType);
        }

    }

    public void pointAdded(){
        if(isSpawned == false){
            Gdx.app.debug("PowerUpManager", "chance Added");
            chance += 0.1f;
            if(chance > MathUtils.random(1f)){
                toSpawn = true;
                chance = 0;
            }
        }else if(isSpawned){
            pointsToDelete--;
            if (pointsToDelete == 0){
                powerUp.deletePowerUp();
            }
        }
        //checking for plus five
        if(whilePlusFive){
            pointsToGo--;
            if (pointsToGo == 0){
                whilePlusFive = false;
                context.getPlayer().getPlayerBody().setGravityScale(2);
            }else{
                usedPlusFive = true;
            }

        }
    }
    public void spawnPowerUp(){
        Gdx.app.debug("PowerUpManager", "Spawn");
        PowerUpType type = powerUpTypes.get(MathUtils.random(powerUpTypes.size-1));
        switch(type){
            case SHIELD:powerUp = new ShieldPowerUp(context);break;
            case PLUSFIVE:powerUp = new PlusFivePowerUp(context);break;
            case SPIKE:powerUp = new SpikePowerDown(context);break;
            case RANDOMTELEPORT:powerUp = new RandomTeleport(context);break;
        }

        powerUp.spawn();
        pointsToDelete = 3;
    }
    public void reset(){
        if(isSpawned && powerUp != null){
            powerUp.deletePowerUp();
        }
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
        if(usedPlusFive){
            usePlusFive();
            usedPlusFive = false;
        }
        if (whilePlusFive){
            addingFivePoints();
        }
        if (toTeleport){
            teleport();
        }
    }

    //Plus Five Power Up
    private void addingFivePoints(){
        context.getPlayer().getPlayerBody().setLinearVelocity(context.getSpikeCreator().isGoRight() ? 4f:-4f,0);
        context.getPlayer().getPlayerBody().setGravityScale(0);
    }

    private void usePlusFive(){
        int guessedNumber = MathUtils.random(2,14);
        while(context.getSpikeCreator().getCurrentSpikeRows().contains(guessedNumber,false)){
            guessedNumber = MathUtils.random(2,14);
        }
        Gdx.app.debug("PoweUP five",Integer.toString(guessedNumber));
        context.getPlayer().getPlayerBody().setTransform(new Vector2(context.getSpikeCreator().isGoRight() ? 7:2,guessedNumber-context.getPlayer().playerDiameter/2),0);
        if(whilePlusFive == false){
            whilePlusFive = true;
            pointsToGo = 5;
        }

    }
    //Random Teleport
    private void teleport(){
        float randomX = MathUtils.random(0,1f) + (context.getSpikeCreator().isGoRight() ? 5f : 2f);
        float randomY = MathUtils.random(4f,12f);
        context.getPlayer().getPlayerBody().setTransform(new Vector2(randomX,randomY),context.getPlayer().getPlayerBody().getAngle());
        toTeleport = false;
    }
}

package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.entities.PlayerRemains;
import pl.dominik.hinc.bounceback.entities.Spike;
import pl.dominik.hinc.bounceback.enums.ScreenType;
import pl.dominik.hinc.bounceback.enums.SpikeOrientation;


public class SpikeCreator implements Updatable{

    private BounceBack context;
    private boolean goRight = true;
    private Array<Spike> currentSpikes;
    private Array<Spike> topAndBottomSpikes;
    private Array<Integer> currentSpikeRows;
    public boolean timeToUpdateSpikes = false;
    private SpikeOrientation currentSpikeOrientation;
    private boolean updateOneSpike = false;
    private boolean movingSpikes = false;
    private float accumulator = -0.01f;

    public SpikeCreator(BounceBack context){
        this.context = context;
        context.addToUpdatableArray(this);
        currentSpikes = new Array<Spike>();
        topAndBottomSpikes = new Array<>();
        currentSpikeRows = new Array<Integer>();
    }
    public void start(){
        createBottomAndTop();
        updateSpikes();
    }
    public void updateSpikes(){
        timeToUpdateSpikes = true;
        context.addOneScore();
        //Color Change
        if(context.getScore() % 5 == 0 && context.getScore() != 0){
            context.getColorManager().defineNewRandoCurrentColor();
        }
        //UpsideDown View
        if(context.getScore() % 25 == 0 && context.getScore() != 0) {
            //context.getCamera().zoom = -context.getCamera().zoom;
            context.getWorld().setGravity(new Vector2(0, 9.81f));
            //context.getPlayer().getPlayerSprite().setFlip(context.getPlayer().getPlayerSprite().isFlipX(), true);
            context.setReverseSprites(true);
            context.getPlayer().setJumpForce(-context.getPlayer().getJumpForce());
            context.getGameScreen().getGameUI().setRotation(180);
            //for (PlayerRemains pl : context.getPlayer().getPlayerRemainsArray()){
            // pl.getRemainBody().applyLinearImpulse(new Vector2(MathUtils.random(0.3f),MathUtils.random(0.3f)),pl.getRemainBody().getWorldCenter(),true);
            // }
        }
        //Back To normal View
        if (context.getScore() % 25 == 5 && context.getScore() != 0 && context.getScore() !=5){
            //context.getCamera().zoom = -context.getCamera().zoom;
            context.getWorld().setGravity(new Vector2(0,-9.81f));
            //context.getPlayer().getPlayerSprite().setFlip(context.getPlayer().getPlayerSprite().isFlipX(),false);
            context.setReverseSprites(false);
            context.getPlayer().setJumpForce(Math.abs(context.getPlayer().getJumpForce()));
            context.getGameScreen().getGameUI().setRotation(0);
           // for (PlayerRemains pl : context.getPlayer().getPlayerRemainsArray()){
                //pl.getRemainBody().applyLinearImpulse(new Vector2(MathUtils.random(0.3f),MathUtils.random(0.3f)),pl.getRemainBody().getWorldCenter(),true);
           // }
        }
        //Gdx.app.debug("Spike creator",Integer.toString(context.getScore()));
    }
    @Override
    public void update() {
        if(updateOneSpike){
            for(Spike spike: currentSpikes){
                if(spike.isToDestroy()){
                    spike.getSpikeBody().setActive(false);
                }
            }
            updateOneSpike = false;
            context.getPlayer().setShielded(false);
        }
        if (movingSpikes){
            float direction  = (goRight) ? -0.01f:0.01f;
            accumulator += direction;
            for (Spike spike: currentSpikes){
                spike.getSpikeBody().setTransform(spike.getSpikeBody().getPosition().x+direction,spike.getSpikeBody().getPosition().y,0);
            }
            //Gdx.app.debug("Acc",Float.toString(accumulator));
            if (accumulator >= 0.43f && goRight == false|| accumulator <= -0.43f && goRight == true){
                Gdx.app.debug("Acc","=width");
                movingSpikes = false;
                accumulator = 0;
            }

        }
        if(timeToUpdateSpikes){
            timeToUpdateSpikes = false;
            if (movingSpikes){
                movingSpikes = false;
                accumulator = 0;
            }
            //Clear Old Spikes
            if(currentSpikes != null && !currentSpikes.isEmpty()){
                for(Spike spike: currentSpikes){

                    context.getWorld().destroyBody(spike.getSpikeBody());
                }
                currentSpikes.clear();
            }
            currentSpikeRows.clear();
            //Determine direction
            if(goRight){
                currentSpikeOrientation = SpikeOrientation.RIGHT;
            }else{
                currentSpikeOrientation = SpikeOrientation.LEFT;
            }
            //Create First and Last spike
            Spike spike1 = new Spike(1,context,currentSpikeOrientation);
            spike1.getSpikeBody().setTransform(spike1.getSpikeBody().getPosition().x + ((goRight) ? spike1.width : -spike1.width),spike1.getSpikeBody().getPosition().y,0);
            currentSpikes.add(spike1);
            Spike spike15 = new Spike(15,context,currentSpikeOrientation);
            spike15.getSpikeBody().setTransform(spike15.getSpikeBody().getPosition().x + ((goRight) ? spike15.width : -spike15.width),spike15.getSpikeBody().getPosition().y,0);
            currentSpikes.add(spike15);

            //Create Array with random numbers
            int howManySpikes = 0;
            int score = context.getScore();
            if(score < 10){
                howManySpikes = 5;
            }else if(score >= 10 && score < 80){
                howManySpikes = 5 + (score / 10);
            }else {
                howManySpikes = 12;
            }
            int succesfullPuts = 0;
            while(succesfullPuts != howManySpikes){
                int number = MathUtils.random(2,14);
                if(!currentSpikeRows.contains(number,false)){
                    currentSpikeRows.add(number);
                    succesfullPuts++;
                }
            }

            //Create Other Spikes
            for(int i = 2; i < 15; i++){
                if (currentSpikeRows.contains(i,false)){
                    Spike spike = new Spike(i,context,currentSpikeOrientation);
                    currentSpikes.add(spike);
                    movingSpikes = true;
                    accumulator = 0f;
                    spike.getSpikeBody().setTransform(spike.getSpikeBody().getPosition().x + ((goRight) ? spike.width : -spike.width),spike.getSpikeBody().getPosition().y,0);

                }
            }
        }

    }
    private void createBottomAndTop() {
        for (int i = 1; i < 9 ; i++){
            topAndBottomSpikes.add(new Spike(i,context, SpikeOrientation.DOWN));
        }
        for (int i = 1; i < 9 ; i++){
            topAndBottomSpikes.add(new Spike(i,context, SpikeOrientation.UP));
        }
    }

    public Array<Spike> getTopAndBottomSpikes() {
        return topAndBottomSpikes;
    }

    public Array<Spike> getCurrentSpikes() {
        return currentSpikes;
    }

    public boolean isGoRight() {
        return goRight;
    }

    public void setGoRight(boolean goRight) {
        this.goRight = goRight;
    }

    public void setUpdateOneSpike(boolean updateOneSpike) {
        this.updateOneSpike = updateOneSpike;
    }

    public Array<Integer> getCurrentSpikeRows() {
        return currentSpikeRows;
    }

    public float getAccumulator() {
        return accumulator;
    }

    public boolean isMovingSpikes() {
        return movingSpikes;
    }
}

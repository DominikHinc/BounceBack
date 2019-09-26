package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import pl.dominik.hinc.bounceback.BounceBack;
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
        if(context.getScore() % 5 == 0 && context.getScore() != 0){
            context.getColorManager().defineNewRandoCurrentColor();
            //context.getCamera().zoom = -context.getCamera().zoom;

        }
        //Gdx.app.debug("Spike creator",Integer.toString(context.getScore()));
    }
    @Override
    public void update() {
        if(timeToUpdateSpikes){
            timeToUpdateSpikes = false;
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
            currentSpikes.add(spike1);
            Spike spike15 = new Spike(15,context,currentSpikeOrientation);
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




}
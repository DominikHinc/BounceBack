package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import pl.dominik.hinc.bounceback.BounceBack;

public class ColorManager {
    private BounceBack context;
    public Color currentColor;
    private boolean randomColors = true;
    public float red;
    public float green;
    public float blue;
    public float alpha = 1f;

    public ColorManager(BounceBack context){
        this.context = context;
        currentColor = new Color(0.25f,0.25f,0.25f,0.4f);
        if (context.getPreferences().contains(BounceBack.RANDOMCOLOR)){
            randomColors = context.getPreferences().getBoolean(BounceBack.RANDOMCOLOR);
        }
        if(context.getPreferences().contains(BounceBack.COLORRED)){
            red = context.getPreferences().getFloat(BounceBack.COLORRED);
            green = context.getPreferences().getFloat(BounceBack.COLORGREEN);
            blue = context.getPreferences().getFloat(BounceBack.COLORBLUE);
            //colorChanged();
        }
    }

    public void colorChanged(){
        if(randomColors == false){
            currentColor.set(red,green,blue,alpha);
            context.getGameRenderer().changeLightsColor(currentColor);
        }
    }

    public void defineNewRandoCurrentColor(){
        if (randomColors == true){
            currentColor.set(MathUtils.random(0.5f),MathUtils.random(0.5f),MathUtils.random(0.5f),MathUtils.random(0.5f));
            context.getGameRenderer().changeLightsColor(currentColor);
        }
    }

    public void setRandomColors(boolean randomColors) {
        this.randomColors = randomColors;
        /*if (randomColors == true){
            defineNewRandoCurrentColor();
        }else {
            colorChanged();
        }*/
    }

    public boolean isRandomColors() {
        return randomColors;
    }
}

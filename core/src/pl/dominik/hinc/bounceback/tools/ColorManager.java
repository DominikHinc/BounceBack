package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import pl.dominik.hinc.bounceback.BounceBack;

public class ColorManager {
    private BounceBack context;
    public Color currentColor;

    public ColorManager(BounceBack context){
        this.context = context;
        currentColor = new Color(0.25f,0.25f,0.25f,0.4f);
    }

    public void defineNewRandoCurrentColor(){
        currentColor.set(MathUtils.random(0.5f),MathUtils.random(0.5f),MathUtils.random(0.5f),MathUtils.random(0.5f));
    }
}

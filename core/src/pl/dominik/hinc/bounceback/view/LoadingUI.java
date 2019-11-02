package pl.dominik.hinc.bounceback.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.StringBuilder;
import pl.dominik.hinc.bounceback.BounceBack;

public class LoadingUI extends Table {
    BounceBack context;
    private ProgressBar progressBar;
    private Label textLabel;
    private Label tittleLabel;

    public LoadingUI(BounceBack context){
        this.context = context;
        setFillParent(true);
        Color color = context.loadingColor;
        progressBar = new ProgressBar(0,1,0.01f,false,context.getSkin());
        progressBar.setColor(color.r+0.4f,color.g+0.4f,color.b+0.4f,color.a+0.4f);
        textLabel = new Label("Progress: ",context.getSkin(),"title");
        textLabel.setColor(color.r+0.4f,color.g+0.4f,color.b+0.4f,color.a+0.4f);
        textLabel.setWrap(true);
        Colors.put("Current",new Color(color.r+0.5f,color.g+0.5f,color.b+0.5f,color.a+0.5f));
        tittleLabel = new Label("[Current]Bounce\n         Back",context.getSkin(),"logo");

        this.add(tittleLabel).expandY().left().top().row();
        this.add(textLabel).expandX().fillX().bottom().pad(0,25,0,0).row();
        this.add(progressBar).expandX().fillX().bottom().pad(20,25,20,25);
        bottom();
    }

    public void setProgress(float progress){
        progressBar.setValue(progress);



        final StringBuilder stringBuilder = textLabel.getText();
        stringBuilder.setLength(0);
        stringBuilder.append("Progress: ");
        stringBuilder.append((int) (progress*100));
        stringBuilder.append(" %");
        textLabel.invalidateHierarchy();

        if(progress >= 1 && !textLabel.getText().equals("Touch Anywhere To Continue")){
            textLabel.setText("Touch Anywhere To Continue");
        }
    }
}

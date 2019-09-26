package pl.dominik.hinc.bounceback.view;

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

    public LoadingUI(BounceBack context){
        this.context = context;
        setFillParent(true);
        progressBar = new ProgressBar(0,1,0.01f,false,context.getSkin());
        textLabel = new Label("Progress: ",context.getSkin(),"title");
        textLabel.setWrap(true);

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

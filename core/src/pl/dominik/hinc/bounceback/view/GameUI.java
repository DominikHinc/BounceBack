package pl.dominik.hinc.bounceback.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.tools.Updatable;

public class GameUI extends Table implements Updatable {
    private BounceBack context;
    private Label scoreLabel;
    private TextButton startButton;
    private float startButtonPad;
    private Label highScoreLabel;

    public GameUI(final BounceBack context){
        super(context.getSkin());
        setFillParent(true);
        this.context = context;
        context.addToUpdatableArray(this);
        this.setOrigin(BounceBack.SCREEN_WIDTH/2,BounceBack.SCREEN_HEIGHT/2);
        this.setTransform(true);
        startButtonPad = BounceBack.SCREEN_HEIGHT/4;
        scoreLabel = new Label("153",context.getSkin(),"default");
        highScoreLabel = new Label("157",context.getSkin(),"medium");
        startButton = new TextButton("START!",context.getSkin());
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                blank();
                context.setScore(624);
                context.getGameScreen().startGame();
                context.getPlayer().getPlayerBody().setActive(true);
            }
        });
        //this.rotateBy(180);


    }
    public void crateInGameUI(){
        this.add(scoreLabel).expandX();
        top();

    }
    public void createMenuUI(){
        //this.add(startButton).expandX().padTop(BounceBack.SCREEN_HEIGHT - BounceBack.SCREEN_HEIGHT/4);
        top();
        this.add(scoreLabel).expandX();
        row();
        highScoreLabel.setText("[Current]"+"High Score: "+context.getPreferences().getInteger(BounceBack.HIGHSCOREPREFS));
        Gdx.app.debug("GAME UI",Integer.toString(context.getPreferences().getInteger(BounceBack.HIGHSCOREPREFS)));
        top();
        add(highScoreLabel).expandX().padBottom(startButtonPad*1.455f);
        row();
        this.bottom();
        this.add(startButton).padBottom(startButtonPad);

    }
    public void blank(){
        this.clear();
    }

    @Override
    public void update() {
        Color color = context.getColorManager().currentColor;
        Colors.put("Current",new Color(color.r+0.4f,color.g+0.4f,color.b+0.4f,color.a+0.4f));
        scoreLabel.setText("[Current]"+context.getScore());
        if(context.getGameScreen().isInMenu()){
            highScoreLabel.setText("[Current]"+highScoreLabel.getText());
        }
        /*if(context.getScore() % 25 == 0 && context.getScore() != 0){
            this.rotateBy(180);
        }
        if (context.getScore() % 30 == 0 && context.getScore() != 0){
            this.rotateBy(180);
        }*/
    }
}

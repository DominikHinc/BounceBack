package pl.dominik.hinc.bounceback.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
    private Label highScoreLabel;

    private TextButton placeHolder;
    //ScoreBoard
    private TextButton scoreBoardButton;
    private String scoreBoardString;
    private Label scoreBoardLabel;
    private float scoreLabelPad = 125;
    private TextButton goBackButton;

    //Sizes And Pads
    private Vector2 startButtonSize;
    private float startButtonPad;
    private float padBottomButtons = 150;
    private float padBottomLabels = 400;
    private float scoreHeight = 250;

    public GameUI(final BounceBack context){
        super(context.getSkin());
        setFillParent(true);
        this.context = context;
        startButtonSize = new Vector2(300,150);
        context.addToUpdatableArray(this);
        this.setOrigin(BounceBack.SCREEN_WIDTH/2,BounceBack.SCREEN_HEIGHT/2);
        this.setTransform(true);
        startButtonPad = BounceBack.SCREEN_HEIGHT/4;

        scoreLabel = new Label("153",context.getSkin(),"default");
        highScoreLabel = new Label("157",context.getSkin(),"medium");
        scoreBoardLabel = new Label("",context.getSkin(),"button");
        scoreBoardString = new String();

        startButton = new TextButton("START!",context.getSkin(),"start");
        startButton.getLabel().setFontScale(1.5f);
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                blank();
                context.setScore(0);
                context.getGameScreen().startGame();
                context.getPlayer().getPlayerBody().setActive(true);
            }
        });
        scoreBoardButton = new TextButton("ScoreBoard",context.getSkin());
        scoreBoardButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                blank();
                createScoreBoardUI();
            }
        });
        goBackButton = new TextButton("Go Back",context.getSkin());
        goBackButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                blank();
                createMenuUI();
            }
        });
        placeHolder = new TextButton("PlaceHolder",context.getSkin());
        //this.rotateBy(180);


    }
    public void createScoreBoardUI(){
        Color c = context.getColorManager().currentColor;
        float m = 0.2f;
        float g = 0.3f;
        scoreBoardString = "Number of jumps: " + context.getScoreBoardManager().jumps + "\n\n";
        scoreBoardString += "Total points earned: " + context.getScoreBoardManager().points + "\n\n";
        scoreBoardString += "Number of deaths: " + context.getScoreBoardManager().deaths + "\n\n";
        scoreBoardString += "Total number of specials spawned: " + context.getScoreBoardManager().powerUps + "\n\n";
        scoreBoardString += "Shields picked up: " + context.getScoreBoardManager().shields + "\n\n";
        scoreBoardString += "PlusFive picked up: " + context.getScoreBoardManager().plusFive + "\n\n";
        scoreBoardString += "RandomTeleport picked up: " + context.getScoreBoardManager().randomTeleport + "\n\n";
        scoreBoardLabel.setText(scoreBoardString);
        scoreBoardLabel.setColor(c.r+g,c.g+g,c.b+g,c.a+g*2);
        this.add(scoreBoardLabel).expandY().expandX().top().left().padTop(scoreLabelPad).padLeft(scoreLabelPad/2);
        this.row();
        goBackButton.setColor(c.r+m,c.g+m,c.b+m,c.a+m);
        this.add(goBackButton).expandX().top().padBottom(scoreLabelPad);
    }
    public void crateInGameUI(){
        this.add(scoreLabel).expandX().height(scoreHeight);
        top();

    }
    public void createMenuUI(){
        Color c = context.getColorManager().currentColor;
        float m = 0.2f;
        //float g = 0.3f;
        this.add(scoreLabel).expandX().expandY().top().height(scoreHeight);
        row();
        highScoreLabel.setText("[Current]"+"High Score: "+context.getPreferences().getInteger(BounceBack.HIGHSCOREPREFS));
        add(highScoreLabel).expandY().expandX().top().padBottom(padBottomLabels-padBottomButtons);
        row();
        this.bottom();
        startButton.setColor(c.r+m,c.g+m,c.b+m,c.a+m);
        //startButton.getLabel().setColor(c.r+g,c.g+g,c.b+g,c.a+g);
        this.add(startButton).size(startButtonSize.x,startButtonSize.y).expandY().expandX();
        this.row();
        scoreBoardButton.setColor(c.r+m,c.g+m,c.b+m,c.a+m);
        //scoreBoardButton.getLabel().setColor(c.r+g,c.g+g,c.b+g,c.a+g);
        this.add(scoreBoardButton).size(startButtonSize.x/2,startButtonSize.y/4).expandY().expandX().top();
        row();
        placeHolder.setColor(c.r+m,c.g+m,c.b+m,c.a+m);
        //placeHolder.getLabel().setColor(c.r+g,c.g+g,c.b+g,c.a+g);
        this.add(placeHolder).size(startButtonSize.x/2,startButtonSize.y/4).expandY().expandX().top().padBottom(padBottomButtons);


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
        //this.draw
        /*if(context.getScore() % 25 == 0 && context.getScore() != 0){
            this.rotateBy(180);
        }
        if (context.getScore() % 30 == 0 && context.getScore() != 0){
            this.rotateBy(180);
        }*/
    }
}

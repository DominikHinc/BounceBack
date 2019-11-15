package pl.dominik.hinc.bounceback.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.tools.Updatable;

public class GameUI extends Table implements Updatable {

    private BounceBack context;

    private Label scoreLabel;
    private TextButton startButton;
    private Label highScoreLabel;


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

    //Settings
    private boolean inSettings = false;
    private TextButton settingsButton;
    private Slider lightQualitySlider;
    private Slider RSlider;
    private Slider GSlider;
    private Slider BSlider;
    //private Slider ASlider;
    private CheckBox randomColorCheckBox;
    private Label lightQualityLabel;
    private Label randomColorLabel;
    private Label RLabel;
    private Label GLabel;
    private Label BLabel;
    //private Label ALabel;


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
        lightQualityLabel = new Label("Light Quality:  ",context.getSkin(),"button");
        RLabel = new Label("Red:  ",context.getSkin(),"button");
        GLabel = new Label("Green:  ",context.getSkin(),"button");
        BLabel = new Label("Blue:  ",context.getSkin(),"button");
        //ALabel = new Label("Alpha:  ",context.getSkin(),"button");
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
                if(inSettings){
                    inSettings = false;
                    context.getPlayer().preparePlayerTextureWithNewColor();
                    flushPreferences();
                }
            }
        });
        settingsButton = new TextButton("Settings",context.getSkin());
        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                blank();
                createSettingsUI();
                inSettings = true;
            }
        });

        randomColorCheckBox = new CheckBox("Random Colors",context.getSkin());
        if(context.getPreferences().contains(BounceBack.RANDOMCOLOR)){
            randomColorCheckBox.setChecked(context.getPreferences().getBoolean(BounceBack.RANDOMCOLOR));
        }
        randomColorCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getColorManager().setRandomColors(randomColorCheckBox.isChecked());
                if (randomColorCheckBox.isChecked()){
                    context.getColorManager().defineNewRandoCurrentColor();
                    chaneSettingsColor();
                }else{
                    context.getColorManager().colorChanged();
                    chaneSettingsColor();
                }
            }
        });

        float min = 0.1f;
        float max = 0.6f;
        float stepsize = 0.01f;
        lightQualitySlider = new Slider(128,2048,128,false,context.getSkin());
        if(context.getPreferences().contains(BounceBack.LIGHTRAYS)){
            lightQualitySlider.setValue(context.getPreferences().getFloat(BounceBack.LIGHTRAYS));
        }
        lightQualitySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getGameRenderer().changeLightRaysNumber(lightQualitySlider.getValue());
            }
        });
        RSlider = new Slider(min,max,stepsize,false,context.getSkin());
        RSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getColorManager().red = RSlider.getValue();
                context.getColorManager().colorChanged();
                chaneSettingsColor();
            }
        });
        GSlider = new Slider(min,max,stepsize,false,context.getSkin());
        GSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getColorManager().green = GSlider.getValue();
                context.getColorManager().colorChanged();
                chaneSettingsColor();
            }
        });
        BSlider = new Slider(min,max,stepsize,false,context.getSkin());
        BSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getColorManager().blue = BSlider.getValue();
                context.getColorManager().colorChanged();
                chaneSettingsColor();
            }
        });
        if (context.getPreferences().contains(BounceBack.COLORRED)){
            RSlider.setValue(context.getPreferences().getFloat(BounceBack.COLORRED));
            GSlider.setValue(context.getPreferences().getFloat(BounceBack.COLORGREEN));
            BSlider.setValue(context.getPreferences().getFloat(BounceBack.COLORBLUE));
        }


        /*ASlider = new Slider(min,max,stepsize,false,context.getSkin());
        ASlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getColorManager().alpha = ASlider.getValue();
                context.getColorManager().colorChanged();
            }
        });*/
        //this.rotateBy(180);
    }
    private void flushPreferences(){
        context.getPreferences().putFloat(BounceBack.LIGHTRAYS,lightQualitySlider.getValue());
        context.getPreferences().putBoolean(BounceBack.RANDOMCOLOR,randomColorCheckBox.isChecked());
        context.getPreferences().putFloat(BounceBack.COLORRED,RSlider.getValue());
        context.getPreferences().putFloat(BounceBack.COLORGREEN,GSlider.getValue());
        context.getPreferences().putFloat(BounceBack.COLORBLUE,BSlider.getValue());
        context.getPreferences().flush();
    }
    public void chaneSettingsColor(){
        Color c = new Color(context.getColorManager().currentColor);
        float g = 0.3f;
        float m = 0.2f;
        c.add(g,g,g,2*g);
        lightQualityLabel.setColor(c);
        randomColorCheckBox.getLabel().setColor(c);
        randomColorCheckBox.getImage().setColor(c);
        RLabel.setColor(c);
        GLabel.setColor(c);
        BLabel.setColor(c);
        //ALabel.setColor(c);
        lightQualitySlider.setColor(c);
        RSlider.setColor(c);
        GSlider.setColor(c);
        BSlider.setColor(c);
        //ASlider.setColor(c);
        c = context.getColorManager().currentColor;
        goBackButton.setColor(c.r+m,c.g+m,c.b+m,c.a+m);
    }

    public void createSettingsUI(){
        chaneSettingsColor();
        add(lightQualityLabel).expandX().expandY().right().padTop(150);
        add(lightQualitySlider).expandX().expandY().left().padTop(150);
        row();
        add(randomColorCheckBox).expandX().expandY().center().colspan(2);
        row();
        add(RLabel).expandX().right();
        add(RSlider).expandX().left().expandY();
        row();
        add(GLabel).expandX().right();
        add(GSlider).expandX().left().expandY();
        row();
        add(BLabel).expandX().right().padBottom(375);
        add(BSlider).expandX().left().expandY().padBottom(375);
        row();
        /*add(ALabel).expandX().right().expandY();
        add(ASlider).expandX().left();
        row();*/
        this.add(goBackButton).expandX().top().center().padBottom(scoreLabelPad).colspan(2);
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
        scoreLabel.setText(11);
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
        settingsButton.setColor(c.r+m,c.g+m,c.b+m,c.a+m);
        //settingsButton.getLabel().setColor(c.r+g,c.g+g,c.b+g,c.a+g);
        this.add(settingsButton).size(startButtonSize.x/2,startButtonSize.y/4).expandY().expandX().top().padBottom(padBottomButtons);


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

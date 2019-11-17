package pl.dominik.hinc.bounceback.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.enums.ScreenType;
import pl.dominik.hinc.bounceback.tools.InputListener;
import pl.dominik.hinc.bounceback.view.LoadingUI;

public class LoadingScreen extends AbstractScreen<LoadingUI> implements InputListener {
    Label label;

    public LoadingScreen(BounceBack context) {
        super(context);
        label = new Label("Game made by: Dominik Hinc",context.getSkin(),"button");
        label.setPosition(20,BounceBack.SCREEN_HEIGHT-label.getHeight());
        Color color = context.loadingColor;
        label.setColor(color.r+0.5f,color.g+0.5f,color.b+0.5f,color.a+0.5f);
        stage.addActor(label);
        load();
    }

    private void load() {
        context.getInputManager().addListener(this);
        //Textures
        context.getAssetManager().load("Player/BIRT.png", Texture.class);
        context.getAssetManager().load("PowerUps/Shield.png",Texture.class);
        context.getAssetManager().load("PowerUps/Five.png",Texture.class);
        context.getAssetManager().load("PowerUps/RandomTeleport.png",Texture.class);
        context.getAssetManager().load("Audio/JumpSound.wav", Sound.class);
        context.getAssetManager().load("Audio/DeathSound.wav",Sound.class);
        context.getAssetManager().load("Audio/PowerUpSpawn.wav",Sound.class);
        context.getAssetManager().load("Audio/PowerUpPickUp.wav",Sound.class);
        context.getAssetManager().load("Audio/ShieldShatter.wav",Sound.class);
        context.getAssetManager().load("Audio/WallHit.wav",Sound.class);
        context.getAssetManager().load("Audio/PowerUpGone.wav",Sound.class);
        context.getAssetManager().load("Audio/ButtonClick.wav",Sound.class);
        context.getParticleManager().loadParticles();
    }

    @Override
    protected LoadingUI getScreenUI(BounceBack context) {
        return new LoadingUI(context);
    }

    @Override
    public void render(float delta) {
        Color c = context.loadingColor;
        Gdx.gl.glClearColor(c.r,c.g,c.b,c.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        context.getAssetManager().update();
        screenUI.setProgress(context.getAssetManager().getProgress());
    }

    @Override
    public void dispose() {
    }

    @Override
    public void touchDown(int screenX, int screenY, int pointer, int button) {
        if(context.getAssetManager().getProgress() == 1){
            stage.getRoot().removeActor(label);
            context.getInputManager().destroyListener(this);
            context.setCurrentBirdTexture(context.getAssetManager().get("Player/BIRT.png", Texture.class));
            if (context.getColorManager().isRandomColors() == true){
                context.getColorManager().currentColor = new Color(context.loadingColor);
            }
            if (context.isMute() == false){
                context.getAssetManager().get("Audio/ButtonClick.wav",Sound.class).play(context.getVolume());
            }
            context.loadingColor = null;
            context.setScreen(ScreenType.GAME);
        }
    }
}

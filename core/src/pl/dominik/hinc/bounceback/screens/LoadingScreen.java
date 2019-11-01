package pl.dominik.hinc.bounceback.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.enums.ScreenType;
import pl.dominik.hinc.bounceback.tools.InputListener;
import pl.dominik.hinc.bounceback.view.LoadingUI;

public class LoadingScreen extends AbstractScreen<LoadingUI> implements InputListener {

    public LoadingScreen(BounceBack context) {
        super(context);
        load();
    }

    private void load() {
        context.getInputManager().addListener(this);
        //Textures
        context.getAssetManager().load("Player/BIRT.png", Texture.class);
        context.getAssetManager().load("PowerUps/Shield.png",Texture.class);
        context.getAssetManager().load("PowerUps/Five.png",Texture.class);
        context.getAssetManager().load("PowerUps/RandomTeleport.png",Texture.class);
        context.getParticleManager().loadParticles();
    }

    @Override
    protected LoadingUI getScreenUI(BounceBack context) {
        return new LoadingUI(context);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
            context.getInputManager().destroyListener(this);
            context.setCurrentBirdTexture(context.getAssetManager().get("Player/BIRT.png", Texture.class));
            context.setScreen(ScreenType.GAME);
        }
    }
}

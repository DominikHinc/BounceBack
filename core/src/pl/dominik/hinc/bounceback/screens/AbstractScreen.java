package pl.dominik.hinc.bounceback.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.RayHandler;
import pl.dominik.hinc.bounceback.BounceBack;

public abstract class AbstractScreen<T extends Table> implements Screen {
    protected final BounceBack context;
    protected SpriteBatch spriteBatch;
    protected Viewport viewport;
    protected T screenUI;
    protected Stage stage;
    protected World world;
    protected RayHandler rayHandler;
    protected Box2DDebugRenderer box2DDebugRenderer;

    public AbstractScreen(BounceBack context){
        this.context = context;
        this.spriteBatch = context.getBatch();
        this.viewport = context.getScreenViewport();
        this.stage = context.getStage();
        this.screenUI = getScreenUI(context);
        this.box2DDebugRenderer = context.getB2dDebugRenderer();
        this.world = context.getWorld();
        this.rayHandler = context.getRayHandler();
    }

    protected abstract T getScreenUI(BounceBack context);


    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
        stage.getViewport().update(width,height,true);
        rayHandler.useCustomViewport(viewport.getScreenX(),viewport.getScreenY(),viewport.getScreenWidth(),viewport.getScreenHeight());
    }

    @Override
    public void show() {
        stage.addActor(screenUI);
    }

    @Override
    public void hide() {
        stage.getRoot().removeActor(screenUI);
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}

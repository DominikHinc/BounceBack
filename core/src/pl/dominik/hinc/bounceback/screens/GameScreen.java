package pl.dominik.hinc.bounceback.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.view.GameUI;

public class GameScreen extends AbstractScreen {

    private SpriteBatch spriteBatch;
    private GameUI gameUI;
    private boolean inMenu;

    public GameScreen(BounceBack context) {
        super(context);
        context.setGameScreen(this);
        spriteBatch = context.getBatch();
        context.inGame = true;
        createStuff();
    }
    private void createStuff(){
        context.getBox2DWorldManager().createMainBox();
        context.getBox2DWorldManager().createWallSensors();
        context.getPlayer().createPlayerBody();
        context.addToUpdatableArray(context.getPlayer());
        context.getSpikeCreator().start();
        gameUI = (GameUI) screenUI;
        setInMenu();
        //context.getStage().addActor(getScreenUI(context));

    }

    public void setInMenu(){
        //context.setPaused(true);
        inMenu = true;
        gameUI.blank();
        gameUI.createMenuUI();

    }
    public void startGame(){
        Gdx.app.debug("XD","XD");
        gameUI.crateInGameUI();
        inMenu = false;
        //context.setPaused(false);
    }
    @Override
    protected Table getScreenUI(BounceBack context) {
        return new GameUI(context);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update();
        viewport.apply(false);
        viewport.getCamera().update();
        if(context.getPlayer().isDead()){
            setInMenu();
        }
        //spriteBatch.setProjectionMatrix(context.getCamera().combined);
        //spriteBatch.begin();
        //context.getGameRenderer().render();
        //spriteBatch.end();
        //box2DDebugRenderer.render(world,viewport.getCamera().combined);

    }
    public void update(){
        /*if(context.getUpdatableArray() != null){
            for (Updatable updatable:context.getUpdatableArray()){
                updatable.update();
            }
        }*/
    }
    @Override
    public void dispose() {

    }

    public boolean isInMenu() {
        return inMenu;
    }

    public void setInMenu(boolean inMenu) {
        this.inMenu = inMenu;
    }
}

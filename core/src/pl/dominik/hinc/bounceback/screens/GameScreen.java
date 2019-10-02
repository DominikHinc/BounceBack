package pl.dominik.hinc.bounceback.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import box2dLight.PointLight;
import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.view.GameUI;

public class GameScreen extends AbstractScreen {

    private SpriteBatch spriteBatch;
    private GameUI gameUI;
    private boolean inMenu;

    //TEST
    private Body body;
    private Fixture fixture;

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
        //TEST
        //createDebug();
        //context.getStage().addActor(getScreenUI(context));

    }

    private void createDebug() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(250f,250f);
        //CircleShape shape = new CircleShape();
        //shape.setRadius(playerDiameter /2);

        //Player Body
        BounceBack.BODY_DEF.position.set(new Vector2(context.getScreenViewport().getWorldWidth()/2,context.getScreenViewport().getWorldHeight()/2));
        BounceBack.BODY_DEF.fixedRotation = true;
        BounceBack.BODY_DEF.gravityScale = 2;
        BounceBack.BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        BounceBack.BODY_DEF.linearVelocity.set(500,300);
        //Player Fixture
        BounceBack.FIXTURE_DEF.shape =shape;
        BounceBack.FIXTURE_DEF.filter.categoryBits = BounceBack.PLAYER_BIT;
        //Create Body And Fixture
        body = context.getWorld().createBody(BounceBack.BODY_DEF);
        fixture = body.createFixture(BounceBack.FIXTURE_DEF);
        int rays = 2048;
        int distance = 1200;
        Color color = new Color(1f,1f,1f,1);
        PointLight light2 = new PointLight(context.getRayHandler(),rays,color,distance,context.getScreenViewport().getWorldWidth()/4,context.getScreenViewport().getWorldHeight()/4);
        PointLight light1 = new PointLight(context.getRayHandler(),rays,color,distance,context.getScreenViewport().getWorldWidth()-context.getScreenViewport().getWorldWidth()/4,context.getScreenViewport().getWorldHeight()- context.getScreenViewport().getWorldHeight()/4);
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
            if (context.getScore() > context.getPreferences().getInteger(BounceBack.HIGHSCOREPREFS)){
                context.getPreferences().putInteger(BounceBack.HIGHSCOREPREFS,context.getScore());
                context.getPreferences().flush();
            }
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

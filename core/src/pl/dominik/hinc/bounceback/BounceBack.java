package pl.dominik.hinc.bounceback;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.RayHandler;
import pl.dominik.hinc.bounceback.entities.Player;
import pl.dominik.hinc.bounceback.enums.ScreenType;
import pl.dominik.hinc.bounceback.powerups.PowerUpManager;
import pl.dominik.hinc.bounceback.screens.AbstractScreen;
import pl.dominik.hinc.bounceback.screens.GameScreen;
import pl.dominik.hinc.bounceback.tools.*;
import pl.dominik.hinc.bounceback.view.GameRenderer;

import java.util.EnumMap;

public class BounceBack extends Game {
	private static final String TAG = BounceBack.class.getSimpleName();
	public static final int SCREEN_WIDTH = 540;
	public static final int SCREEN_HEIGHT = 960;
	public static  float TIME_STEP = 1/60f;
	public static final float PPM = 1/60f;
	//MASK BITS
	public static final short PLAYER_BIT = 1 << 0;
	public static final short WALL_BIT = 1 << 1;
	public static final short SPIKE_BIT = 1 << 2;
	public static final short OTHER_OBJECT_BIT = 1 << 3;
	public static final short POWERUP_BIT = 1 << 4;
    public static final short LIGHT_BIT = 1 << 5;

	public static float TOP_LEDGE;
	public static float LEFT_LEDGE;
	public static float BOTTOM_LEDGE;
	public static float RIGHT_LEDGE;

	public static final BodyDef BODY_DEF = new BodyDef();
	public static final FixtureDef FIXTURE_DEF = new FixtureDef();
	private float accumulator;
	private int score = -1;
	private SpriteBatch batch;
	private EnumMap<ScreenType, AbstractScreen> screenCashe;
	private OrthographicCamera camera;
	private Viewport screenViewport;
	private Stage stage;
	private Skin skin;

	private World world;
	private WorldContactListener worldContactListener;
	private Box2DDebugRenderer b2dDebugRenderer;
	private Box2DWorldManager box2DWorldManager;
	private RayHandler rayHandler;

	private Player player;
	private Array<Updatable> updatableArray;
	private SpikeCreator spikeCreator;
	private InputManager inputManager;
	private InputMultiplexer inputMultiplexer;
	private GameRenderer gameRenderer;
	//public boolean reversePlayer = false;
	public boolean inGame = false;
	private AssetManager assetManager;
	private ColorManager colorManager;
	private boolean paused = false;
	private Texture currentBirdTexture;
	private boolean reverseSprites = false;
	private boolean isInGame = false;
	private GameScreen gameScreen;
	//Preferences
	private Preferences preferences;
	public final static String HIGHSCOREPREFS = "HIGH_SCORE_SAVE";

	private PowerUpManager powerUpManager;

	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.gl.glEnable(GL20.GL_DITHER);
		batch = new SpriteBatch();
		//Camera And Viewport
		camera = new OrthographicCamera();
		screenViewport = new FitViewport(9,16,camera);
		//screenViewport = new StretchViewport(9,16,camera);
		//camera.zoom = -25;
		initStatics();
		//InputManager
		inputMultiplexer = new InputMultiplexer();
		inputManager = new InputManager();
		inputMultiplexer.addProcessor(inputManager);
		//Gdx.input.setInputProcessor(inputManager);
		//Updates
		updatableArray = new Array<Updatable>();
		//Box2D
		Box2D.init();
		world = new World(new Vector2(0,-9.81f),true);
		b2dDebugRenderer = new Box2DDebugRenderer();
		worldContactListener = new WorldContactListener(this);
		world.setContactListener(worldContactListener);
		box2DWorldManager = new Box2DWorldManager(this);
        spikeCreator = new SpikeCreator(this);
		RayHandler.useDiffuseLight(true);
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.1f,0.1f,0.1f,0.2f);
        rayHandler.setCulling(true);
		//Stage and scene2d Stuff
		stage = new Stage(new FitViewport(SCREEN_WIDTH,SCREEN_HEIGHT),batch);
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		//AssetManeger and skin
		assetManager = new AssetManager();
		prepareSkin();
		//Colors
		colorManager = new ColorManager(this);
		//Entities
		player = new Player(this);
		//GameRenderer
		gameRenderer = new GameRenderer(this);
		//Preferences
		preferences = Gdx.app.getPreferences("BounceBack Save");
		//PowerUps
		powerUpManager = new PowerUpManager(this);
		//Screen Related Code
		screenCashe = new EnumMap<ScreenType, AbstractScreen>(ScreenType.class);
		setScreen(ScreenType.LOADING);


	}

	private void initStatics() {
		TOP_LEDGE = screenViewport.getWorldHeight()/2+7.5f;
		RIGHT_LEDGE = screenViewport.getWorldWidth()/2 + 4;
		LEFT_LEDGE = screenViewport.getWorldWidth()/2 -4;
		BOTTOM_LEDGE = screenViewport.getWorldHeight()/2 -7.5f;
	}


	@Override
	public void render () {
		super.render();
		//Update Everything Updatable
		if(updatableArray != null){
			for (Updatable updatable:updatableArray){
				updatable.update();
			}
		}
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Step World
		if(!paused){
			final float deltaTime = Math.min(0.25f,Gdx.graphics.getRawDeltaTime());
			accumulator += deltaTime;
			while (accumulator >= TIME_STEP){
				world.step(TIME_STEP,6,2);
				accumulator -= TIME_STEP;
			}
		}

		//Render
		batch.setProjectionMatrix(camera.combined);
		if(inGame){
			gameRenderer.render();
		}

		//Stage draw and Act
		batch.setProjectionMatrix(stage.getCamera().combined);
		stage.getViewport().apply();
		stage.act();
		stage.draw();
		//b2dDebugRenderer.render(world,screenViewport.getCamera().combined);


	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		stage.dispose();
		world.dispose();
		b2dDebugRenderer.dispose();
		assetManager.dispose();
		skin.dispose();
		currentBirdTexture.dispose();
		rayHandler.dispose();
	}
	public void resetFixtureAndBodyDef(){
		BODY_DEF.position.set(new Vector2(0,0));
		BODY_DEF.gravityScale = 1;
		BODY_DEF.type = BodyDef.BodyType.StaticBody;
		BODY_DEF.fixedRotation = false;
		BODY_DEF.angle = 0;
		BODY_DEF.angularVelocity = 0;

		FIXTURE_DEF.density = 1;
		FIXTURE_DEF.filter.categoryBits = 0x0001;
		FIXTURE_DEF.filter.maskBits = -1;
		FIXTURE_DEF.shape = null;
		FIXTURE_DEF.friction = 0.2f;
		FIXTURE_DEF.isSensor = false;
		FIXTURE_DEF.restitution = 0;

	}
	private void prepareSkin() {
		Colors.put("Red", Color.RED);
		Colors.put("Blue", Color.BLUE);

		//Generate TTF
		final ObjectMap<String,Object> resources = new ObjectMap<String, Object>();
		final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Skin/Gisbon.ttf"));
		final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.minFilter = Texture.TextureFilter.Linear;
		fontParameter.magFilter = Texture.TextureFilter.Linear;
		fontParameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + ":";
		final int[] sizesFont = {32,100,128,148,169};
		for (int size:sizesFont){
			fontParameter.size = size;
			final BitmapFont bitmapFont = fontGenerator.generateFont(fontParameter);
			bitmapFont.getData().markupEnabled = true;
			resources.put("font_" + size,bitmapFont);
		}
		fontGenerator.dispose();

		//load Skin
		final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("Skin/flat-earth-ui.atlas",resources);
		assetManager.load("Skin/flat-earth-ui.json",Skin.class,skinParameter);
		assetManager.finishLoading();
		skin = assetManager.get("Skin/flat-earth-ui.json", Skin.class);
	}

	public void setScreen(ScreenType screenType) {
		Screen screen = screenCashe.get(screenType);
		if(screen == null){
			//Set new Screen
			AbstractScreen newScreen = null;
			try {
				Gdx.app.debug(TAG,"Creating New Screen "+screenType);
				newScreen = (AbstractScreen) ClassReflection.getConstructor(screenType.getScreenClass(), BounceBack.class).newInstance(this);
				screenCashe.put(screenType, newScreen);
				setScreen(newScreen);
			} catch (ReflectionException e) {
				throw new GdxRuntimeException("Screen " + screenType + "could not be created", e);
			}
		}else {
			//Switch to existing screen
			Gdx.app.debug(TAG, "Switching to screen:" + screenType);
			setScreen(screen);
		}
	}

	public boolean isReverseSprites() {
		return reverseSprites;
	}

	public void setReverseSprites(boolean reverseSprites) {
		this.reverseSprites = reverseSprites;
	}

	public PowerUpManager getPowerUpManager() {
		return powerUpManager;
	}

	public Preferences getPreferences() {
		return preferences;
	}

	public RayHandler getRayHandler() {
        return rayHandler;
    }

    public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public boolean isInGame() {
		return isInGame;
	}

	public void setInGame(boolean inGame) {
		isInGame = inGame;
	}

	public Texture getCurrentBirdTexture() {
		return currentBirdTexture;
	}

	public void setCurrentBirdTexture(Texture currentBirdTexture) {
		this.currentBirdTexture = currentBirdTexture;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	public ColorManager getColorManager() {
		return colorManager;
	}

	public Array<Updatable> getUpdatableArray() {
		return updatableArray;
	}

	public GameRenderer getGameRenderer() {
		return gameRenderer;
	}

	public InputManager getInputManager() {
		return inputManager;
	}
	public void addToUpdatableArray(Updatable updatable){
		updatableArray.add(updatable);
	}
	public Player getPlayer() {
		return player;
	}

	public Box2DWorldManager getBox2DWorldManager() {
		return box2DWorldManager;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Viewport getScreenViewport() {
		return screenViewport;
	}

	public Stage getStage() {
		return stage;
	}

	public Skin getSkin() {
		return skin;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public World getWorld() {
		return world;
	}

	public Box2DDebugRenderer getB2dDebugRenderer() {
		return b2dDebugRenderer;
	}

	public SpikeCreator getSpikeCreator() {
		return spikeCreator;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void addOneScore(){
		this.score++;
	}
}

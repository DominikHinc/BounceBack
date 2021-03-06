package pl.dominik.hinc.bounceback.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.entities.Spike;
import pl.dominik.hinc.bounceback.tools.RenderableEntity;

public class GameRenderer {
    private BounceBack context;
    private Array<RenderableEntity> renderableEntities;
    private Array<Spike> spikes;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private RayHandler rayHandler;
    private Label fpsLabel;
    //Light
    private Array<Light> lightArray;
    private PointLight playerLight;
    int rays = 512;
    int distance = 5;
    //Particles


    public GameRenderer(BounceBack context){
        this.context = context;
        this.spriteBatch = context.getBatch();
        renderableEntities = new Array<>();
        spikes = new Array<>();
        shapeRenderer = new ShapeRenderer();
        if(context.getPreferences().contains(BounceBack.LIGHTRAYS)){
            rays = (int)context.getPreferences().getFloat(BounceBack.LIGHTRAYS);
        }
        rayHandler = context.getRayHandler();
        createLights();

        //FPS DEBUG
        //fpsLabel = new Label("xd",context.getSkin());
        //context.getStage().addActor(fpsLabel);
    }
    public void createLights(){
        lightArray = new Array<>();

        Color color = new Color(0.6f,0.6f,0.6f,1);
        PointLight light1 = new PointLight(context.getRayHandler(),rays,color,distance,2,2);
        PointLight light2 = new PointLight(context.getRayHandler(),rays,color,distance,2,14);
        PointLight light3 = new PointLight(context.getRayHandler(),rays,color,distance,7,2);
        PointLight light4 = new PointLight(context.getRayHandler(),rays,color,distance,7,14);
        PointLight light5 = new PointLight(context.getRayHandler(),rays,color,distance+3,4.5f,8);
        short maskBit = BounceBack.PLAYER_BIT | BounceBack.OTHER_OBJECT_BIT |BounceBack.SPIKE_BIT | BounceBack.WALL_BIT;
        light5.setContactFilter(BounceBack.LIGHT_BIT,(short)1,maskBit);
        lightArray.add(light1);
        lightArray.add(light2);
        lightArray.add(light3);
        lightArray.add(light4);
        lightArray.add(light5);
        for(Light light:lightArray){
            light.setSoftnessLength(1);
            light.setSoft(true);
        }
    }
    public void createPlayerLight(){
        //Player Light
        playerLight = new PointLight(context.getRayHandler(),rays,new Color(0.5f,0.5f,0.5f,1f),distance+1,context.getPlayer().getPlayerBody().getPosition().x,context.getPlayer().getPlayerBody().getPosition().y);
        playerLight.attachToBody(context.getPlayer().getPlayerBody());
    }

    public void addRenderableEntity(RenderableEntity renderableEntity){
        renderableEntities.add(renderableEntity);
    }
    public void destroyRenderableEntity(RenderableEntity renderableEntity){
        if(renderableEntities.contains(renderableEntity,true)){
            renderableEntities.removeValue(renderableEntity,true);
        }
    }

    public void render(){
        //FPS
        //fpsLabel.setText(Gdx.graphics.getFramesPerSecond());
        //pointAdded();
        shapeRenderer.setProjectionMatrix(context.getScreenViewport().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderForeground();
        for (Spike spike: context.getSpikeCreator().getCurrentSpikes()){
            spike.render(shapeRenderer);
        }
        for (Spike spike: context.getSpikeCreator().getTopAndBottomSpikes()){
            spike.render(shapeRenderer);
        }
        renderBackGround();

        //shapeRenderer.end();
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        //roundedRect(2,2,1,1,0.2f);
        shapeRenderer.end();
        if(context.getPlayer().isShielded()){
            Gdx.gl20.glLineWidth(4);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            renderShield();
            shapeRenderer.end();
            Gdx.gl20.glLineWidth(1);
        }
        spriteBatch.begin();
        for(RenderableEntity renderableEntity: renderableEntities){
            if(renderableEntity != null){
                renderableEntity.render(spriteBatch);
            }
        }
        spriteBatch.end();
        rayHandler.setCombinedMatrix(context.getCamera());
        rayHandler.updateAndRender();
    }

    private void renderShield() {
        shapeRenderer.setColor(0f,1f,1f,0.65f);
        shapeRenderer.circle(context.getPlayer().getPlayerBody().getPosition().x,context.getPlayer().getPlayerBody().getPosition().y,context.getPlayer().playerDiameter+context.getPlayer().texturePlusSize,50);

    }

    /*public void roundedRect(float x, float y, float width, float height, float radius) {
        // Central rectangle
        shapeRenderer.rect(x + radius, y + radius, width - 2 * radius, height - 2 * radius);

        // Four side rectangles, in clockwise order
        shapeRenderer.rect(x + radius, y, width - 2 * radius, radius);
        shapeRenderer.rect(x + width - radius, y + radius, radius, height - 2 * radius);
        shapeRenderer.rect(x + radius, y + height - radius, width - 2 * radius, radius);
        shapeRenderer.rect(x, y + radius, radius, height - 2 * radius);

        // Four arches, clockwise too
        shapeRenderer.arc(x + radius, y + radius, radius, 180f, 90f);
        shapeRenderer.arc(x + width - radius, y + radius, radius, 270f, 90f);
        shapeRenderer.arc(x + width - radius, y + height - radius, radius, 0f, 90f);
        shapeRenderer.arc(x + radius, y + height - radius, radius, 90f, 90f);
    }*/
    private void update(){
        if (context.getScore() % 25 == 0){
            context.getCamera().zoom = -context.getCamera().zoom;
        }
        if (context.getScore() % 30 == 0){
            context.getCamera().zoom = -context.getCamera().zoom;
        }
    }
    public void changeLightsColor(Color color){
        for(Light light:lightArray){
            light.setColor(color.r+0.25f,color.g+0.25f,color.b+0.25f,1);
        }
        //playerLight.setColor(color.r+0.15f,color.g+0.15f,color.b+0.15f,1);
    }
    public void changeLightRaysNumber(float rays){
        this.rays = (int)rays;
        for (Light light: lightArray){
            light.remove(true);
            light = null;
        }
        lightArray.clear();
        playerLight.remove(true);
        playerLight = null;
        createLights();
        createPlayerLight();
    }

        private void renderForeground() {
        Color color = context.getColorManager().currentColor;
        shapeRenderer.setColor(color.r+0.15f,color.g+0.15f,color.b+0.15f,color.a+0.15f);
        shapeRenderer.rect(BounceBack.LEFT_LEDGE,BounceBack.BOTTOM_LEDGE,BounceBack.RIGHT_LEDGE-BounceBack.LEFT_LEDGE,BounceBack.TOP_LEDGE-BounceBack.BOTTOM_LEDGE);
    }

    private void renderBackGround() {
        shapeRenderer.setColor(context.getColorManager().currentColor);
        shapeRenderer.rect(0,0,BounceBack.LEFT_LEDGE,BounceBack.TOP_LEDGE);
        shapeRenderer.rect(0,0,9,BounceBack.BOTTOM_LEDGE);
        shapeRenderer.rect(BounceBack.RIGHT_LEDGE,BounceBack.BOTTOM_LEDGE,BounceBack.LEFT_LEDGE,16-BounceBack.BOTTOM_LEDGE);
        shapeRenderer.rect(0,BounceBack.TOP_LEDGE,9,BounceBack.BOTTOM_LEDGE);
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }
}

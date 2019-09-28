package pl.dominik.hinc.bounceback.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
    private Array<Light> lightArray;
    public GameRenderer(BounceBack context){
        this.context = context;
        this.spriteBatch = context.getBatch();
        renderableEntities = new Array<>();
        spikes = new Array<>();
        shapeRenderer = new ShapeRenderer();
        rayHandler = context.getRayHandler();
        createLights();
        //FPS DEBUG
        //fpsLabel = new Label("xd",context.getSkin());
        //context.getStage().addActor(fpsLabel);
    }
    public void createLights(){
        lightArray = new Array<>();
        int rays = 2048;
        int distance = 8;
        Color color = new Color(0.5f,0.5f,0.5f,1);
        PointLight light1 = new PointLight(context.getRayHandler(),rays,color,distance,3,3);
        PointLight light2 = new PointLight(context.getRayHandler(),rays,color,distance,3,13);
        PointLight light3 = new PointLight(context.getRayHandler(),rays,color,distance,6,3);
        PointLight light4 = new PointLight(context.getRayHandler(),rays,color,distance,6,13);
        lightArray.add(light1);
        lightArray.add(light2);
        lightArray.add(light3);
        lightArray.add(light4);
        for(Light light:lightArray){
            light.setSoftnessLength(1);
            light.setSoft(true);

        }
        //light = new PointLight(context.getRayHandler(),2048,new Color(0,0,0,0.75f),8,4.5f,8);
        //light.setSoft(true);

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
        shapeRenderer.setProjectionMatrix(context.getScreenViewport().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        renderBackGround();
        renderForeground();
        //shapeRenderer.end();
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Spike spike: context.getSpikeCreator().getCurrentSpikes()){
            spike.render(shapeRenderer);
        }
        for (Spike spike: context.getSpikeCreator().getTopAndBottomSpikes()){
            spike.render(shapeRenderer);
        }
        //roundedRect(2,2,1,1,0.2f);
        shapeRenderer.end();
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

        private void renderForeground() {
        Color color = context.getColorManager().currentColor;
        shapeRenderer.setColor(color.r+0.15f,color.g+0.15f,color.b+0.15f,color.a+0.15f);
        shapeRenderer.rect(BounceBack.LEFT_LEDGE,BounceBack.BOTTOM_LEDGE,BounceBack.RIGHT_LEDGE-BounceBack.LEFT_LEDGE,BounceBack.TOP_LEDGE-BounceBack.BOTTOM_LEDGE);
    }

    private void renderBackGround() {
        shapeRenderer.setColor(context.getColorManager().currentColor);
        shapeRenderer.rect(-50,-50,100,100);
    }

}

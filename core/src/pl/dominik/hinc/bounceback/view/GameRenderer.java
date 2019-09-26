package pl.dominik.hinc.bounceback.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import pl.dominik.hinc.bounceback.BounceBack;
import pl.dominik.hinc.bounceback.entities.Spike;
import pl.dominik.hinc.bounceback.tools.RenderableEntity;

public class GameRenderer {
    private BounceBack context;
    private Array<RenderableEntity> renderableEntities;
    private Array<Spike> spikes;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private Label fpsLabel;
    public GameRenderer(BounceBack context){
        this.context = context;
        this.spriteBatch = context.getBatch();
        renderableEntities = new Array<>();
        spikes = new Array<>();
        shapeRenderer = new ShapeRenderer();
        //FPS DEBUG
        //fpsLabel = new Label("xd",context.getSkin());
        //context.getStage().addActor(fpsLabel);
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
        shapeRenderer.end();
        spriteBatch.begin();
        for(RenderableEntity renderableEntity: renderableEntities){
            if(renderableEntity != null){
                renderableEntity.render(spriteBatch);
            }
        }
        spriteBatch.end();
    }

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

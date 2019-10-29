package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import pl.dominik.hinc.bounceback.BounceBack;

public class ParticleManager implements RenderableEntity{

    private BounceBack context;
    private final float particleScale = 9f/BounceBack.SCREEN_WIDTH;
    private ParticleEffectLoader particleEffectLoader;
    //Particle Effects
    private ParticleEffect shieldDestroyedParticle;
    //Particle Booleans
    private boolean shieldDestroyed = false;

    public ParticleManager(BounceBack context){
        this.context = context;
        context.getGameRenderer().addRenderableEntity(this);
        //particleEffectLoader = new ParticleEffectLoader();
    }

    public void loadParticles(){
        shieldDestroyedParticle = new ParticleEffect();
        shieldDestroyedParticle.load(Gdx.files.internal("shieldParticle.p"),Gdx.files.internal(""));

        shieldDestroyedParticle.scaleEffect(particleScale);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if(shieldDestroyed){
            shieldDestroyedParticle.getEmitters().first().setPosition(context.getPlayer().getPlayerBody().getPosition().x,context.getPlayer().getPlayerBody().getPosition().y);
            shieldDestroyedParticle.getEmitters().get(1).setPosition(context.getPlayer().getPlayerBody().getPosition().x,context.getPlayer().getPlayerBody().getPosition().y);
            shieldDestroyedParticle.start();
            shieldDestroyed = false;
        }
        if(!shieldDestroyedParticle.isComplete()){
            shieldDestroyedParticle.update(Gdx.graphics.getDeltaTime());
            shieldDestroyedParticle.draw(spriteBatch);
        }
    }

    public void setShieldDestroyed(boolean shieldDestroyed) {
        this.shieldDestroyed = shieldDestroyed;
    }
}

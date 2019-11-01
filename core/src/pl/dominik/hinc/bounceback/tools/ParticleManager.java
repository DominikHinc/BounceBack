package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import pl.dominik.hinc.bounceback.BounceBack;

public class ParticleManager implements RenderableEntity{

    private BounceBack context;
    private final float particleScale = 9f/BounceBack.SCREEN_WIDTH;
    private ParticleEffectLoader particleEffectLoader;
    //Particle Effects
    private ParticleEffect shieldDestroyedParticle;
    private ParticleEffect dieParticle;
    //Particle Booleans
    private boolean shieldDestroyed = false;
    private boolean playerDied = false;
    //Other
    private Vector2 playerPos;

    public ParticleManager(BounceBack context){
        this.context = context;
        context.getGameRenderer().addRenderableEntity(this);
        playerPos = new Vector2();
        //particleEffectLoader = new ParticleEffectLoader();
    }

    public void loadParticles(){
        shieldDestroyedParticle = new ParticleEffect();
        shieldDestroyedParticle.load(Gdx.files.internal("Particle/shieldParticle.p"),Gdx.files.internal(""));

        dieParticle = new ParticleEffect();
        dieParticle.load(Gdx.files.internal("Particle/dieParticle.p"),Gdx.files.internal(""));
        //dieParticle.getEmitters().first().duration = 0;

        shieldDestroyedParticle.scaleEffect(particleScale);
        dieParticle.scaleEffect(particleScale);
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

        if(playerDied){
            dieParticle.getEmitters().first().setPosition(playerPos.x,playerPos.y);


            dieParticle.start();
            playerDied = false;
        }
        if (!dieParticle.isComplete()){
            dieParticle.update(Gdx.graphics.getDeltaTime());
            dieParticle.draw(spriteBatch);
        }
    }

    public void setShieldDestroyed(boolean shieldDestroyed) {
        this.shieldDestroyed = shieldDestroyed;
    }

    public void playerDied(Vector2 playerPos){
        playerDied = true;
        this.playerPos = playerPos;
    }
}

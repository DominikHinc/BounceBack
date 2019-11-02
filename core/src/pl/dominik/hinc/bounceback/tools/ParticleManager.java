package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.Color;
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
    private ParticleEffect playerJumpParticle;
    //Particle Booleans
    private boolean shieldDestroyed = false;
    private boolean playerDied = false;
    private boolean playerJump = false;
    //Other
    private Vector2 playerDeathPos;
    private Vector2 currentPlayerPos;

    public ParticleManager(BounceBack context){
        this.context = context;
        context.getGameRenderer().addRenderableEntity(this);
        playerDeathPos = new Vector2();
        currentPlayerPos = new Vector2();
        //particleEffectLoader = new ParticleEffectLoader();
    }

    public void loadParticles(){
        shieldDestroyedParticle = new ParticleEffect();
        shieldDestroyedParticle.load(Gdx.files.internal("Particle/shieldParticle.p"),Gdx.files.internal("Particle/"));

        dieParticle = new ParticleEffect();
        dieParticle.load(Gdx.files.internal("Particle/dieParticle.p"),Gdx.files.internal("Particle/"));

        Gdx.app.debug("Particle","jump");
        playerJumpParticle = new ParticleEffect();
        playerJumpParticle.load(Gdx.files.internal("Particle/cloud.p"),Gdx.files.internal("Particle/"));

        shieldDestroyedParticle.scaleEffect(particleScale);
        dieParticle.scaleEffect(particleScale);
        playerJumpParticle.scaleEffect(particleScale);
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
            dieParticle.getEmitters().first().setPosition(playerDeathPos.x, playerDeathPos.y);
            dieParticle.start();
            playerDied = false;
        }

        if (!dieParticle.isComplete()){
            dieParticle.update(Gdx.graphics.getDeltaTime());
            dieParticle.draw(spriteBatch);
        }

        if (playerJump){
            playerJump = false;
            playerJumpParticle.getEmitters().first().setPosition(currentPlayerPos.x,currentPlayerPos.y+context.getPlayer().playerDiameter/2);
            Color c = context.getColorManager().currentColor;
            playerJumpParticle.getEmitters().first().getTint().setColors(new float[]{c.r,c.g,c.b,c.a});
            playerJumpParticle.start();
        }
        if (!playerJumpParticle.isComplete()){
            playerJumpParticle.update(Gdx.graphics.getDeltaTime());
            playerJumpParticle.draw(spriteBatch);
        }

    }

    public void setShieldDestroyed(boolean shieldDestroyed) {
        this.shieldDestroyed = shieldDestroyed;
    }

    public void playerDied(Vector2 playerPos){
        playerDied = true;
        this.playerDeathPos = playerPos;
    }
    public void playerJumpParticle(Vector2 playerPos){
        currentPlayerPos = playerPos;
        playerJump = true;
    }
}

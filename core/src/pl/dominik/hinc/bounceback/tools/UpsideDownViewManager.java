package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.math.Vector2;

import pl.dominik.hinc.bounceback.BounceBack;

public class UpsideDownViewManager {

    private BounceBack context;
    private boolean upsidedown = false;

    public UpsideDownViewManager(BounceBack context){
        this.context = context;
    }

    public void pointAdded(){
        if(context.getScore() % 25 == 0 && context.getScore() != 0) {
            setSpsideDownView(true);
        }
        //Back To normal View
        if (context.getScore() % 25 == 5 && context.getScore() != 0 && context.getScore() !=5){
            setSpsideDownView(false);
        }
    }

    public void setSpsideDownView(boolean isUpsideDown){
        if(isUpsideDown){
            upsidedown = true;
            context.getWorld().setGravity(new Vector2(0, 9.81f));
            context.setReverseSprites(true);
            context.getPlayer().setJumpForce(-context.getPlayer().getJumpForce());
            context.getGameScreen().getGameUI().setRotation(180);
            context.getParticleManager().reverseParticleMotion();
            //for (PlayerRemains pl : context.getPlayer().getPlayerRemainsArray()){
            // pl.getRemainBody().applyLinearImpulse(new Vector2(MathUtils.random(0.3f),MathUtils.random(0.3f)),pl.getRemainBody().getWorldCenter(),true);
            // }
        }else{
            upsidedown = false;
            context.getWorld().setGravity(new Vector2(0,-9.81f));
            context.setReverseSprites(false);
            context.getPlayer().setJumpForce(Math.abs(context.getPlayer().getJumpForce()));
            context.getGameScreen().getGameUI().setRotation(0);
            context.getParticleManager().reverseParticleMotion();
            // for (PlayerRemains pl : context.getPlayer().getPlayerRemainsArray()){
            //pl.getRemainBody().applyLinearImpulse(new Vector2(MathUtils.random(0.3f),MathUtils.random(0.3f)),pl.getRemainBody().getWorldCenter(),true);
            // }
        }
    }

    public boolean isUpsidedown() {
        return upsidedown;
    }
}

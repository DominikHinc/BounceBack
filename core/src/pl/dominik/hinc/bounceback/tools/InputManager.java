package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {
    private Array<InputListener> listeners;

    public InputManager(){
        listeners = new Array<InputListener>();
    }

    public void addListener(InputListener listener){
        listeners.add(listener);
    }
    public void destroyListener(InputListener listener){
        if(listeners.contains(listener,true)){
            listeners.removeValue(listener,true);
        }
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (InputListener listener: listeners){
            listener.touchDown(screenX,screenY,pointer,button);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

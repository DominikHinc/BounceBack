package pl.dominik.hinc.bounceback.enums;


import pl.dominik.hinc.bounceback.screens.AbstractScreen;
import pl.dominik.hinc.bounceback.screens.GameScreen;
import pl.dominik.hinc.bounceback.screens.LoadingScreen;

public enum ScreenType {
    LOADING(LoadingScreen.class),
    GAME(GameScreen.class);
    private final Class<? extends AbstractScreen> screenClass;


    ScreenType(Class<? extends AbstractScreen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends AbstractScreen> getScreenClass() {
        return screenClass;
    }
}

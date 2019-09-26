package pl.dominik.hinc.bounceback.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.dominik.hinc.bounceback.BounceBack;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = BounceBack.SCREEN_WIDTH;
		config.height = BounceBack.SCREEN_HEIGHT;
		new LwjglApplication(new BounceBack(), config);
	}
}

package pl.dominik.hinc.bounceback;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import pl.dominik.hinc.bounceback.BounceBack;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration(){{
			r = 8;
			g = 8;
			b = 8;
			a = 8;
		}};
		initialize(new BounceBack(), config);
	}
}

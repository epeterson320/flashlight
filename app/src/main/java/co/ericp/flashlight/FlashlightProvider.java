package co.ericp.flashlight;

import android.content.Context;
import android.os.Build;

/**
 * The interface to the flashlight.
 */
public class FlashlightProvider {
    private static volatile Flashlight singleton;

    public static Flashlight getInstance(Context c) {
        if (singleton == null) {
            synchronized (FlashlightProvider.class) {
                if (singleton == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        singleton = new FlashlightImpl(c);
                    } else {
                        singleton = new LegacyFlashlightImpl(c);
                    }
                }
            }
        }
        return singleton;
    }

    public static void clear() {
        singleton = null;
    }
}

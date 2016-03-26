package co.ericp.flashlight;

import android.app.Activity;
import android.os.Bundle;

/**
 * The (invisible) activity launched by the launcher icon.
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlashlightProvider.getInstance(this).toggle();
        finish();
    }

}

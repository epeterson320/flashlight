package co.ericp.flashlight;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.widget.Toast;

/**
 * A {@link Flashlight} implemented for APIs 5 to 22.
 */
@TargetApi(Build.VERSION_CODES.ECLAIR)
public class LegacyFlashlightImpl implements Flashlight {

    private Camera camera;
    private Camera.Parameters features;
    private boolean isOn = false;

    public LegacyFlashlightImpl(Context c) {
        Context context = c.getApplicationContext();
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            try {
                camera = Camera.open();
                features = camera.getParameters();
                if (!features.getSupportedFlashModes().contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                    Toast.makeText(context, R.string.not_available, Toast.LENGTH_LONG).show();
                }
            } catch (RuntimeException e) {
                Toast.makeText(context, R.string.not_available, Toast.LENGTH_LONG).show();
            }
        } else {

            Toast.makeText(context, R.string.no_camera, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Toggle the flashlight on or off.
     *
     * @return true if the flashlight is now on.
     */
    @Override
    public boolean toggle() {
        if (isOn) {
            camera.release();
            FlashlightProvider.clear();
            return false;
        } else {
            features.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(features);
            isOn = true;
            return true;
        }
    }
}

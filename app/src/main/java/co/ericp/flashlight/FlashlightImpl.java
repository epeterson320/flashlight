package co.ericp.flashlight;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.widget.Toast;

/**
 * A {@link Flashlight} for the current API.
 */
@TargetApi(Build.VERSION_CODES.M)
public class FlashlightImpl implements Flashlight {

    Application app;
    CameraManager cameraManager;
    String cameraId;
    boolean isOn;

    public FlashlightImpl(Context c) {
        app = (Application) c.getApplicationContext();
        cameraManager = (CameraManager) app.getSystemService(Context.CAMERA_SERVICE);

        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics =
                        cameraManager.getCameraCharacteristics(id);

                Boolean hasCamera =
                        characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

                if (hasCamera != null && hasCamera) {
                    cameraId = id;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            Toast.makeText(c, R.string.not_available, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean toggle() {
        try {
            cameraManager.setTorchMode(cameraId, !isOn);
            isOn = !isOn;
            if (!isOn) FlashlightProvider.clear();
            return isOn;
        } catch (CameraAccessException e) {
            Toast.makeText(app, R.string.not_available, Toast.LENGTH_LONG).show();
            return false;
        }
    }
}

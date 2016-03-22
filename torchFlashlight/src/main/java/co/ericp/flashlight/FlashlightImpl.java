package co.ericp.flashlight;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import co.ericp.flashlight.core.Flashlight;

/**
 * Interface to the camera.
 */
public class FlashlightImpl implements Flashlight {

    LooperThread looperThread = new LooperThread();
    CameraManager cameraManager;
    String cameraId;
    boolean isOn;


    CameraManager.TorchCallback callback = new CameraManager.TorchCallback() {
        @Override
        public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
            if (cameraId.equals(FlashlightImpl.this.cameraId)) {
                isOn = enabled;
            }
        }
    };

    public FlashlightImpl(@NonNull CameraManager cm) throws Exception {
        cameraManager = cm;

        String[] ids = cameraManager.getCameraIdList();
        for (String id : ids) {
            CameraCharacteristics info = cameraManager.getCameraCharacteristics(id);
            Boolean hasFlash = info.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            if (hasFlash != null && hasFlash) {
                cameraId = id;
                break;
            }
        }

        looperThread.run();
        cameraManager.registerTorchCallback(callback, looperThread.handler);

        if (cameraId == null) throw new Exception("No flash available");
    }

    public void turnOn() throws CameraAccessException {
        cameraManager.setTorchMode(cameraId, true);
    }

    public void turnOff() throws CameraAccessException {
        cameraManager.setTorchMode(cameraId, false);
    }

    public synchronized boolean isOn() throws InterruptedException {
        return isOn;
    }

    static class LooperThread extends Thread {
        public Handler handler;

        public void run() {
            Looper.prepare();
            handler = new Handler(Looper.myLooper());
            Looper.loop();
        }
    }
}
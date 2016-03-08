package co.ericp.flashlight;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import co.ericp.flashlight.core.IFlashlight;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FlashlightImplTest extends InstrumentationTestCase {

    boolean mEnabled;
    CameraManager mCameraMgr;
    String mID;

    CameraManager.TorchCallback flashChangeListener = new CameraManager.TorchCallback() {
        @Override
        public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
            mEnabled = enabled;
        }
    };

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        // Get camera manager & camera ID
        mCameraMgr = (CameraManager) getInstrumentation().getContext()
                .getSystemService(Context.CAMERA_SERVICE);

        String ids[] = mCameraMgr.getCameraIdList();

        for (String id : ids) {
            CameraCharacteristics info = mCameraMgr.getCameraCharacteristics(id);
            Boolean hasFlash = info.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            if (hasFlash) {
                mID = id;
                break;
            }
        }

        if (mID == null) {
            throw new Exception("No flash available");
        }

        // Turn flashlight off
        mCameraMgr.setTorchMode(mID, false);

    }

    @Test
    public void turnOnFlashlight() {
        // Turn camera off & register for callbacks when status changed
        mCameraMgr.registerTorchCallback(flashChangeListener, null);

        // Instantiate Flashlight Impl
        IFlashlight flashlight = new FlashlightImpl();

        // Call TurnOnFlashlight
        flashlight.turnOn();

        // Assert that flashlight is on
        mCameraMgr.unregisterTorchCallback(flashChangeListener);

        assertTrue(mEnabled);
    }

    @Test
    public void itTurnsOffFlashlight() {

    }

    @Test
    public void itGetsFlashlightStatus() {

    }

    @Override
    @After
    public void tearDown() throws Exception {
        // Turn off torch
        CameraManager cameraMgr = (CameraManager) getInstrumentation().getContext()
                .getSystemService(Context.CAMERA_SERVICE);

        final String id = cameraMgr.getCameraIdList()[0];
        cameraMgr.setTorchMode(id, false);

        super.tearDown();
    }
}

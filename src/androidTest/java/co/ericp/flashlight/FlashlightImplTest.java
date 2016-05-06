/*
 * Copyright 2016 Eric Peterson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.ericp.flashlight;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraManager.TorchCallback;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.RequiresDevice;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 23)
@TargetApi(23)
@RequiresDevice
@SmallTest
public class FlashlightImplTest {

    private CameraManager cm;
    private String id;
    private boolean isOn = false;
    private boolean awaitingCallback = false;

    @Before
    public void getCameraManager() {
        cm = (CameraManager) InstrumentationRegistry
                .getTargetContext()
                .getSystemService(Context.CAMERA_SERVICE);
    }

    @Test
    public void turnsOnFlashlight() throws Exception {
        // Given a flashlight
        FlashlightImpl flashlight = new FlashlightImpl(cm);
        id = flashlight.cameraId;

        // When I turn on the flashlight
        flashlight.setFlashlight(true);

        // Then the flashlight should be on.
        HandlerThread callbackThread = new HandlerThread("torchCallback");
        callbackThread.start();

        Handler callbackHandler = new Handler(callbackThread.getLooper());

        awaitingCallback = true;
        cm.registerTorchCallback(torchCallback, callbackHandler);

        while (awaitingCallback) {
            synchronized (this) {
                wait(500L);
            }
        }

        assertTrue(isOn);
    }

    @After
    public void clearSingleton() {
        FlashlightProvider.clear();
    }

    @After
    public void shutdownFlash() throws Exception {
        cm.setTorchMode(id, false);
    }

    @After
    public void unregisterCallback() {
        cm.unregisterTorchCallback(torchCallback);
    }

    private final TorchCallback torchCallback = new TorchCallback() {
        @Override
        public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
            if (cameraId.equals(id)) {
                isOn = enabled;
                awaitingCallback = false;
                synchronized (FlashlightImplTest.this) {
                    FlashlightImplTest.this.notifyAll();
                }
            }
        }
    };
}

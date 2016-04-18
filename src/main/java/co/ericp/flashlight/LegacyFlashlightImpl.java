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
import android.hardware.Camera;
import android.os.Build;

/**
 * A {@link Flashlight} implemented for APIs 5 to 22.
 */
@TargetApi(Build.VERSION_CODES.ECLAIR)
@SuppressWarnings("deprecation")
class LegacyFlashlightImpl implements Flashlight {

    private final Camera camera;

    LegacyFlashlightImpl(Camera c) throws CameraUnavailableException {
        camera = c;

        if (!camera.getParameters().getSupportedFlashModes()
                .contains(Camera.Parameters.FLASH_MODE_TORCH)) {
            throw new CameraUnavailableException();
        }
    }

    /**
     * Toggle the flashlight on or off.
     */
    @Override
    public void toggle() {
        if (isOn()) {
            turnOff();
        } else {
            turnOn();
        }
    }

    private boolean isOn() {
        return camera.getParameters().getFlashMode()
                .equals(Camera.Parameters.FLASH_MODE_TORCH);
    }

    private void turnOn() {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
    }

    private void turnOff() {
        camera.release();
        FlashlightProvider.clear();
    }
}

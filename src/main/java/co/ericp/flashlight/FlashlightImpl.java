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
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;

/**
 * A {@link Flashlight} for the current API.
 */
@TargetApi(Build.VERSION_CODES.M)
class FlashlightImpl implements Flashlight {

    private final CameraManager cameraManager;
    String cameraId;

    FlashlightImpl(CameraManager cm) throws Flashlight.UnavailableException {
        cameraManager = cm;
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

            if (cameraId == null) {
                throw new Flashlight.UnavailableException();
            }

        } catch (CameraAccessException e) {
            throw new Flashlight.UnavailableException();
        }
    }

    @Override
    public void setFlashlight(boolean enabled) throws Flashlight.UnavailableException {
        try {
            cameraManager.setTorchMode(cameraId, enabled);
        } catch (CameraAccessException e) {
            throw new Flashlight.UnavailableException();
        }
    }

    @Override
    public void release() {
    }
}

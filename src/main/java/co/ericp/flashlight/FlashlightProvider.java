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

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import static android.content.Context.CAMERA_SERVICE;
import static android.content.pm.PackageManager.FEATURE_CAMERA;

/**
 * The interface to the flashlight.
 *
 * Not thread-safe, so call it from the main thread only.
 */
class FlashlightProvider {
    static Flashlight singleton;

    static Flashlight getInstance(Context c) throws FlashlightUnavailableException {
        if (singleton == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager cm =
                        (CameraManager) c.getSystemService(CAMERA_SERVICE);
                singleton = new FlashlightImpl(cm);

            } else if (c.getPackageManager().hasSystemFeature(FEATURE_CAMERA)) {
                try {
                    @SuppressWarnings("deprecation")
                    Camera camera = Camera.open();
                    singleton = new LegacyFlashlightImpl(camera);
                } catch (RuntimeException e) {
                    throw new FlashlightUnavailableException();
                }
            } else {
                throw new FlashlightUnavailableException();
            }
        }

        return singleton;
    }

    static void clear() {
        singleton = null;
    }
}

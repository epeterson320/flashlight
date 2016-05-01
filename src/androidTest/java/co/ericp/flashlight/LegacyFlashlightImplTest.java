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

import android.hardware.Camera;
import android.os.Build;
import android.support.test.filters.RequiresDevice;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

@RunWith(AndroidJUnit4.class)
@SuppressWarnings("deprecation")
@RequiresDevice
public class LegacyFlashlightImplTest {

    private Camera camera;

    @Before
    public void getCamera() {
        assumeThat(Build.VERSION.SDK_INT, lessThan(Build.VERSION_CODES.M));
        camera = Camera.open();
    }

    @Test
    public void turnsOnCamera() throws FlashlightUnavailableException {
        Flashlight flashlight = new LegacyFlashlightImpl(camera);

        flashlight.toggle();

        String flashMode = camera.getParameters().getFlashMode();
        assertEquals(flashMode, FLASH_MODE_TORCH);
    }

    @After
    public void releaseCamera() throws Exception {
        if (camera != null) {
            camera.release();
        }
    }
}

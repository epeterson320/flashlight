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
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FlashlightImplTest {

    @Test
    @SdkSuppress(minSdkVersion = 23)
    @TargetApi(23)
    public void turnsOnFlashlight() throws Exception {
        Context c = InstrumentationRegistry.getTargetContext();

        CameraManager cm = (CameraManager) c.getSystemService(Context.CAMERA_SERVICE);

        Flashlight flashlight = new FlashlightImpl(cm);

        flashlight.toggle();

        //TODO verify torch is on
    }

}

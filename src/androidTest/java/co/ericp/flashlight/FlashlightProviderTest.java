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
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class FlashlightProviderTest {

    @Test
    public void providesCorrectImpl() throws Exception {
        Context ctx = InstrumentationRegistry.getTargetContext();
        Flashlight flashlight = FlashlightProvider.getInstance(ctx);

        Class expectedClass = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ? FlashlightImpl.class
                : LegacyFlashlightImpl.class;

        assertThat(flashlight, instanceOf(expectedClass));
    }

    @After
    public void clear() {
        FlashlightProvider.clear();
    }
}

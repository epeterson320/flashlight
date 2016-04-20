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

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivityTest {

    Flashlight mockedFlashlight;

    @Rule
    public final ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void mockFlashlight() {
        mockedFlashlight = mock(Flashlight.class);
        FlashlightProvider.singleton = mockedFlashlight;
    }

    @Test
    public void callsToggle() throws Exception {
        activityRule.launchActivity(null);
        verify(mockedFlashlight).toggle();
    }

    @Test
    public void finishesAutomatically() throws Exception {
        MainActivity activity = activityRule.launchActivity(null);
        assertTrue(activity.isFinishing());
    }

    @After
    public void clearSingleton() {
        FlashlightProvider.clear();
    }

}

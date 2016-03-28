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

/**
 * The interface to the flashlight.
 */
public class FlashlightProvider {
    private static volatile Flashlight singleton;

    public static Flashlight getInstance(Context c) {
        if (singleton == null) {
            synchronized (FlashlightProvider.class) {
                if (singleton == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        singleton = new FlashlightImpl(c);
                    } else {
                        singleton = new LegacyFlashlightImpl(c);
                    }
                }
            }
        }
        return singleton;
    }

    public static void clear() {
        singleton = null;
    }
}

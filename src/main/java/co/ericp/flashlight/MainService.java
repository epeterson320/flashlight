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

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.widget.Toast;

/**
 * Long-running process to hold onto the flashlight
 */
public class MainService extends Service {

    private Flashlight flashlight;

    private int startedCount = 0;

    @Override
    public void onCreate() {
        try {
            flashlight = FlashlightProvider.getInstance(this);
        } catch (FlashlightUnavailableException e) {
            Toast.makeText(getApplicationContext(),
                    R.string.not_available, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            flashlight.toggle();
        } catch (FlashlightUnavailableException e) {
            Toast.makeText(getApplicationContext(),
                    R.string.not_available, Toast.LENGTH_LONG)
                    .show();
            stopSelf();
        }

        startedCount++;

        if (startedCount > 1) {
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (startedCount == 1) {
            Toast.makeText(getApplicationContext(),
                    R.string.low_memory, Toast.LENGTH_LONG)
                    .show();
        }
        flashlight.release();
        FlashlightProvider.clear();
    }

    @Override
    public Binder onBind(Intent i) {
        return null;
    }
}

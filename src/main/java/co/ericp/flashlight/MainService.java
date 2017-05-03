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

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

/**
 * Long-running process to hold onto the flashlight
 */
public class MainService extends Service {

    private Flashlight flashlight;

    private int startedCount = 0;
    private static int ID = 1;

    @Override
    public void onCreate() {
        try {
            flashlight = FlashlightProvider.getInstance(this);

            Intent intent = new Intent(this, MainService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, ID, intent, 0x00);
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                    R.drawable.ic_power_48dp,
                    getString(R.string.notification_action_text),
                    pendingIntent)
                    .build();

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_notif_white)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(getString(R.string.notification_title))
                    .setContentText(getString(R.string.notification_text))
                    .addAction(action)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setStyle(new NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0)
                    )
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setCustomBigContentView(null)
                    .build();

            startForeground(ID, notification);
        } catch (Flashlight.UnavailableException e) {
            Toast.makeText(getApplicationContext(),
                    R.string.not_available, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            boolean shouldEnable = (startedCount == 0);
            flashlight.setFlashlight(shouldEnable);
            startedCount++;

        } catch (Flashlight.UnavailableException e) {
            Toast.makeText(getApplicationContext(),
                    R.string.not_available, Toast.LENGTH_LONG)
                    .show();
            stopSelf();
        }

        if (startedCount > 1) {
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public Binder onBind(Intent i) {
        return null;
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

}

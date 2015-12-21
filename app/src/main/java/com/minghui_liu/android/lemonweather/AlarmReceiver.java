package com.minghui_liu.android.lemonweather;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Afei on 15-11-17.
 * Builds an notification upon receiving scheduled system alarm calls.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //build intent
        intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //set specifics of the notificaiont, including icon, sound, etc.
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Weather for the day")
                        .setContentText("Check out weather updates on LemonWeather!")
                        .setAutoCancel(true)
                        .setSound(Uri.parse("android.resource://com.minghui_liu.android.lemonweather/" + R.raw.lemon_squeeze))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        //fire notification
        notificationManager.notify(0, mBuilder.build());
    }
}
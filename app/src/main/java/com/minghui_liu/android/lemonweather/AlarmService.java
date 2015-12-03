package com.minghui_liu.android.lemonweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Afei on 15-11-17.
 * Reference: http://androidideasblog.blogspot.co.uk/2011/07/alarmmanager-and-notificationmanager.html
 */
public class AlarmService {

    private static Context mContext;

    public static void setContext(Context context) {
        AlarmService.mContext = context;
    }

    //consider adding a second parameter for setting the time of alarm
    public static void setAlarm(long time){
        PendingIntent mAlarmSender = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, AlarmReceiver.class), 0);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        // Schedule the alarm
        am.set(AlarmManager.RTC_WAKEUP, time, mAlarmSender);

    }

    public static void setAlarm() {
        // Set the alarm to 10 seconds from now -- DEBUGGING USE ONLY -------------------------
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10);

        /* set notification time - hard code for now
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 00);
        */
        setAlarm(calendar.getTimeInMillis());
    }

    public static void cancelAlarm() {
        PendingIntent mAlarmSender = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, AlarmReceiver.class), 0);
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.cancel(mAlarmSender);
    }

    public static void updateAlarmTime(long time) {
        setAlarm(time);
    }

}

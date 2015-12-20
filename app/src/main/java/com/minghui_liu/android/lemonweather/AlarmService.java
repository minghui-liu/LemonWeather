package com.minghui_liu.android.lemonweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Afei on 15-11-17.
 * Reference: http://androidideasblog.blogspot.co.uk/2011/07/alarmmanager-and-notificationmanager.html
 */
public class AlarmService {

    private static Context mContext;
    private static AlarmManager am;

    public static void setContext(Context context) {
        AlarmService.mContext = context;
    }

    public static void setAlarm(long time){

        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(time);

        //check if alarm time has already passed
        Calendar current = Calendar.getInstance();
        long diff = current.getTimeInMillis() - alarm.getTimeInMillis();
        if (diff > 0) {
            alarm.add(Calendar.DATE, 1);
        }

        time = alarm.getTimeInMillis();
        PendingIntent mAlarmSender = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, AlarmReceiver.class), 0);
        am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        // Schedule the alarm
        am.set(AlarmManager.RTC_WAKEUP, time, mAlarmSender);
        am.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, mAlarmSender);
    }

    public static void cancelAlarm() {
        if(isAlarmSet()) {
            PendingIntent mAlarmSender = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, AlarmReceiver.class), 0);
            am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            am.cancel(mAlarmSender);
        }
    }

    public static void updateAlarmTime(long time) {
        cancelAlarm();
        setAlarm(time);
    }

    //default 8am notification time
    public static long getDefaultTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static boolean isAlarmSet() {
        PendingIntent mAlarmSender = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE);
        return mAlarmSender != null;
    }

    public static Context getmContext(){
        return mContext;
    }

}
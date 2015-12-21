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
 * Contains fields and methods to manage system alarm service for the app's notification
 */
public class AlarmService {

    //keep track of the context for later use in intent
    private static Context mContext;
    //alarm manager
    private static AlarmManager am;

    //Main activity sets the member context
    public static void setContext(Context context) {
        AlarmService.mContext = context;
    }

    /*
     * Sets an alarm service that fires daily at the given time
     * @param long time
     */
    public static void setAlarm(long time){

        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(time);

        //check if alarm time has already passed; if so, add one day
        Calendar current = Calendar.getInstance();
        long diff = current.getTimeInMillis() - alarm.getTimeInMillis();
        if (diff > 0) {
            alarm.add(Calendar.DATE, 1);
        }

        time = alarm.getTimeInMillis();
        PendingIntent mAlarmSender = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, AlarmReceiver.class), 0);
        am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        //schedule the alarm
        am.set(AlarmManager.RTC_WAKEUP, time, mAlarmSender);
        am.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, mAlarmSender);
    }

    /*
     * Cancel the alarm with the intent if it exists
     */
    public static void cancelAlarm() {
        if(isAlarmSet()) {
            PendingIntent mAlarmSender = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, AlarmReceiver.class), 0);
            am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            am.cancel(mAlarmSender);
        }
    }

    /*
     * Updates the alarm time by canceling the existing one and reset with new time
     * @param long time
     */
    public static void updateAlarmTime(long time) {
        cancelAlarm();
        setAlarm(time);
    }

    /*
     * Returns a default 8am notification time
     * @return long time
     */
    public static long getDefaultTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /*
     * Checks if alarm for the intent exists
     * @return boolean, true if intent is already scheduled;
     *                  false otherwise
     */
    public static boolean isAlarmSet() {
        PendingIntent mAlarmSender = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext, AlarmReceiver.class), PendingIntent.FLAG_NO_CREATE);
        return mAlarmSender != null;
    }

    public static Context getmContext(){
        return mContext;
    }

}
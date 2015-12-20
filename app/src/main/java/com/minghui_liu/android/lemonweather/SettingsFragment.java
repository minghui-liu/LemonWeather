package com.minghui_liu.android.lemonweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * Created by kevin on 11/27/15.
 * Modified by Afei on 12/2/15.
 */
public class SettingsFragment extends PreferenceFragment {

    private ListPreference mUnitListPreference;
    private SwitchPreference mNotificationSwitch;
    private TimePreference mTimePreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mUnitListPreference = (ListPreference) getPreferenceManager().findPreference("pref_unit");
        mUnitListPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.equals("metric") ? "Celsius" : "Fahrenheit");
                return true;
            }
        });

        mNotificationSwitch = (SwitchPreference) getPreferenceManager().findPreference("pref_notification");
        mNotificationSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object notificationOn) {
                if ((boolean) notificationOn){
                    mTimePreference.setEnabled(true);
                    long time = PreferenceManager.getDefaultSharedPreferences(AlarmService.getmContext())
                            .getLong("pref_time", AlarmService.getDefaultTime());
                    AlarmService.setAlarm(time);
                }
                else {
                    mTimePreference.setEnabled(false);
                    AlarmService.cancelAlarm();
                }
                return true;
            }
        });

        mTimePreference = (TimePreference) getPreferenceManager().findPreference("pref_time");
        mTimePreference.setEnabled(getPreferenceManager().getSharedPreferences().getBoolean("pref_notification", true));
        mTimePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                long newTime = (long) newValue;
                AlarmService.updateAlarmTime(newTime);
                return true;
            }
        });

    }
}

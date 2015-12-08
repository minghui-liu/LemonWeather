package com.minghui_liu.android.lemonweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by kevin on 11/27/15.
 * Modified by Afei on 12/2/15.
 */
public class SettingsFragment extends PreferenceFragment {

    private SwitchPreference mNotificationSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        mNotificationSwitch = (SwitchPreference) getPreferenceManager().findPreference("pref_notification");
        mNotificationSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object notificationOn) {
                if ((boolean) notificationOn)
                    AlarmService.setAlarm();
                else
                    AlarmService.cancelAlarm();

                return true;
            }
        });
    }
}

package com.minghui_liu.android.lemonweather;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by kevin on 11/27/15.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
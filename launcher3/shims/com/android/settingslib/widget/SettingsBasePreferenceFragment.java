package com.android.settingslib.widget;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

/** Lightweight replacement for the SettingsLib preference fragment base class. */
public class SettingsBasePreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // The launcher supplies its own preferences in its concrete fragment.
    }
}

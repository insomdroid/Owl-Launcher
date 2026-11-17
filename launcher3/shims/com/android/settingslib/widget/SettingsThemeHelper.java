package com.android.settingslib.widget;

import android.content.Context;

/** Lightweight replacement for the SettingsLib expressive-theme helper. */
public final class SettingsThemeHelper {
    private SettingsThemeHelper() {}

    public static boolean isExpressiveTheme(Context context) {
        return false;
    }
}

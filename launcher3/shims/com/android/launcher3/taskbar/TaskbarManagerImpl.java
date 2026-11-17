package com.android.launcher3.taskbar;

import android.net.Uri;
import android.provider.Settings;

/** Build stand-in: only the URI constant used by settings code (taskbar sources not in tree). */
public final class TaskbarManagerImpl {
    private TaskbarManagerImpl() {}
    public static final Uri NAVIGATION_BAR_HINT_URI = Settings.System.getUriFor("navigation_bar_hint");
    public static final Uri ENABLE_TASKBAR_URI = Settings.System.getUriFor("enable_taskbar");
}

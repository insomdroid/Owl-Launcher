package com.android.systemui.shared;

/** Build stand-in for aconfig flags of another module. */
public final class Flags {
    private Flags() {}

    public static boolean extendibleThemeManager() { return false; }
    public static final boolean EXTENDIBLETHEMEMANAGER = false;

    // The standalone build does not enable the SystemUI customization picker.
    // Keep the API used by Launcher3 sources available without pulling in
    // the full SystemUI shared-flags generated library.
    public static boolean notificationDotContrastBorder() { return false; }
    public static final boolean NOTIFICATION_DOT_CONTRAST_BORDER = false;
    public static boolean newCustomizationPickerUi() { return false; }
    public static final boolean NEW_CUSTOMIZATION_PICKER_UI = false;
}

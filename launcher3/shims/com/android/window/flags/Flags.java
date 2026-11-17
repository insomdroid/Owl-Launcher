package com.android.window.flags;

/** Build stand-in for aconfig flags of another module. */
public final class Flags {
    private Flags() {}
    public static boolean enableNonDefaultDisplaySplit() { return false; }
    public static final boolean ENABLENONDEFAULTDISPLAYSPLIT = false;
    public static boolean showHomeBehindDesktop() { return false; }
    public static final boolean SHOW_HOME_BEHIND_DESKTOP = false;
}

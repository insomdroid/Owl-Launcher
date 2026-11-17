package com.android.wm.shell;

/** Build stand-in for aconfig flags of another module. */
public final class Flags {
    private Flags() {}
    public static boolean enableBubbleBar() { return false; }
    public static final boolean ENABLEBUBBLEBAR = false;
    public static boolean enableBubbleBarOnPhones() { return false; }
    public static final boolean ENABLEBUBBLEBARONPHONES = false;
    public static boolean enableGsf() { return false; }
    public static final boolean ENABLEGSF = false;
    public static boolean enableTinyTaskbar() { return false; }
    public static final boolean ENABLETINYTASKBAR = false;
    public static boolean enableRetrievableBubbles() { return false; }
    public static final boolean ENABLERETRIEVABLEBUBBLES = false;
}

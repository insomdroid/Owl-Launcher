package com.android.wm.shell.shared.bubbles;

/** Standalone-build fallback: bubble-anything integration is not included. */
public final class BubbleAnythingFlagHelper {
    private BubbleAnythingFlagHelper() {}

    public static boolean enableCreateAnyBubble() {
        return false;
    }
}

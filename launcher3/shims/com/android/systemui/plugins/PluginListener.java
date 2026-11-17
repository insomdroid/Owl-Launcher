package com.android.systemui.plugins;

/** Build stand-in for the SystemUI plugin_core library (not in this tree). */
public interface PluginListener<T extends Plugin> {
    default void onPluginConnected(T plugin, android.content.Context context) {}
    default void onPluginDisconnected(T plugin) {}
}

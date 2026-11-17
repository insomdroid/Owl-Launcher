/*
 * Owl appearance settings for icons, backplates, and per-area scale and label colour.
 * Areas: "home", "dock", "drawer".
 */
package com.android.launcher3.owl

import android.content.Context
import android.graphics.Color
import com.android.launcher3.LauncherPrefs

object OwlAppearance {

    fun shape(context: Context): String = LauncherPrefs.get(context).get(LauncherPrefs.OWL_ICON_SHAPE)

    /** ARGB backplate for apps outside the pack, or 0 for none. */
    fun backplate(context: Context): Int {
        val prefs = LauncherPrefs.get(context)
        if (prefs.get(LauncherPrefs.OWL_BACKPLATE_MODE) != "custom") return 0
        val base = try {
            Color.parseColor(prefs.get(LauncherPrefs.OWL_BACKPLATE_COLOR))
        } catch (e: IllegalArgumentException) {
            0xFF808080.toInt()
        }
        val opacity = prefs.get(LauncherPrefs.OWL_BACKPLATE_OPACITY).coerceIn(0, 100)
        return (base and 0x00FFFFFF) or ((255 * opacity / 100) shl 24)
    }

    fun iconScale(context: Context, area: String): Float = scaleItem(context, area) / 100f

    fun labelScale(context: Context, area: String): Float = labelScaleItem(context, area) / 100f

    /** Label colour for the area, or 0 for the automatic theme colour. */
    fun labelColor(context: Context, area: String): Int {
        val palette = OwlTokens.current(context)
        return when (LauncherPrefs.get(context).get(labelColorItem(area))) {
            "primary" -> palette.primary
            "display" -> palette.display
            "secondary" -> palette.secondary
            else -> 0
        }
    }

    private fun scaleItem(context: Context, area: String): Int = LauncherPrefs.get(context).get(
        when (area) {
            "dock" -> LauncherPrefs.OWL_SCALE_DOCK
            "drawer" -> LauncherPrefs.OWL_SCALE_DRAWER
            else -> LauncherPrefs.OWL_SCALE_HOME
        })

    private fun labelScaleItem(context: Context, area: String): Int = LauncherPrefs.get(context).get(
        when (area) {
            "dock" -> LauncherPrefs.OWL_LABEL_SCALE_DOCK
            "drawer" -> LauncherPrefs.OWL_LABEL_SCALE_DRAWER
            else -> LauncherPrefs.OWL_LABEL_SCALE_HOME
        })

    private fun labelColorItem(area: String) = when (area) {
        "dock" -> LauncherPrefs.OWL_LABEL_COLOR_DOCK
        "drawer" -> LauncherPrefs.OWL_LABEL_COLOR_DRAWER
        else -> LauncherPrefs.OWL_LABEL_COLOR_HOME
    }

    /** Settings were changed; the launcher reloads when it comes back to the front. */
    fun markDirty(context: Context) {
        LauncherPrefs.get(context).put(LauncherPrefs.OWL_APPEARANCE_DIRTY to true)
    }

    fun consumeDirty(context: Context): Boolean {
        val dirty = LauncherPrefs.get(context).get(LauncherPrefs.OWL_APPEARANCE_DIRTY)
        if (dirty) LauncherPrefs.get(context).put(LauncherPrefs.OWL_APPEARANCE_DIRTY to false)
        return dirty
    }
}

/*
 * Option lists for the Owl choice pages. Values are stored as strings in LauncherPrefs.
 */
package com.android.launcher3.owl

import android.content.Context
import com.android.launcher3.LauncherPrefs

object OwlChoices {

    private val LABEL_COLORS = listOf(
        "automatic" to "Automatic",
        "primary" to "Primary",
        "display" to "Display",
        "secondary" to "Secondary",
    )

    val options: Map<String, List<Pair<String, String>>> = mapOf(
        "owl_icon_shape" to listOf(
            "system" to "System",
            "circle" to "Circle",
            "squircle" to "Squircle",
            "rounded_square" to "Rounded square",
            "square" to "Square",
            "teardrop" to "Teardrop",
            "hexagon" to "Hexagon",
            "octagon" to "Octagon",
            "flower" to "Flower",
            "cat" to "Cat",
        ),
        "owl_backplate_mode" to listOf("none" to "None", "custom" to "Custom colour"),
        "owl_backplate_color" to listOf(
            "#808080" to "Grey",
            "#FFFFFF" to "White",
            "#000000" to "Black",
            "#1C1C1C" to "Charcoal",
        ),
        "owl_home_page" to (0..9).map { it.toString() to "Page ${it + 1}" },
        "owl_label_color_home" to LABEL_COLORS,
        "owl_label_color_dock" to LABEL_COLORS,
        "owl_label_color_drawer" to LABEL_COLORS,
    )

    /** The page the HOME button returns to (0 = the leftmost page). */
    fun homePage(context: Context): Int = current(context, "owl_home_page").toIntOrNull() ?: 0

    fun label(key: String, value: String): String =
        options[key]?.firstOrNull { it.first.equals(value, ignoreCase = true) }?.second ?: value

    fun current(context: Context, key: String): String = when (key) {
        "owl_icon_shape" -> LauncherPrefs.get(context).get(LauncherPrefs.OWL_ICON_SHAPE)
        "owl_backplate_mode" -> LauncherPrefs.get(context).get(LauncherPrefs.OWL_BACKPLATE_MODE)
        "owl_backplate_color" -> LauncherPrefs.get(context).get(LauncherPrefs.OWL_BACKPLATE_COLOR)
        "owl_home_page" -> LauncherPrefs.get(context).get(LauncherPrefs.OWL_HOME_PAGE)
        "owl_label_color_home" -> LauncherPrefs.get(context).get(LauncherPrefs.OWL_LABEL_COLOR_HOME)
        "owl_label_color_dock" -> LauncherPrefs.get(context).get(LauncherPrefs.OWL_LABEL_COLOR_DOCK)
        "owl_label_color_drawer" -> LauncherPrefs.get(context).get(LauncherPrefs.OWL_LABEL_COLOR_DRAWER)
        else -> ""
    }

    fun write(context: Context, key: String, value: String) {
        val prefs = LauncherPrefs.get(context)
        when (key) {
            "owl_icon_shape" -> prefs.put(LauncherPrefs.OWL_ICON_SHAPE to value)
            "owl_backplate_mode" -> prefs.put(LauncherPrefs.OWL_BACKPLATE_MODE to value)
            "owl_backplate_color" -> prefs.put(LauncherPrefs.OWL_BACKPLATE_COLOR to value)
            "owl_home_page" -> prefs.put(LauncherPrefs.OWL_HOME_PAGE to value)
            "owl_label_color_home" -> prefs.put(LauncherPrefs.OWL_LABEL_COLOR_HOME to value)
            "owl_label_color_dock" -> prefs.put(LauncherPrefs.OWL_LABEL_COLOR_DOCK to value)
            "owl_label_color_drawer" -> prefs.put(LauncherPrefs.OWL_LABEL_COLOR_DRAWER to value)
        }
    }
}

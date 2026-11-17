/*
 * Owl Launcher design tokens (translated from vibe-nothing-ui-design, MIT).
 * Single source of truth for colors and theme modes. See docs/DESIGN.md.
 */
package com.android.launcher3.owl

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color

enum class OwlThemeMode { SYSTEM, LIGHT, DARK, AMOLED }

data class OwlPalette(
    val bg: Int,
    val surface: Int,
    val raised: Int,
    val line: Int,
    val line2: Int,
    val muted: Int,
    val secondary: Int,
    val primary: Int,
    val display: Int,
    val accent: Int,
    val accentText: Int,
    val success: Int,
    val warning: Int,
    val error: Int,
)

object OwlTokens {
    private val ACCENT = Color.parseColor("#D71921")

    val DARK = OwlPalette(
        bg = Color.parseColor("#000000"),
        surface = Color.parseColor("#0C0C0C"),
        raised = Color.parseColor("#171717"),
        line = Color.parseColor("#262626"),
        line2 = Color.parseColor("#3A3A3A"),
        muted = Color.parseColor("#5A5A5A"),
        secondary = Color.parseColor("#8C8C8C"),
        primary = Color.parseColor("#EDEDED"),
        display = Color.parseColor("#FFFFFF"),
        accent = ACCENT,
        accentText = Color.parseColor("#FF4438"),
        success = Color.parseColor("#7BE38A"),
        warning = Color.parseColor("#F2C94C"),
        error = Color.parseColor("#FF5247"),
    )

    val LIGHT = OwlPalette(
        bg = Color.parseColor("#F2F2F2"),
        surface = Color.parseColor("#FFFFFF"),
        raised = Color.parseColor("#EDEDED"),
        line = Color.parseColor("#E2E2E2"),
        line2 = Color.parseColor("#CFCFCF"),
        muted = Color.parseColor("#9A9A9A"),
        secondary = Color.parseColor("#585A5A"),
        primary = Color.parseColor("#1C1C1C"),
        display = Color.parseColor("#000000"),
        accent = ACCENT,
        accentText = Color.parseColor("#C2141C"),
        success = Color.parseColor("#3D8B4A"),
        warning = Color.parseColor("#9C6B00"),
        error = Color.parseColor("#D23B30"),
    )

    // AMOLED: pure black surfaces so OLED pixels stay off; hairlines lifted slightly.
    val AMOLED = DARK.copy(
        surface = Color.parseColor("#000000"),
        raised = Color.parseColor("#0C0C0C"),
        line = Color.parseColor("#1A1A1A"),
        line2 = Color.parseColor("#2A2A2A"),
    )

    fun resolve(mode: OwlThemeMode, context: Context): OwlPalette = when (mode) {
        OwlThemeMode.LIGHT -> LIGHT
        OwlThemeMode.DARK -> DARK
        OwlThemeMode.AMOLED -> AMOLED
        OwlThemeMode.SYSTEM -> {
            val night = (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            if (night) DARK else LIGHT
        }
    }
}

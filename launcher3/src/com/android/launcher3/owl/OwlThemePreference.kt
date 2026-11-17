/*
 * Settings row for the Owl theme. Tapping cycles System -> Light -> Dark -> AMOLED.
 * No dialog is used, so it does not depend on AppCompat dialog resources.
 */
package com.android.launcher3.owl

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import com.android.launcher3.LauncherPrefs

class OwlThemePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : Preference(context, attrs) {

    init {
        isPersistent = false
        setOnPreferenceClickListener {
            cycle()
            true
        }
    }

    override fun onAttached() {
        super.onAttached()
        refreshSummary()
    }

    private fun cycle() {
        val modes = OwlThemeMode.values()
        val next = modes[(OwlTokens.modeOf(context).ordinal + 1) % modes.size]
        LauncherPrefs.get(context).put(LauncherPrefs.OWL_THEME_MODE to next.name.lowercase())
        refreshSummary()
    }

    private fun refreshSummary() {
        val label = when (OwlTokens.modeOf(context)) {
            OwlThemeMode.SYSTEM -> "System"
            OwlThemeMode.LIGHT -> "Light"
            OwlThemeMode.DARK -> "Dark"
            OwlThemeMode.AMOLED -> "AMOLED"
        }
        summary = "$label. Tap to change. Takes effect after reopening the launcher."
    }
}

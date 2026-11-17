/*
 * Settings row for the icon pack. Tapping cycles: None -> each installed pack -> None.
 */
package com.android.launcher3.owl

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import com.android.launcher3.LauncherPrefs

class OwlIconPackPreference @JvmOverloads constructor(
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
        val options = listOf("") + OwlIconPacks.installedPacks(context)
        val current = OwlIconPacks.selected(context)
        val next = options[(options.indexOf(current).coerceAtLeast(0) + 1) % options.size]
        LauncherPrefs.get(context).put(LauncherPrefs.OWL_ICON_PACK to next)
        refreshSummary()
    }

    private fun refreshSummary() {
        val pack = OwlIconPacks.selected(context)
        summary = if (pack.isEmpty()) {
            "Default icons. Tap to choose an installed icon pack."
        } else {
            "${OwlIconPacks.labelOf(context, pack)}. Tap to change."
        }
    }
}

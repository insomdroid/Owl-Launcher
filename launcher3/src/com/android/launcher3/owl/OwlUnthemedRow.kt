/*
 * Settings row for apps outside the icon pack. Shows the current mode; tapping opens
 * OwlUnthemedFragment.
 */
package com.android.launcher3.owl

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import com.android.launcher3.LauncherPrefs

class OwlUnthemedRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : Preference(context, attrs) {

    override fun onAttached() {
        super.onAttached()
        summary = labelFor(LauncherPrefs.get(context).get(LauncherPrefs.OWL_UNTHEMED_MODE))
    }

    companion object {
        fun labelFor(mode: String): String = when (mode) {
            "original" -> "Original colours"
            "monochrome" -> "Monochrome"
            else -> "Automatic"
        }
    }
}

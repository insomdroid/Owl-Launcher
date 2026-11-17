/*
 * Settings row for the icon pack. Shows the current pack; tapping opens OwlIconPackFragment.
 */
package com.android.launcher3.owl

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference

class OwlIconPackRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : Preference(context, attrs) {

    override fun onAttached() {
        super.onAttached()
        val pack = OwlIconPacks.selected(context)
        summary = if (pack.isEmpty()) "Default icons" else OwlIconPacks.labelOf(context, pack)
    }
}

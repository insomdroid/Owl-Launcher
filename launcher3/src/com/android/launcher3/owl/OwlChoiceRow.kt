/*
 * Settings row for one Owl choice. The preference key comes from the "owl_pref" extra.
 * Tapping opens OwlChoiceFragment with the same extra.
 */
package com.android.launcher3.owl

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference

class OwlChoiceRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : Preference(context, attrs) {

    override fun onAttached() {
        super.onAttached()
        val key = extras.getString("owl_pref") ?: return
        summary = OwlChoices.label(key, OwlChoices.current(context, key))
    }
}

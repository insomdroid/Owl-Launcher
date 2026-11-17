/*
 * Settings category listing every icon option: Default plus each installed icon pack.
 * The selected option is marked. Tapping an option selects it.
 */
package com.android.launcher3.owl

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import com.android.launcher3.LauncherPrefs

class OwlIconPackCategory @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : PreferenceCategory(context, attrs) {

    override fun onAttached() {
        super.onAttached()
        rebuild()
    }

    private fun rebuild() {
        removeAll()
        val selected = OwlIconPacks.selected(context)
        val packs = OwlIconPacks.installedPacks(context)
        val options = listOf("" to "Default icons") +
            packs.map { it to OwlIconPacks.labelOf(context, it) }

        if (packs.isEmpty()) {
            addPreference(Preference(context).apply {
                isPersistent = false
                title = "No icon packs installed"
                summary = "Install an icon pack, then reopen Settings."
                isSelectable = false
            })
        }
        options.forEach { (pkg, label) ->
            addPreference(Preference(context).apply {
                isPersistent = false
                key = "owl_icon_pack_option_" + pkg.ifEmpty { "default" }
                title = label
                summary = if (pkg == selected) "Selected" else null
                setOnPreferenceClickListener {
                    LauncherPrefs.get(context).put(LauncherPrefs.OWL_ICON_PACK to pkg)
                    rebuild()
                    true
                }
            })
        }
    }
}

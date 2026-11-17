/*
 * Icon pack page: a plain list of Default plus each installed icon pack.
 * The current choice is marked "Selected". Tapping one applies it and goes back.
 */
package com.android.launcher3.owl

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class OwlIconPackFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val ctx = requireContext()
        val screen = preferenceManager.createPreferenceScreen(ctx)
        preferenceScreen = screen

        val selected = OwlIconPacks.selected(ctx)
        val packs = OwlIconPacks.installedPacks(ctx)
        val options = listOf("" to "Default icons") +
            packs.map { it to OwlIconPacks.labelOf(ctx, it) }

        if (packs.isEmpty()) {
            screen.addPreference(Preference(ctx).apply {
                key = "owl_pack_none"
                title = "No icon packs installed"
                summary = "Install an icon pack, then come back here."
                isSelectable = false
            })
        }
        options.forEach { (pkg, label) ->
            screen.addPreference(Preference(ctx).apply {
                key = "owl_pack_" + pkg.ifEmpty { "default" }
                isPersistent = false
                title = label
                summary = if (pkg == selected) "Selected" else ""
                setOnPreferenceClickListener {
                    OwlIconPacks.select(ctx, pkg)
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    true
                }
            })
        }
    }
}

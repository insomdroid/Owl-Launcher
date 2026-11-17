/*
 * Page for apps outside the icon pack: Automatic, Original colours, or Monochrome.
 * Same list pattern as the icon pack page. Tapping one applies it and goes back.
 */
package com.android.launcher3.owl

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.launcher3.LauncherPrefs

class OwlUnthemedFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val ctx = requireContext()
        val screen = preferenceManager.createPreferenceScreen(ctx)
        preferenceScreen = screen

        val current = LauncherPrefs.get(ctx).get(LauncherPrefs.OWL_UNTHEMED_MODE)
        listOf("automatic", "original", "monochrome").forEach { mode ->
            screen.addPreference(Preference(ctx).apply {
                key = "owl_unthemed_$mode"
                isPersistent = false
                title = OwlUnthemedRow.labelFor(mode)
                summary = if (mode == current) "Selected" else ""
                setOnPreferenceClickListener {
                    LauncherPrefs.get(ctx).put(LauncherPrefs.OWL_UNTHEMED_MODE to mode)
                    OwlIconPacks.select(ctx, OwlIconPacks.selected(ctx))
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    true
                }
            })
        }
    }
}

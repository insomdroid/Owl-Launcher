/*
 * Generic choice page: one row per option, the current one marked "Selected".
 * Tapping applies the option, reloads the launcher, and goes back.
 */
package com.android.launcher3.owl

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class OwlChoiceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val ctx = requireContext()
        val screen = preferenceManager.createPreferenceScreen(ctx)
        preferenceScreen = screen
        val key = arguments?.getString("owl_pref") ?: return
        val current = OwlChoices.current(ctx, key)
        OwlChoices.options[key].orEmpty().forEach { (value, label) ->
            screen.addPreference(Preference(ctx).apply {
                this.key = "${key}_$value"
                isPersistent = false
                title = label
                summary = if (value.equals(current, ignoreCase = true)) "Selected" else ""
                setOnPreferenceClickListener {
                    OwlChoices.write(ctx, key, value)
                    OwlIconPacks.select(ctx, OwlIconPacks.selected(ctx))
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    true
                }
            })
        }
    }
}

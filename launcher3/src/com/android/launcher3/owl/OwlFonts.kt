/*
 * Owl Launcher typefaces: Doto (dot display), Geist (UI), Geist Mono (labels/data).
 * All fonts are SIL OFL 1.1, bundled under res/font/ (see docs/fonts/OFL).
 */
package com.android.launcher3.owl

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.android.launcher3.R

object OwlFonts {
    fun doto(context: Context): Typeface? = load(context, R.font.owl_doto_rond)
    fun geist(context: Context): Typeface? = load(context, R.font.owl_geist_regular)
    fun geistMedium(context: Context): Typeface? = load(context, R.font.owl_geist_medium)
    fun geistSemiBold(context: Context): Typeface? = load(context, R.font.owl_geist_semibold)
    fun geistMono(context: Context): Typeface? = load(context, R.font.owl_geist_mono)

    private fun load(context: Context, id: Int): Typeface? =
        ResourcesCompat.getFont(context, id)
}

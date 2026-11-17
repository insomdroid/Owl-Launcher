/*
 * Owl dot field: sparse dots on a 120dp pitch. Used as the settings background.
 */
package com.android.launcher3.owl

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class OwlDotDrawable(context: Context) : Drawable() {

    private val density = context.resources.displayMetrics.density
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = (OwlTokens.current(context).display and 0x00FFFFFF) or (0x6B shl 24)
    }

    override fun draw(canvas: Canvas) {
        val pitch = 120f * density
        val radius = 0.65f * density
        var y = bounds.top + pitch / 2f
        while (y < bounds.bottom) {
            var x = bounds.left + pitch / 2f
            while (x < bounds.right) {
                canvas.drawCircle(x, y, radius, paint)
                x += pitch
            }
            y += pitch
        }
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}
    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}

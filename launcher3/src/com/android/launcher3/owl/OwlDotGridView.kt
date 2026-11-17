/*
 * Sparse dot field behind the workspace (design: 120px pitch on web, ~40dp here).
 * Static drawing, no animation, so it costs nothing while idle.
 */
package com.android.launcher3.owl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class OwlDotGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : View(context, attrs, defStyle) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val pitch = 40f * resources.displayMetrics.density
    private val radius = 1f * resources.displayMetrics.density
    private var dotColor = 0x40FFFFFF
    private var bgColor = 0xFF000000.toInt()

    init {
        setWillNotDraw(false)
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        isClickable = false
        refreshColor()
    }

    private fun refreshColor() {
        val palette = OwlTokens.current(context)
        bgColor = palette.bg
        dotColor = (palette.display and 0x00FFFFFF) or (0x40 shl 24)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refreshColor()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Opaque Owl background, so the theme is visible regardless of the system wallpaper.
        canvas.drawColor(bgColor)
        paint.color = dotColor
        var y = pitch / 2f
        while (y < height) {
            var x = pitch / 2f
            while (x < width) {
                canvas.drawCircle(x, y, radius, paint)
                x += pitch
            }
            y += pitch
        }
    }
}

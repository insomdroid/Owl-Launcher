/*
 * Owl icon wrapper: applies the chosen shape (clip), an optional backplate, and marks the
 * icon so Launcher3 does not add its own adaptive wrapper (Nomina draws icons this way).
 */
package com.android.launcher3.icons

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper

class OwlPackDrawable @JvmOverloads constructor(
    inner: Drawable,
    private val shape: String = "system",
    private val backplate: Int = 0,
) : DrawableWrapper(inner) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }

    override fun draw(canvas: Canvas) {
        val rect = RectF(bounds)
        val clip = OwlIconShape.path(shape, rect)
        if (backplate != 0) {
            paint.color = backplate
            canvas.drawPath(clip ?: OwlIconShape.path("rounded_square", rect)!!, paint)
        }
        if (clip != null) {
            canvas.save()
            canvas.clipPath(clip)
            super.draw(canvas)
            canvas.restore()
        } else {
            super.draw(canvas)
        }
    }
}

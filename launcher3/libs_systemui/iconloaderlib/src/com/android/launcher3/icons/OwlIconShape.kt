/*
 * Owl icon shapes (Nomina's list). Each returns a path inside [rect], or null for "system".
 */
package com.android.launcher3.icons

import android.graphics.Path
import android.graphics.RectF
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sin

object OwlIconShape {

    fun path(shape: String, rect: RectF): Path? {
        val cx = rect.centerX()
        val cy = rect.centerY()
        val radius = min(rect.width(), rect.height()) / 2f
        return when (shape) {
            "circle" -> Path().apply { addOval(rect, Path.Direction.CW) }
            "squircle" -> Path().apply {
                // Superellipse with exponent 4: |x|^4 + |y|^4 = 1.
                for (i in 0..160) {
                    val t = 2 * PI * i / 160
                    val x = cx + radius * signedPow(cos(t), 0.5)
                    val y = cy + radius * signedPow(sin(t), 0.5)
                    if (i == 0) moveTo(x.toFloat(), y.toFloat()) else lineTo(x.toFloat(), y.toFloat())
                }
                close()
            }
            "rounded_square", "cat" -> Path().apply {
                val r = rect.width() * 0.22f
                addRoundRect(rect, r, r, Path.Direction.CW)
            }
            "square" -> Path().apply { addRect(rect, Path.Direction.CW) }
            "teardrop" -> Path().apply {
                val h = rect.width() / 2f
                // Top-right corner stays square, the others are fully rounded.
                addRoundRect(rect, floatArrayOf(h, h, 0f, 0f, h, h, h, h), Path.Direction.CW)
            }
            "hexagon" -> polygon(cx, cy, radius, 6, -PI / 2)
            "octagon" -> polygon(cx, cy, radius, 8, -PI / 8)
            "flower" -> Path().apply {
                for (i in 0..180) {
                    val t = 2 * PI * i / 180
                    val rr = radius * (0.86 + 0.14 * cos(6 * t))
                    val x = cx + rr * cos(t)
                    val y = cy + rr * sin(t)
                    if (i == 0) moveTo(x.toFloat(), y.toFloat()) else lineTo(x.toFloat(), y.toFloat())
                }
                close()
            }
            else -> null
        }
    }

    private fun polygon(cx: Float, cy: Float, radius: Float, sides: Int, start: Double): Path =
        Path().apply {
            for (i in 0 until sides) {
                val t = start + 2 * PI * i / sides
                val x = cx + radius * cos(t)
                val y = cy + radius * sin(t)
                if (i == 0) moveTo(x.toFloat(), y.toFloat()) else lineTo(x.toFloat(), y.toFloat())
            }
            close()
        }

    private fun signedPow(v: Double, e: Double): Double = sign(v) * abs(v).pow(e)
}

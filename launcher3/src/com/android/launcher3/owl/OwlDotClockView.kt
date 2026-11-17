/*
 * Dot-matrix clock (HH:mm). Redraws once per minute aligned to the minute boundary,
 * and stops its timer while the window is not visible, to save battery.
 */
package com.android.launcher3.owl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import java.util.Calendar

class OwlDotClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : View(context, attrs, defStyle) {

    private val litPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val unlitPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private var text = ""
    private val dotoTypeface: Typeface? by lazy { OwlFonts.doto(context) }
    private var palette = OwlTokens.DARK

    private val tick = Runnable {
        updateTime()
        scheduleNext()
    }

    init {
        isClickable = false
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        stop()
        super.onDetachedFromWindow()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == VISIBLE) start() else stop()
    }

    private fun start() {
        updateTime()
        scheduleNext()
    }

    private fun stop() {
        removeCallbacks(tick)
    }

    private fun scheduleNext() {
        removeCallbacks(tick)
        val delay = 60_000L - (System.currentTimeMillis() % 60_000L)
        postDelayed(tick, delay)
    }

    private fun updateTime() {
        val cal = Calendar.getInstance()
        text = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
        palette = OwlTokens.current(context)
        litPaint.color = palette.display
        // Unlit dots: the same color at low alpha, like an LED matrix at rest.
        unlitPaint.color = (palette.display and 0x00FFFFFF) or (0x22 shl 24)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (text.isEmpty() || width == 0 || height == 0) return

        val doto = dotoTypeface
        if (doto != null) {
            // Doto (ROND 100) gives round dots directly; size the text to the view height.
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                typeface = doto
                color = litPaint.color
                textAlign = Paint.Align.CENTER
            }
            paint.textSize = height * 0.9f
            val maxWidth = width * 0.95f
            val measured = paint.measureText(text)
            if (measured > maxWidth) paint.textSize *= maxWidth / measured
            val baseline = height / 2f - (paint.descent() + paint.ascent()) / 2f
            canvas.drawText(text, width / 2f, baseline, paint)
            return
        }

        val glyphs = text.map { DotFont.glyph(it) }
        val gap = 1
        val totalCols = glyphs.size * DotFont.COLS + (glyphs.size - 1) * gap
        val pitch = minOf(width.toFloat() / totalCols, height.toFloat() / DotFont.ROWS)
        val radius = pitch * 0.38f
        val startX = (width - totalCols * pitch) / 2f + pitch / 2f
        val startY = (height - DotFont.ROWS * pitch) / 2f + pitch / 2f

        var colOffset = 0
        for (glyph in glyphs) {
            for (row in 0 until DotFont.ROWS) {
                for (col in 0 until DotFont.COLS) {
                    val lit = glyph[row][col] == '#'
                    val cx = startX + (colOffset + col) * pitch
                    val cy = startY + row * pitch
                    canvas.drawCircle(cx, cy, radius, if (lit) litPaint else unlitPaint)
                }
            }
            colOffset += DotFont.COLS + gap
        }
    }
}

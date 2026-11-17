/*
 * Owl hero mark: a 25x25 dot matrix in the Owl display color, with a status dot.
 * Static drawing, no animation.
 */
package com.android.launcher3.owl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class OwlHeroGlyph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : View(context, attrs, defStyle) {

    private val rows = arrayOf(
        ".........................",
        ".........................",
        "..........#####..........",
        "....#...##.....##..#.....",
        ".....###.........##.#....",
        "......#...........#......",
        ".....#.............#.....",
        "....##.............##....",
        "....#...............#....",
        "....#..###.....###..#....",
        "....#.#####...#####.#....",
        "...##.#####...#####.##...",
        "....#..###.....###..#....",
        "....#...............#....",
        "....#.......#.......#....",
        "....##.....###.....##....",
        ".....#....#####....#.....",
        "......#...........#......",
        "......##.........##......",
        "........##.....##........",
        "..........#####..........",
        ".........................",
        ".........................",
        ".........................",
        ".........................",
    )

    private val litPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val unlitPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val statusPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }

    init {
        isClickable = false
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        refreshColors()
    }

    private fun refreshColors() {
        val palette = OwlTokens.current(context)
        litPaint.color = palette.display
        unlitPaint.color = (palette.display and 0x00FFFFFF) or (0x1A shl 24)
        statusPaint.color = palette.accent
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refreshColors()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = rows.size
        val pitch = minOf(width, height).toFloat() / size
        val radius = pitch * 0.36f
        val startX = (width - size * pitch) / 2f + pitch / 2f
        val startY = (height - size * pitch) / 2f + pitch / 2f
        for (y in 0 until size) {
            for (x in 0 until size) {
                val lit = rows[y][x] == '#'
                canvas.drawCircle(startX + x * pitch, startY + y * pitch, radius,
                    if (lit) litPaint else unlitPaint)
            }
        }
        // Status dot: 9dp solid accent circle at the top-right corner.
        val dot = 4.5f * resources.displayMetrics.density
        canvas.drawCircle(width - dot - 4f, dot + 4f, dot, statusPaint)
    }
}

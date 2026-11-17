/*
 * Owl clock widget: HH:mm in the Doto dot-matrix face, drawn in the Owl display color.
 * Refreshes once a minute while at least one instance is on screen.
 */
package com.android.launcher3.owl.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.TypedValue
import android.widget.RemoteViews
import com.android.launcher3.R
import com.android.launcher3.owl.OwlFonts
import com.android.launcher3.owl.OwlTokens
import java.util.Calendar

class OwlClockWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) render(context, manager, id)
        scheduleTicks(context)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context, manager: AppWidgetManager, appWidgetId: Int, newOptions: android.os.Bundle,
    ) {
        render(context, manager, appWidgetId)
    }

    override fun onEnabled(context: Context) = scheduleTicks(context)

    override fun onDisabled(context: Context) {
        if (ids(context).isEmpty()) {
            val am = context.getSystemService(AlarmManager::class.java)
            am.cancel(tickIntent(context))
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_TICK) {
            val manager = AppWidgetManager.getInstance(context)
            for (id in ids(context)) render(context, manager, id)
            return
        }
        super.onReceive(context, intent)
    }

    private fun ids(context: Context): IntArray =
        AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, OwlClockWidget::class.java))

    private fun tickIntent(context: Context): PendingIntent = PendingIntent.getBroadcast(
        context, 0,
        Intent(context, OwlClockWidget::class.java).setAction(ACTION_TICK),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

    private fun scheduleTicks(context: Context) {
        if (ids(context).isEmpty()) return
        val nextMinute = (System.currentTimeMillis() / 60_000L + 1) * 60_000L
        context.getSystemService(AlarmManager::class.java)
            .setRepeating(AlarmManager.RTC, nextMinute, 60_000L, tickIntent(context))
    }

    private fun render(context: Context, manager: AppWidgetManager, id: Int) {
        val options = manager.getAppWidgetOptions(id)
        val density = context.resources.displayMetrics.density
        val w = (options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, 250) * density).toInt().coerceAtLeast(64)
        val h = (options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, 110) * density).toInt().coerceAtLeast(64)
        val views = RemoteViews(context.packageName, R.layout.owl_clock_widget)
        views.setImageViewBitmap(R.id.owl_clock_image, drawTime(context, w, h))
        manager.updateAppWidget(id, views)
    }

    companion object {
        const val ACTION_TICK = "com.android.launcher3.owl.widget.CLOCK_TICK"

        fun drawTime(context: Context, width: Int, height: Int): Bitmap {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val cal = Calendar.getInstance()
            val text = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = OwlTokens.current(context).display
                typeface = OwlFonts.doto(context)
                textAlign = Paint.Align.CENTER
            }
            // Fit the text inside the bitmap, keeping 8% margins on each side.
            paint.textSize = height.toFloat()
            val fitW = width * 0.84f / paint.measureText(text).coerceAtLeast(1f)
            paint.textSize = (height * 0.9f * minOf(1f, fitW)).coerceAtMost(height * 0.9f)
            val baseline = height / 2f - (paint.descent() + paint.ascent()) / 2f
            canvas.drawText(text, width / 2f, baseline, paint)
            return bitmap
        }
    }
}

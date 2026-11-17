/*
 * Owl media widget: previous, play/pause and next for whatever app is playing.
 * Controls are sent as media key events, so no extra permission is needed.
 */
package com.android.launcher3.owl.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.view.KeyEvent
import android.widget.RemoteViews
import com.android.launcher3.R

class OwlMediaWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.owl_media_widget)
            views.setOnClickPendingIntent(R.id.owl_media_prev, action(context, ACTION_PREV, 1))
            views.setOnClickPendingIntent(R.id.owl_media_play, action(context, ACTION_PLAY, 2))
            views.setOnClickPendingIntent(R.id.owl_media_next, action(context, ACTION_NEXT, 3))
            manager.updateAppWidget(id, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val code = when (intent.action) {
            ACTION_PREV -> KeyEvent.KEYCODE_MEDIA_PREVIOUS
            ACTION_PLAY -> KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
            ACTION_NEXT -> KeyEvent.KEYCODE_MEDIA_NEXT
            else -> return super.onReceive(context, intent)
        }
        val am = context.getSystemService(AudioManager::class.java)
        am.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, code))
        am.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_UP, code))
    }

    private fun action(context: Context, name: String, request: Int): PendingIntent =
        PendingIntent.getBroadcast(
            context, request,
            Intent(context, OwlMediaWidget::class.java).setAction(name),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

    companion object {
        const val ACTION_PREV = "com.android.launcher3.owl.widget.MEDIA_PREV"
        const val ACTION_PLAY = "com.android.launcher3.owl.widget.MEDIA_PLAY"
        const val ACTION_NEXT = "com.android.launcher3.owl.widget.MEDIA_NEXT"
    }
}

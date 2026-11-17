/*
 * Owl weather widget: current temperature and condition from Open-Meteo.
 * Location comes from the last known fix, and only when the user has allowed it in settings.
 * The last reading is kept, so the widget still shows something when the network is down.
 */
package com.android.launcher3.owl.widget

import android.Manifest
import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.android.launcher3.R
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt

class OwlWeatherWidget : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        // Network work runs off the main thread; goAsync keeps the receiver alive until it ends.
        val pending = goAsync()
        Thread {
            try {
                super.onReceive(context, intent)
            } finally {
                pending.finish()
            }
        }.start()
    }

    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        val reading = refresh(context)
        for (id in appWidgetIds) {
            manager.updateAppWidget(id, views(context, reading))
        }
    }

    private data class Reading(val temp: Int, val code: Int)

    private fun views(context: Context, reading: Reading?): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.owl_weather_widget)
        if (reading == null) {
            views.setTextViewText(R.id.owl_weather_temp, "--")
            views.setTextViewText(R.id.owl_weather_label, "Allow location in settings")
        } else {
            views.setTextViewText(R.id.owl_weather_temp, "${reading.temp}°")
            views.setTextViewText(R.id.owl_weather_label, condition(reading.code))
        }
        return views
    }

    private fun refresh(context: Context): Reading? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val location = location(context)
        if (location != null) {
            fetch(location.latitude, location.longitude)?.let { fresh ->
                prefs.edit().putInt("temp", fresh.temp).putInt("code", fresh.code).apply()
                return fresh
            }
        }
        // No location or no network: fall back to the last reading, if any.
        if (!prefs.contains("code")) return null
        return Reading(prefs.getInt("temp", 0), prefs.getInt("code", 0))
    }

    @SuppressLint("MissingPermission")
    private fun location(context: Context): Location? {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) return null
        val manager = context.getSystemService(LocationManager::class.java) ?: return null
        return listOf(LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER)
            .mapNotNull { provider ->
                try {
                    manager.getLastKnownLocation(provider)
                } catch (e: SecurityException) {
                    null
                }
            }
            .maxByOrNull { it.time }
    }

    private fun fetch(lat: Double, lon: Double): Reading? {
        val url = URL(
            "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon" +
                "&current=temperature_2m,weather_code"
        )
        val connection = url.openConnection() as HttpURLConnection
        return try {
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val current = JSONObject(body).getJSONObject("current")
            Reading(current.getDouble("temperature_2m").roundToInt(), current.getInt("weather_code"))
        } catch (e: Exception) {
            null
        } finally {
            connection.disconnect()
        }
    }

    /** WMO weather interpretation codes, grouped to the words the widget shows. */
    private fun condition(code: Int): String = when (code) {
        0 -> "Clear"
        1, 2, 3 -> "Cloudy"
        45, 48 -> "Fog"
        51, 53, 55, 56, 57 -> "Drizzle"
        61, 63, 65, 66, 67 -> "Rain"
        71, 73, 75, 77 -> "Snow"
        80, 81, 82 -> "Showers"
        85, 86 -> "Snow showers"
        95, 96, 99 -> "Thunderstorm"
        else -> "Unknown"
    }

    private companion object {
        const val PREFS = "owl_weather"
    }
}

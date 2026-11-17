/*
 * Owl: export and restore the launcher settings as one JSON file.
 * The file holds the launcher's shared preferences (the backed-up file), so every setting
 * the launcher reads is included.
 */
package com.android.launcher3.owl

import android.content.Context
import android.net.Uri
import android.content.Context.MODE_PRIVATE
import com.android.launcher3.InvariantDeviceProfile
import com.android.launcher3.LauncherAppState
import com.android.launcher3.LauncherFiles
import org.json.JSONArray
import org.json.JSONObject

object OwlBackup {

    private const val FORMAT = 1
    private const val GRID_KEY = "idp_grid_name"

    private fun prefs(context: Context) =
        context.getSharedPreferences(LauncherFiles.SHARED_PREFERENCES_KEY, MODE_PRIVATE)

    fun export(context: Context, uri: Uri): Boolean = try {
        val values = JSONObject()
        for ((key, value) in prefs(context).all) {
            when (value) {
                is Set<*> -> values.put(key, JSONObject().put("set", JSONArray(value.toList())))
                null -> Unit
                else -> values.put(key, value)
            }
        }
        val root = JSONObject().put("format", FORMAT).put("values", values)
        context.contentResolver.openOutputStream(uri)?.use { it.write(root.toString(2).toByteArray()) }
        true
    } catch (e: Exception) {
        false
    }

    fun restore(context: Context, uri: Uri): Boolean = try {
        val text = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
            ?: return false
        val values = JSONObject(text).getJSONObject("values")
        val editor = prefs(context).edit()
        val keys = values.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            when (val value = values.get(key)) {
                is Boolean -> editor.putBoolean(key, value)
                is Int -> editor.putInt(key, value)
                is Long -> editor.putLong(key, value)
                is Double -> editor.putFloat(key, value.toFloat())
                is String -> editor.putString(key, value)
                is JSONObject -> value.optJSONArray("set")?.let { arr ->
                    editor.putStringSet(key, (0 until arr.length()).map { arr.getString(it) }.toSet())
                }
            }
        }
        editor.commit()
        // Apply the restored grid right away; the other settings are read on the next reload.
        if (values.has(GRID_KEY)) {
            InvariantDeviceProfile.INSTANCE.get(context).setCurrentGrid(values.getString(GRID_KEY))
        }
        OwlAppearance.markDirty(context)
        LauncherAppState.getInstance(context).model.forceReload()
        true
    } catch (e: Exception) {
        false
    }
}

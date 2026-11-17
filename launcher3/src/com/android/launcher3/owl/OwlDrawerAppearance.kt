/*
 * App drawer background: blur of the wallpaper behind the drawer (Android 12+), and an
 * optional picture behind the drawer with a dim layer.
 */
package com.android.launcher3.owl

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import com.android.launcher3.LauncherPrefs
import java.io.File
import kotlin.math.max
import kotlin.math.roundToInt

object OwlDrawerAppearance {

    private const val PICTURE_FILE = "owl_drawer_picture.jpg"
    private const val MAX_BLUR_DP = 80f

    /** [openness] is 0 when the drawer is closed and 1 when it is fully open. */
    fun applyBlur(activity: Activity, openness: Float) {
        val context = activity
        val on = LauncherPrefs.get(context).get(LauncherPrefs.OWL_DRAWER_BLUR)
        val radius = if (on) {
            val strength = LauncherPrefs.get(context).get(LauncherPrefs.OWL_DRAWER_BLUR_STRENGTH) / 100f
            (openness.coerceIn(0f, 1f) * strength * MAX_BLUR_DP * context.resources.displayMetrics.density)
                .roundToInt()
        } else {
            0
        }
        activity.window.setBackgroundBlurRadius(radius)
    }

    private var cachedPicture: Bitmap? = null
    private var cachedStamp = -1L

    /** The drawer picture, decoded once and reloaded when the file changes. */
    fun picture(context: Context): Bitmap? {
        val file = File(context.filesDir, PICTURE_FILE)
        if (!file.exists()) return null
        val stamp = file.lastModified()
        if (cachedPicture == null || stamp != cachedStamp) {
            val options = BitmapFactory.Options().apply { inSampleSize = 2 }
            cachedPicture = BitmapFactory.decodeFile(file.path, options)
            cachedStamp = stamp
        }
        return cachedPicture
    }

    /** Draws the picture centred and cropped over the whole canvas, then the dim layer. */
    fun drawPicture(canvas: Canvas, context: Context) {
        val bitmap = picture(context) ?: return
        val w = canvas.width.toFloat()
        val h = canvas.height.toFloat()
        val scale = max(w / bitmap.width, h / bitmap.height)
        val srcW = w / scale
        val srcH = h / scale
        val src = Rect(
            ((bitmap.width - srcW) / 2).toInt(),
            ((bitmap.height - srcH) / 2).toInt(),
            ((bitmap.width + srcW) / 2).toInt(),
            ((bitmap.height + srcH) / 2).toInt(),
        )
        canvas.drawBitmap(bitmap, src, RectF(0f, 0f, w, h), Paint(Paint.FILTER_BITMAP_FLAG))
        val dim = LauncherPrefs.get(context).get(LauncherPrefs.OWL_DRAWER_PICTURE_DIM) / 100f
        if (dim > 0f) {
            canvas.drawColor(Color.argb((255 * dim).roundToInt(), 0, 0, 0))
        }
    }

    fun importPicture(context: Context, uri: Uri): Boolean = try {
        context.contentResolver.openInputStream(uri)?.use { input ->
            File(context.filesDir, PICTURE_FILE).outputStream().use { input.copyTo(it) }
        }
        LauncherPrefs.get(context).put(LauncherPrefs.OWL_APPEARANCE_DIRTY to true)
        true
    } catch (e: Exception) {
        false
    }

    fun clearPicture(context: Context) {
        File(context.filesDir, PICTURE_FILE).delete()
        LauncherPrefs.get(context).put(LauncherPrefs.OWL_APPEARANCE_DIRTY to true)
    }
}

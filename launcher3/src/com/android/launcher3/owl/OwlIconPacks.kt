/*
 * Minimal icon pack support. A pack is an installed app that declares one of the common
 * icon-pack intents and ships res/xml/appfilter.xml mapping components to drawables.
 */
package com.android.launcher3.owl

import android.content.ComponentName
import android.content.Context
import com.android.launcher3.LauncherAppState
import com.android.launcher3.LauncherPrefs
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Xml
import org.xmlpull.v1.XmlPullParser

object OwlIconPacks {

    /**
     * Stores the choice and drops cached icons. Home and All Apps reload with the new pack,
     * without restarting the launcher.
     */
    fun select(context: Context, pkg: String) {
        LauncherPrefs.get(context).put(LauncherPrefs.OWL_ICON_PACK to pkg)
        val app = LauncherAppState.getInstance(context)
        app.iconCache.invalidateAllIcons {
            android.os.Handler(android.os.Looper.getMainLooper()).post { app.model.forceReload() }
        }
    }


    private val PACK_INTENTS = listOf(
        "org.adw.launcher.THEMES",
        "com.novalauncher.THEME",
        "com.anddoes.launcher.THEME",
        "com.gau.go.launcherex.theme",
    )

    /** Package names of installed icon packs, sorted by label. */
    fun installedPacks(context: Context): List<String> {
        val pm = context.packageManager
        return PACK_INTENTS
            .flatMap { action -> pm.queryIntentActivities(Intent(action), 0) }
            .map { it.activityInfo.packageName }
            .distinct()
            .sortedBy { pkg -> labelOf(context, pkg) }
    }

    fun labelOf(context: Context, packageName: String): String = try {
        context.packageManager.getApplicationLabel(
            context.packageManager.getApplicationInfo(packageName, 0)).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        packageName
    }

    /** The package of the selected icon pack, or empty for none. */
    fun selected(context: Context): String = LauncherPrefs.get(context).get(LauncherPrefs.OWL_ICON_PACK)

    /** Icon from the selected pack for [component], or null to use the default icon. */
    fun iconFor(context: Context, component: ComponentName, dpi: Int): Drawable? {
        val pack = selected(context)
        if (pack.isEmpty()) return null
        return try {
            val packContext = context.createPackageContext(pack, 0)
            val res = packContext.resources
            val mapping = readAppFilter(packContext)
            val drawableName = mapping[component] ?: return null
            val id = res.getIdentifier(drawableName, "drawable", pack)
            if (id == 0) null else res.getDrawableForDensity(id, dpi, null)
        } catch (e: Exception) {
            android.util.Log.w("OwlIconPacks", "Icon pack $pack failed for $component", e)
            null
        }
    }

    private fun readAppFilter(packContext: Context): Map<ComponentName, String> {
        val res = packContext.resources
        val map = HashMap<ComponentName, String>()
        val xmlId = res.getIdentifier("appfilter", "xml", packContext.packageName)
        val parser: XmlPullParser = if (xmlId != 0) {
            res.getXml(xmlId)
        } else {
            val assetParser = android.util.Xml.newPullParser()
            try {
                packContext.assets.open("appfilter.xml").use { input ->
                    assetParser.setInput(input, "utf-8")
                }
            } catch (e: java.io.IOException) {
                return emptyMap()
            }
            assetParser
        }
        try {
            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG && parser.name == "item") {
                    val component = parser.getAttributeValue(null, "component")
                    val drawable = parser.getAttributeValue(null, "drawable")
                    if (component != null && drawable != null) {
                        // Format: ComponentInfo{package/class}
                        val inner = component.removePrefix("ComponentInfo{").removeSuffix("}")
                        val name: ComponentName? = ComponentName.unflattenFromString(inner)
                        if (name != null) map[name] = drawable
                    }
                }
                event = parser.next()
            }
        } finally {
            parser.close()
        }
        return map
    }
}
